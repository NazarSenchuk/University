package model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Derivative {
    private List<Contract> contracts;
    
    public Derivative() {
        this.contracts = new ArrayList<>();
    }
    
    public void addContract(Contract contract) {
        contracts.add(contract);
    }
    
    public void removeContract(Contract contract) {
        contracts.remove(contract);
    }
    
    public void sortByRisk() {
        contracts.sort(Comparator.comparing(Contract::getRiskLevel));
        System.out.println("Контракти відсортовано за рівнем ризику");
    }
    
    public List<Contract> searchContract(String criteria) {
        List<Contract> result = new ArrayList<>();
        for (Contract contract : contracts) {
            if (String.valueOf(contract.getContractNumber()).contains(criteria) ||
                contract.getRiskLevel().toLowerCase().contains(criteria.toLowerCase())) {
                result.add(contract);
            }
        }
        return result;
    }
    
    public void calculateTotalValue() {
        float totalValue = 0;
        for (Contract contract : contracts) {
            totalValue += contract.calculateValue();
        }
        System.out.println("Загальна вартість деривативу: " + totalValue);
    }
    
    public List<Contract> getContracts() { 
        return new ArrayList<>(contracts); 
    }
}