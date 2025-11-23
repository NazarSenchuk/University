import model.Contract;
import model.Derivative;
import service.ContractProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.Arrays;
import static model.Contract.RiskLevel.*;

public class ContractProcessorTest {
    
    @Test
    void testCalculateRisk_Low() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 6, 1),  // 152 дні - LOW
            true, null, 1);
            
        Contract.RiskLevel risk = ContractProcessor.calculateRisk(contract);
        assertEquals(LOW, risk);
        assertEquals(LOW, contract.getRiskLevel());
    }
    
    @Test
    void testCalculateRisk_Medium() {
        Contract contract = new Contract(2, 1002, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 7, 1),  // 182 дні - MEDIUM
            true, null, 1);
            
        Contract.RiskLevel risk = ContractProcessor.calculateRisk(contract);
        assertEquals(MEDIUM, risk);
    }
    
    @Test
    void testCalculateRisk_High() {
        Contract contract = new Contract(3, 1003, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2025, 1, 2),  // 367 днів - HIGH
            true, null, 1);
            
        Contract.RiskLevel risk = ContractProcessor.calculateRisk(contract);
        assertEquals(HIGH, risk);
    }
    
    @Test
    void testCalculateValue_LowRisk() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 1, 11),  // 10 днів
            true, LOW, 1);
            
        float value = ContractProcessor.calculateValue(contract);
        assertEquals(100.0f, value, 0.001);  // 10 днів * 10.0 * 1.0
    }
    
    @Test
    void testCalculateValue_MediumRisk() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 1, 11),  // 10 днів
            true, MEDIUM, 1);
            
        float value = ContractProcessor.calculateValue(contract);
        assertEquals(120.0f, value, 0.001);  // 10 днів * 10.0 * 1.2
    }
    
    @Test
    void testCalculateValue_HighRisk() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 1, 11),  // 10 днів
            true, HIGH, 1);
            
        float value = ContractProcessor.calculateValue(contract);
        assertEquals(150.0f, value, 0.001);  // 10 днів * 10.0 * 1.5
    }
    
    @Test
    void testCalculateTotalValue() {
        Derivative derivative = new Derivative();
        
        Contract contract1 = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 1, 11), 
            true, LOW, 1);
            
        Contract contract2 = new Contract(2, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 1, 11), 
            true, MEDIUM, 1);
            
        derivative.addContract(contract1);
        derivative.addContract(contract2);
        
        float totalValue = ContractProcessor.calculateTotalValue(derivative);
        assertEquals(220.0f, totalValue, 0.001);  // 100 + 120
    }
    
    @Test
    void testValidateContract_Valid() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31), 
            true, LOW, 1);
            
        assertTrue(ContractProcessor.validateContract(contract));
    }
    
    @Test
    void testValidateContract_InvalidDates() {
        Contract contract = new Contract(1, 1001, 
            LocalDate.of(2024, 12, 31), 
            LocalDate.of(2024, 1, 1), 
            true, LOW, 1);
            
        assertFalse(ContractProcessor.validateContract(contract));
    }
    
    @Test
    void testValidateContract_NullDates() {
        Contract contract = new Contract(1, 1001, null, null, true, LOW, 1);
        assertFalse(ContractProcessor.validateContract(contract));
    }
}
