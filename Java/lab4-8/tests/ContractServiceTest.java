import model.Contract;
import model.Customer;
import model.Derivative;
import service.ContractService;
import service.ContractProcessor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.util.*;
import java.time.LocalDate;
import static model.Contract.RiskLevel.*;

public class ContractServiceTest {
    private ContractService contractService;
    private List<Customer> customers;
    private List<Contract> contracts;
    private List<Derivative> derivatives;
    private ContractProcessor contractProcessor;
    @BeforeEach
    void setUp() {
        String simulatedInput = "1\n01.01.2024\n31.12.2024\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));
        
        contractService = new ContractService(new Scanner(System.in));
        customers = new ArrayList<>();
        contracts = new ArrayList<>();
        derivatives = new ArrayList<>();
        contractProcessor = new ContractProcessor();
        customers.add(new Customer(1, "Nazar", "Senchuk", "123-456-789", "nazar@test.com"));
    }
     @Test
    void testCreateContractInteractive() {
        Derivative existingDerivative = new Derivative();
        existingDerivative.setDerivativeId(1);
        existingDerivative.setCustomerId(1);
        derivatives.add(existingDerivative);
        
        String input = "1\n01.01.2024\n31.12.2024\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> 
            contractService.createContractInteractive(customers, contracts, contractProcessor, derivatives)
        );
        
        assertEquals(1, contracts.size());
        assertEquals(1, derivatives.size()); 
    }
    @Test
    void testShowContractsStatistics() {
        contracts.add(new Contract(1, 1001, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        contracts.add(new Contract(2, 1002, LocalDate.now(), LocalDate.now().plusDays(200), true, MEDIUM, 2));
        contracts.add(new Contract(3, 1003, LocalDate.now(), LocalDate.now().plusDays(300), false, HIGH, 3));
        
        assertDoesNotThrow(() -> contractService.showContractsStatistics(contracts));
    }
    
    @Test
    void testShowContractsStatistics_Empty() {
        assertDoesNotThrow(() -> contractService.showContractsStatistics(new ArrayList<>()));
    }
    
    @Test
    void testViewAllContracts() {
        contracts.add(new Contract(1, 1001, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        
        assertDoesNotThrow(() -> contractService.viewAllContracts(contracts, customers));
    }
    
    @Test
    void testViewAllContracts_Empty() {
        assertDoesNotThrow(() -> contractService.viewAllContracts(new ArrayList<>(), customers));
    }
    
    @Test
    void testViewCustomerContracts() {
        contracts.add(new Contract(1, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.viewCustomerContracts(customers, contracts));
    }
    
    @Test
    void testFindContract() {
        contracts.add(new Contract(1001, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        
        String input = "1\n1001\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.findContract(customers, contracts));
    }
    
    @Test
    void testDisplayAllDerivatives() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivatives.add(derivative);
        
        assertDoesNotThrow(() -> contractService.displayAllDerivatives(derivatives, customers));
    }
    
    @Test
    void testFindDerivativeByCustomer() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivatives.add(derivative);
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.findDerivativeByCustomer(derivatives, customers));
    }
    
    @Test
    void testSortDerivativeContractsByRisk() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivative.addContract(new Contract(1, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, HIGH, 1));
        derivative.addContract(new Contract(2, 1, LocalDate.now(), LocalDate.now().plusDays(50), true, LOW, 1));
        derivatives.add(derivative);
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.sortDerivativeContractsByRisk(derivatives));
    }
    
    @Test
    void testSearchContractsInDerivative() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivative.addContract(new Contract(1, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        derivatives.add(derivative);
        
        String input = "1\nLOW\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.searchContractsInDerivative(derivatives));
    }
    
    @Test
    void testCalculateDerivativeValue() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivative.addContract(new Contract(1, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        derivatives.add(derivative);
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.calculateDerivativeValue(derivatives));
    }
    
    @Test
    void testViewDerivativeContracts() {
        Derivative derivative = new Derivative();
        derivative.setDerivativeId(1);
        derivative.setCustomerId(1);
        derivative.addContract(new Contract(1, 1, LocalDate.now(), LocalDate.now().plusDays(100), true, LOW, 1));
        derivatives.add(derivative);
        
        String input = "1\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.viewDerivativeContracts(derivatives, customers));
    }
    
    @Test
    void testRegisterCustomer() {
        String input = "Jane\nSmith\n987-654-321\njane@test.com\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        contractService = new ContractService(new Scanner(System.in));
        
        assertDoesNotThrow(() -> contractService.registerCustomer(customers));
        assertEquals(2, customers.size());
    }
}
