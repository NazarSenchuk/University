import model.Contract;
import service.ContractProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import static model.Contract.RiskLevel.*;

public class ContractTest {
    private Contract contract;
    
    @BeforeEach
    void setUp() {
        contract = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31), 
            true, LOW, 1);
    }
    
    @Test
    void testContractCreation() {
        assertNotNull(contract);
        assertEquals(1, contract.getContractNumber());
        assertEquals(1001, contract.getCustomerId());
        assertEquals(LOW, contract.getRiskLevel());
        assertTrue(contract.isActive());
    }
    
    @Test
    void testContractValidation() {
        assertTrue(ContractProcessor.validateContract(contract));
        
        Contract invalidContract = new Contract(2, 1002, 
            LocalDate.of(2024, 12, 31), 
            LocalDate.of(2024, 1, 1), 
            true, LOW, 1);
        assertFalse(ContractProcessor.validateContract(invalidContract));
    }
    
    @Test
    void testRiskCalculation() {
        Contract.RiskLevel riskLevel = ContractProcessor.calculateRisk(contract);
        assertNotNull(riskLevel);
    }
    
    @Test
    void testValueCalculation() {
        float value = ContractProcessor.calculateValue(contract);
        assertTrue(value > 0);
    }
    
    @Test
    void testContractSetters() {
        contract.setActive(false);
        contract.setRiskLevel(HIGH);
        
        assertFalse(contract.isActive());
        assertEquals(HIGH, contract.getRiskLevel());
    }
    
    @Test
    void testGetters() {
        assertEquals(1, contract.getDerivativeId());
        assertNotNull(contract.getStartDate());
        assertNotNull(contract.getEndDate());
    }
    
    @Test
    void testEmptyConstructor() {
        Contract emptyContract = new Contract();
        assertNotNull(emptyContract);
    }
    
    @Test
    void testRiskLevelEnum() {
        assertEquals("LOW", LOW.name());
        assertEquals("MEDIUM", MEDIUM.name());
        assertEquals("HIGH", HIGH.name());
    }
    
    @Test
    void testContractWithNullDates() {
        Contract nullContract = new Contract();
        nullContract.setContractNumber(1);
        nullContract.setCustomerId(1001);
        nullContract.setRiskLevel(HIGH);
        
        assertNull(nullContract.getStartDate());
        assertNull(nullContract.getEndDate());
        assertEquals(HIGH, nullContract.getRiskLevel());
    }
    
    @Test
    void testContractToStringWithNullValues() {
        Contract nullContract = new Contract();
        nullContract.setContractNumber(999);
        nullContract.setRiskLevel(MEDIUM);
        
        String result = nullContract.toString();
        assertNotNull(result);
        assertTrue(result.contains("Контракт #999"));
        assertTrue(result.contains("Ризик: MEDIUM"));
    }
    
    @Test
    void testFullConstructor() {
        Contract fullContract = new Contract(999, 888, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31), 
            false, HIGH, 777);
        
        assertEquals(999, fullContract.getContractNumber());
        assertEquals(888, fullContract.getCustomerId());
        assertEquals(777, fullContract.getDerivativeId());
        assertEquals(HIGH, fullContract.getRiskLevel());
        assertFalse(fullContract.isActive());
    }
    
    @Test
    void testAllSetters() {
        Contract testContract = new Contract();
        
        testContract.setContractNumber(555);
        testContract.setCustomerId(666);
        testContract.setDerivativeId(777);
        testContract.setStartDate(LocalDate.of(2024, 1, 1));
        testContract.setEndDate(LocalDate.of(2024, 12, 31));
        testContract.setActive(false);
        testContract.setRiskLevel(MEDIUM);
        
        assertEquals(555, testContract.getContractNumber());
        assertEquals(666, testContract.getCustomerId());
        assertEquals(777, testContract.getDerivativeId());
        assertEquals(MEDIUM, testContract.getRiskLevel());
        assertFalse(testContract.isActive());
    }
}
