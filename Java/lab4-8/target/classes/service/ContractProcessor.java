package service;

import model.Contract;
import model.Derivative;
import static model.Contract.RiskLevel.*;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContractProcessor {
    private static final Logger logger = LogManager.getLogger(ContractProcessor.class);
    
    public static boolean validateContract(Contract contract) {
        boolean isValid = contract.getStartDate() != null && 
                         contract.getEndDate() != null &&
                         contract.getEndDate().isAfter(contract.getStartDate());
        logger.debug("Валідація контракту {}: {}", contract.getContractNumber(), isValid ? "успішно" : "не пройдено");
        return isValid;
    }
    
    public static Contract.RiskLevel calculateRisk(Contract contract) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(contract.getStartDate(), contract.getEndDate());
        
        Contract.RiskLevel riskLevel;
        if (days > 365) riskLevel = Contract.RiskLevel.HIGH;
        else if (days > 180) riskLevel = Contract.RiskLevel.MEDIUM;
        else riskLevel = Contract.RiskLevel.LOW;
        
        contract.setRiskLevel(riskLevel);
        logger.debug("Розраховано ризик для контракту {}: {}", contract.getContractNumber(), riskLevel);
        return riskLevel;
    }
    
    public static float calculateValue(Contract contract) {
        long days = java.time.temporal.ChronoUnit.DAYS.between(contract.getStartDate(), contract.getEndDate());
        float baseValue = days * 10.0f;
        float riskMultiplier = switch(contract.getRiskLevel()) {
            case HIGH -> 1.5f;
            case MEDIUM -> 1.2f;
            case LOW -> 1.0f;
            default -> 1.0f;
        };
        
        return baseValue * riskMultiplier;
    }

    public static float calculateTotalValue(Derivative derivative) {
        float sum = 0;
        for (Contract contract : derivative.getContracts()) {
            sum += calculateValue(contract);
        }
        logger.debug("Розраховано загальну вартість деривативу {}: {}", derivative.getDerivativeId(), sum);
        return sum;
    }
}