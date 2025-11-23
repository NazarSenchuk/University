import model.Claim;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDate;

public class ClaimTest {
    private Claim claim;
    
    @BeforeEach
    void setUp() {
        claim = new Claim(1, 1001, 5001, 500.00f, 
            "Car accident", LocalDate.of(2024, 1, 15), "PENDING");
    }
    
    @Test
    void testClaimCreation() {
        assertNotNull(claim);
        assertEquals(1, claim.getClaimId());
        assertEquals(1001, claim.getContractNumber());
        assertEquals(500.00f, claim.getAmount(), 0.001);
        assertEquals("Car accident", claim.getDescription());
        assertEquals("PENDING", claim.getStatus());
    }
    
    @Test
    void testClaimSetters() {
        claim.setAmount(600.00f);
        claim.setStatus("APPROVED");
        claim.setDescription("Updated description");
        claim.setSubmissionDate(LocalDate.now());
        
        assertEquals(600.00f, claim.getAmount(), 0.001);
        assertEquals("APPROVED", claim.getStatus());
        assertEquals("Updated description", claim.getDescription());
        assertNotNull(claim.getSubmissionDate());
    }
    
    @Test
    void testClaimToString() {
        String toString = claim.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("Вимога #1"));
    }
    
    @Test
    void testCustomerId() {
        assertEquals(5001, claim.getCustomerId());
        claim.setCustomerId(6001);
        assertEquals(6001, claim.getCustomerId());
    }
    
    @Test
    void testContractNumber() {
        claim.setContractNumber(2001);
        assertEquals(2001, claim.getContractNumber());
    }
    
    @Test
    void testEmptyConstructor() {
        Claim emptyClaim = new Claim();
        assertNotNull(emptyClaim);
    }
    
    @Test
    void testFullConstructor() {
        Claim fullClaim = new Claim(1, 1001, 5001, 1000.0f, 
            "Test", LocalDate.now(), "TEST_STATUS");
        assertNotNull(fullClaim);
        assertEquals(1000.0f, fullClaim.getAmount(), 0.001);
    }
    
    @Test
    void testAllSetters() {
        Claim testClaim = new Claim();
        
        testClaim.setClaimId(999);
        testClaim.setContractNumber(888);
        testClaim.setCustomerId(777);
        testClaim.setAmount(1234.56f);
        testClaim.setDescription("Test Description");
        testClaim.setStatus("TEST_STATUS");
        testClaim.setSubmissionDate(LocalDate.of(2024, 12, 31));
        
        assertEquals(999, testClaim.getClaimId());
        assertEquals(888, testClaim.getContractNumber());
        assertEquals(777, testClaim.getCustomerId());
        assertEquals(1234.56f, testClaim.getAmount(), 0.001);
        assertEquals("Test Description", testClaim.getDescription());
        assertEquals("TEST_STATUS", testClaim.getStatus());
        assertEquals(LocalDate.of(2024, 12, 31), testClaim.getSubmissionDate());
    }
    
    @Test
    void testClaimWithNullValues() {
        Claim nullClaim = new Claim();
        nullClaim.setClaimId(1);
        nullClaim.setDescription("Test");
        
        assertEquals(1, nullClaim.getClaimId());
        assertEquals("Test", nullClaim.getDescription());
        assertNull(nullClaim.getSubmissionDate());
    }
    
    @Test
    void testClaimToStringWithNullValues() {
        Claim nullClaim = new Claim();
        nullClaim.setClaimId(999);
        nullClaim.setContractNumber(888);
        
        String result = nullClaim.toString();
        assertNotNull(result);
        assertTrue(result.contains("Вимога #999"));
    }
}
