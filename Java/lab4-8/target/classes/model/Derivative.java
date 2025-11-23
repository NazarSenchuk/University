package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Derivative {
    private int id;
    private List<Contract> contracts;
    private  int customer_id  ;
    public Derivative() {
        this.contracts = new ArrayList<>();
    }
    public void addContract(Contract contract) {
        contracts.add(contract);
    }
    public int getDerivativeId() { return id; }
    public void setDerivativeId(int derivativeId) { this.id  = derivativeId; }
    public int getCustomerId() { return customer_id; }
    public void setCustomerId(int customerId) { this.customer_id  = customerId; }
    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }
    public void sortByRisk() {
        contracts.sort(Comparator.comparing(Contract::getRiskLevel));
        System.out.println("Контракти відсортовано за рівнем ризику");
    }
    public List<Contract> searchContract(String criteria) {
    try {
    
        int contractNumber = Integer.parseInt(criteria);
        return contracts.stream()
            .filter(contract -> contract.getContractNumber() == contractNumber)
            .toList();
    } catch (NumberFormatException e) {
        return contracts.stream()
            .filter(contract -> contract.getRiskLevel().name().equalsIgnoreCase(criteria))
            .toList();
    }
}
    
    
    public List<Contract> getContracts() { 
        return new ArrayList<>(contracts); 
    }
}