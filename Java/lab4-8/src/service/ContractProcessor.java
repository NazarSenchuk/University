package service;

import model.Contract;

public class ContractProcessor {
    
    public boolean validateContract(Contract contract) {
        boolean isValid = contract.getStartDate() != null && 
                         contract.getEndDate() != null &&
                         contract.getEndDate().isAfter(contract.getStartDate());
        System.out.println("Валідація контракту " + contract.getContractNumber() + ": " + (isValid ? "пройдено" : "не пройдено"));
        return isValid;
    }
    
    public String calculateRisk(Contract contract) {
        // Спрощена логіка розрахунку ризику
        long days = java.time.temporal.ChronoUnit.DAYS.between(contract.getStartDate(), contract.getEndDate());
        String riskLevel = days > 365 ? "HIGH" : (days > 180 ? "MEDIUM" : "LOW");
        contract.setRiskLevel(riskLevel);
        return riskLevel;
    }
    
    public float calculateValue(Contract contract) {
        return contract.calculateValue();
    }
}
