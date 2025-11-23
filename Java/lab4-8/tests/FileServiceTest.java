import service.FileService;
import model.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.nio.file.Path;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static model.Contract.RiskLevel.*;

public class FileServiceTest {
    
    @Test
    void testSaveAndLoadCustomers() {
        List<Customer> customers = new ArrayList<>();
        customers.add(new Customer(1, "John", "Doe", "123-456-789", "john@test.com"));
        customers.add(new Customer(2, "Jane", "Smith", "987-654-321", "jane@test.com"));
        
        assertDoesNotThrow(() -> FileService.saveCustomers(customers));
    }
    
    @Test
    void testSaveAndLoadContracts() {
        List<Contract> contracts = new ArrayList<>();
        contracts.add(new Contract(1001, 1, 
            LocalDate.of(2024, 1, 1), 
            LocalDate.of(2024, 12, 31), 
            true, LOW, 1));
        
        assertDoesNotThrow(() -> FileService.saveContracts(contracts));
    }
    
    @Test
    void testSaveAndLoadClaims() {
        List<Claim> claims = new ArrayList<>();
        claims.add(new Claim(1, 1001, 1, 500.0f, "Test claim", LocalDate.now(), "PENDING"));
        
        assertDoesNotThrow(() -> FileService.saveClaims(claims));
    }
    
    @Test
    void testSaveAndLoadDerivatives() {
        List<Derivative> derivatives = new ArrayList<>();
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1001);
        derivatives.add(derivative);
        
        assertDoesNotThrow(() -> FileService.saveDerivatives(derivatives));
    }
    
    @Test
    void testLoadAll() {
        List<Customer> customers = new ArrayList<>();
        List<Derivative> derivatives = new ArrayList<>();
        List<Contract> contracts = new ArrayList<>();
        List<Claim> claims = new ArrayList<>();
        
        assertDoesNotThrow(() -> FileService.loadAll(customers, derivatives, contracts, claims));
    }
}
