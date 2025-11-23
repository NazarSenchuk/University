import model.Claim;
import model.Contract;
import service.ClaimService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.time.LocalDate;
import static model.Contract.RiskLevel.*;

public class ClaimServiceTest {
    private ClaimService claimService;
    private List<Claim> claims;
    private List<Contract> contracts;
    
    @BeforeEach
    void setUp() {
        String simulatedInput = "1001\n500.50\nTest claim\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        
        claimService = new ClaimService(new Scanner(System.in));
        claims = new ArrayList<>();
        contracts = new ArrayList<>();
        
        contracts.add(new Contract(1001, 1, 
            LocalDate.now().minusDays(30), 
            LocalDate.now().plusDays(30), 
            true, LOW, 1));
    }
    
     
    @Test
    void testSubmitClaim() {
        String input = "1001\n500.50\nTest claim description\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        claimService = new ClaimService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> claimService.submitClaim(contracts, claims));
        
        assertEquals(1, claims.size());
        Claim claim = claims.get(0);
        assertEquals(1001, claim.getContractNumber());
        assertEquals(500.50f, claim.getAmount(), 0.001);
        assertEquals("Test claim description", claim.getDescription());
        assertEquals("На розгляді", claim.getStatus());
    }
    @Test
    void testViewCustomerClaims_WithClaims() {
        claims.add(new Claim(1, 1001, 1, 500.0f, "Test", LocalDate.now(), "PENDING"));
        
        assertDoesNotThrow(() -> claimService.viewCustomerClaims(claims, 1));
    }
    
    @Test
    void testViewCustomerClaims_NoClaims() {
        assertDoesNotThrow(() -> claimService.viewCustomerClaims(claims, 1));
    }
    
    @Test
    void testViewAllClaims_WithClaims() {
        claims.add(new Claim(1, 1001, 1, 500.0f, "Test", LocalDate.now(), "PENDING"));
        claims.add(new Claim(2, 1002, 2, 300.0f, "Test 2", LocalDate.now(), "APPROVED"));
        
        assertDoesNotThrow(() -> claimService.viewAllClaims(claims));
    }
    
    @Test
    void testViewAllClaims_Empty() {
        assertDoesNotThrow(() -> claimService.viewAllClaims(new ArrayList<>()));
    }
    
    @Test
    void testReviewClaim_Approve() {
        claims.add(new Claim(1, 1001, 1, 500.0f, "Test", LocalDate.now(), "PENDING"));
        contracts.add(new Contract(1001, 1, LocalDate.now(), LocalDate.now().plusDays(365), true, LOW, 1));
        
        String input = "1\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        claimService = new ClaimService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> claimService.reviewClaim(claims, contracts));
    }
    
    @Test
    void testReviewClaim_Reject() {
        claims.add(new Claim(1, 1001, 1, 500.0f, "Test", LocalDate.now(), "PENDING"));
        
        String input = "1\n2\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        claimService = new ClaimService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> claimService.reviewClaim(claims, contracts));
    }
    
    @Test
    void testReviewClaim_NotFound() {
        String input = "999\n1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        claimService = new ClaimService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> claimService.reviewClaim(claims, contracts));
    }
}
