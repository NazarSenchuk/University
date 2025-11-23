import model.Derivative;
import model.Contract;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.List;
import static model.Contract.RiskLevel.*;

public class DerivativeTest {
    private Derivative derivative;
    private Contract contract1;
    private Contract contract2;
    
    @BeforeEach
    void setUp() {
        derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1001);
        
        contract1 = new Contract(1, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 6, 1), 
            true, LOW, 1);
            
        contract2 = new Contract(2, 1001, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31), 
            true, HIGH, 1);
    }
    
    @Test
    void testDerivativeCreation() {
        assertNotNull(derivative);
        assertEquals(1, derivative.getDerivativeId());
        assertEquals(1001, derivative.getCustomerId());
        assertTrue(derivative.getContracts().isEmpty());
    }
    
    @Test
    void testAddContract() {
        derivative.addContract(contract1);
        derivative.addContract(contract2);
        
        List<Contract> contracts = derivative.getContracts();
        assertEquals(2, contracts.size());
    }
    
    @Test
    void testSortByRisk() {
        derivative.addContract(contract2);
        derivative.addContract(contract1);
        
        derivative.sortByRisk();
        
        List<Contract> contracts = derivative.getContracts();
        assertEquals(LOW, contracts.get(0).getRiskLevel());
        assertEquals(HIGH, contracts.get(1).getRiskLevel());
    }
    
    @Test
    void testSearchContract() {
        derivative.addContract(contract1);
        derivative.addContract(contract2);
        
        List<Contract> lowRiskResults = derivative.searchContract("LOW");
        assertEquals(1, lowRiskResults.size());
        
        List<Contract> noResults = derivative.searchContract("NONEXISTENT");
        assertTrue(noResults.isEmpty());
    }
    
    @Test
    void testSetters() {
        derivative.setDerivativeId(999);
        derivative.setCustomerId(888);
        
        assertEquals(999, derivative.getDerivativeId());
        assertEquals(888, derivative.getCustomerId());
    }
    
    @Test
    void testRemoveContract() {
        derivative.addContract(contract1);
        derivative.addContract(contract2);
        assertEquals(2, derivative.getContracts().size());
        
        derivative.removeContract(contract1);
        assertEquals(1, derivative.getContracts().size());
        assertEquals(contract2, derivative.getContracts().get(0));
    }
    
    @Test
    void testEmptyDerivative() {
        Derivative emptyDerivative = new Derivative();
        assertNotNull(emptyDerivative);
        assertTrue(emptyDerivative.getContracts().isEmpty());
    }
    
    @Test
    void testSearchByContractNumber() {
        derivative.addContract(contract1);
        derivative.addContract(contract2);
        
        List<Contract> results = derivative.searchContract("1");
        assertEquals(1, results.size());
        assertEquals(1, results.get(0).getContractNumber());
    }
}
    
   