package service;

import model.Contract;
import model.Derivative;

public class ContractProcessor {
    
    public static boolean validateContract(Contract contract) {
        boolean isValid = contract.getStartDate() != null && 
                         contract.getEndDate() != null &&
                         contract.getEndDate().isAfter(contract.getStartDate());
        System.out.println("Валідація контракту " + contract.getContractNumber() + ": " + (isValid ? "пройдено" : "не пройдено"));
        return isValid;
    }
    
    public static String calculateRisk(Contract contract) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(contract.getStartDate(), contract.getEndDate());
        
        String riskLevel;
        if (days > 365) riskLevel = "HIGH"; 
        else if (days > 180) riskLevel = "MEDIUM";
        else riskLevel = "LOW";
        contract.setRiskLevel(riskLevel);
        return riskLevel;
    }
    
    public static float calculateValue(Contract contract) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(contract.getStartDate(), contract.getEndDate());
        float baseValue = days * 10.0f;
        float riskMultiplier = switch(contract.getRiskLevel()) {
            case "HIGH" -> 1.5f;
            case "MEDIUM" -> 1.2f;
            case "LOW" -> 1.0f;
            default -> 1.0f;
        };
        
        return baseValue * riskMultiplier;
    }

    public static float calculateTotalValue(Derivative derivative) {
        float sum = 0;
        for (Contract contract : derivative.getContracts()) {
            sum += calculateValue(contract);
        }
        return sum;
    }
}