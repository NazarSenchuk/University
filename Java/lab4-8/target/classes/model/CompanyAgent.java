package model;

public class CompanyAgent {
    private int agentId;
    private String firstName;
    private String lastName;
    
    public CompanyAgent() {}
    
    public CompanyAgent(int agentId, String firstName, String lastName) {
        this.agentId = agentId;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + agentId + ")";
    }
}