import model.*;
import service.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;
import java.util.*;
import static model.Contract.RiskLevel.*;
import static model.Contract.RiskLevel;

public class AdditionalCoverageTest {
    
    @Test
    void testCompanyAgent() {
        CompanyAgent agent = new CompanyAgent(1, "John", "Agent");
        assertEquals(1, agent.getAgentId());
        assertEquals("John", agent.getFirstName());
        assertEquals("Agent", agent.getLastName());
        
        agent.setAgentId(2);
        agent.setFirstName("Jane");
        agent.setLastName("Smith");
        
        assertEquals(2, agent.getAgentId());
        assertEquals("Jane", agent.getFirstName());
        assertEquals("Smith", agent.getLastName());
        
        assertNotNull(agent.toString());
    }
    
    @Test
    void testCustomerInputMethods() {
        assertDoesNotThrow(() -> {
            String input = "123-456-789\n";
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            String phone = Customer.getPhone(scanner, "телефон");
            assertEquals("123-456-789", phone);
        });
    }
    
    @Test
    void testClaimInputMethods() {
        assertDoesNotThrow(() -> {
            String input = "500.50\n";
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            float amount = Claim.getSumOfClaim(scanner, "суму");
            assertEquals(500.50f, amount, 0.001);
        });
    }
    
    @Test
    void testContractInputMethods() {
        assertDoesNotThrow(() -> {
            String input = "25.12.2024\n";
            System.setIn(new java.io.ByteArrayInputStream(input.getBytes()));
            java.util.Scanner scanner = new java.util.Scanner(System.in);
            LocalDate date = Contract.yearInput("дату", scanner);
            assertEquals(LocalDate.of(2024, 12, 25), date);
        });
    }
    
    @Test
    void testDerivativeAdditionalMethods() {
        Derivative derivative = new Derivative();
        Contract contract = new Contract(1, 1001, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1);
        
        derivative.addContract(contract);
        assertEquals(1, derivative.getContracts().size());
        
        derivative.removeContract(contract);
        assertEquals(0, derivative.getContracts().size());
    }
    
    @Test
    void testFileServiceEdgeCases() {
        assertDoesNotThrow(() -> {
            FileService.saveCustomers(new ArrayList<>());
            FileService.saveContracts(new ArrayList<>());
            FileService.saveClaims(new ArrayList<>());
            FileService.saveDerivatives(new ArrayList<>());
        });
    }
    
    @Test
    void testContractEdgeCases() {
        Contract contract = new Contract();
        assertNotNull(contract);
        
        contract.setContractNumber(999);
        contract.setCustomerId(888);
        contract.setDerivativeId(777);
        contract.setStartDate(LocalDate.now());
        contract.setEndDate(LocalDate.now().plusDays(1));
        contract.setActive(false);
        contract.setRiskLevel(HIGH);
        
        assertEquals(999, contract.getContractNumber());
        assertEquals(888, contract.getCustomerId());
        assertEquals(777, contract.getDerivativeId());
        assertEquals(HIGH, contract.getRiskLevel());
        assertFalse(contract.isActive());
    }
    
    @Test
    void testClaimEdgeCases() {
        Claim claim = new Claim();
        assertNotNull(claim);
        
        claim.setClaimId(999);
        claim.setContractNumber(888);
        claim.setCustomerId(777);
        claim.setAmount(1000.50f);
        claim.setDescription("Test Description");
        claim.setStatus("TEST_STATUS");
        claim.setSubmissionDate(LocalDate.now());
        
        assertEquals(999, claim.getClaimId());
        assertEquals(888, claim.getContractNumber());
        assertEquals(777, claim.getCustomerId());
        assertEquals(1000.50f, claim.getAmount(), 0.001);
        assertEquals("Test Description", claim.getDescription());
        assertEquals("TEST_STATUS", claim.getStatus());
    }
    
    @Test
    void testRiskLevelCoverage() {
        assertEquals(4, RiskLevel.values().length);
        assertNotNull(RiskLevel.valueOf("LOW"));
        assertNotNull(RiskLevel.valueOf("MEDIUM"));
        assertNotNull(RiskLevel.valueOf("HIGH"));
    }
}
