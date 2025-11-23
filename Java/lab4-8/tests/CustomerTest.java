import model.Customer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CustomerTest {
    private Customer customer;
    
    @BeforeEach
    void setUp() {
        customer = new Customer(1, "John", "Doe", "123-456-789", "john@test.com");
    }
    
    @Test
    void testCustomerCreation() {
        assertNotNull(customer);
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("123-456-789", customer.getPhone());
        assertEquals("john@test.com", customer.getEmail());
    }
    
    @Test
    void testCustomerSetters() {
        customer.setFirstName("Jane");
        customer.setLastName("Smith");
        customer.setPhone("987-654-321");
        customer.setEmail("jane@test.com");
        customer.setId(999);
        
        assertEquals("Jane", customer.getFirstName());
        assertEquals("Smith", customer.getLastName());
        assertEquals("987-654-321", customer.getPhone());
        assertEquals("jane@test.com", customer.getEmail());
        assertEquals(999, customer.getId());
    }
    
    @Test
    void testCustomerToString() {
        String toString = customer.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("John Doe"));
    }
    
    @Test
    void testEmptyConstructor() {
        Customer emptyCustomer = new Customer();
        assertNotNull(emptyCustomer);
        assertNull(emptyCustomer.getFirstName());
    }
    
    @Test
    void testFullConstructor() {
        Customer fullCustomer = new Customer(999, "Alice", "Brown", "555-1234", "alice@test.com");
        assertEquals(999, fullCustomer.getId());
        assertEquals("Alice", fullCustomer.getFirstName());
        assertEquals("Brown", fullCustomer.getLastName());
        assertEquals("555-1234", fullCustomer.getPhone());
        assertEquals("alice@test.com", fullCustomer.getEmail());
    }
    
    @Test
    void testAllSetters() {
        Customer testCustomer = new Customer();
        
        testCustomer.setId(555);
        testCustomer.setFirstName("Bob");
        testCustomer.setLastName("Wilson");
        testCustomer.setPhone("111-222-3333");
        testCustomer.setEmail("bob@test.com");
        
        assertEquals(555, testCustomer.getId());
        assertEquals("Bob", testCustomer.getFirstName());
        assertEquals("Wilson", testCustomer.getLastName());
        assertEquals("111-222-3333", testCustomer.getPhone());
        assertEquals("bob@test.com", testCustomer.getEmail());
    }
    
    @Test
    void testCustomerWithNullValues() {
        Customer nullCustomer = new Customer();
        nullCustomer.setId(1);
        
        assertEquals(1, nullCustomer.getId());
        assertNull(nullCustomer.getFirstName());
        assertNull(nullCustomer.getLastName());
        assertNull(nullCustomer.getPhone());
        assertNull(nullCustomer.getEmail());
    }
    
    @Test
    void testCustomerToStringWithNullValues() {
        Customer nullCustomer = new Customer();
        nullCustomer.setId(999);
        
        String result = nullCustomer.toString();
        assertNotNull(result);
        assertTrue(result.contains("ID: 999"));
    }
}
