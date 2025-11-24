import model.CompanyAgent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CompanyAgentTest {
    
    @Test
    void testCompanyAgentCreation() {
        CompanyAgent agent = new CompanyAgent(1, "Nazar", "Doe");
        assertNotNull(agent);
        assertEquals(1, agent.getAgentId());
        assertEquals("Nazar", agent.getFirstName());
        assertEquals("Doe", agent.getLastName());
    }
    
    @Test
    void testCompanyAgentSetters() {
        CompanyAgent agent = new CompanyAgent();
        agent.setAgentId(2);
        agent.setFirstName("Jane");
        agent.setLastName("Smith");
        
        assertEquals(2, agent.getAgentId());
        assertEquals("Jane", agent.getFirstName());
        assertEquals("Smith", agent.getLastName());
    }
    
    @Test
    void testCompanyAgentToString() {
        CompanyAgent agent = new CompanyAgent(1, "Nazar", "Senchuk");
        String result = agent.toString();
        assertNotNull(result);
        assertTrue(result.contains("Nazar Senchuk"));
        assertTrue(result.contains("ID: 1"));
    }
    
    @Test
    void testCompanyAgentEmptyConstructor() {
        CompanyAgent agent = new CompanyAgent();
        assertNotNull(agent);
        assertNull(agent.getFirstName());
        assertNull(agent.getLastName());
        assertEquals(0, agent.getAgentId());
    }
    
    @Test
    void testCompanyAgentFullConstructor() {
        CompanyAgent agent = new CompanyAgent(999, "Alice", "Brown");
        assertEquals(999, agent.getAgentId());
        assertEquals("Alice", agent.getFirstName());
        assertEquals("Brown", agent.getLastName());
    }
    
    @Test
    void testCompanyAgentAllSetters() {
        CompanyAgent agent = new CompanyAgent();
        
        agent.setAgentId(555);
        agent.setFirstName("Bob");
        agent.setLastName("Wilson");
        
        assertEquals(555, agent.getAgentId());
        assertEquals("Bob", agent.getFirstName());
        assertEquals("Wilson", agent.getLastName());
    }
    
    @Test
    void testCompanyAgentWithNullValues() {
        CompanyAgent agent = new CompanyAgent();
        agent.setAgentId(1);
        
        assertEquals(1, agent.getAgentId());
        assertNull(agent.getFirstName());
        assertNull(agent.getLastName());
    }
    
    @Test
    void testCompanyAgentToStringWithNullValues() {
        CompanyAgent agent = new CompanyAgent();
        agent.setAgentId(999);
        
        String result = agent.toString();
        assertNotNull(result);
        assertTrue(result.contains("ID: 999"));
    }
}
