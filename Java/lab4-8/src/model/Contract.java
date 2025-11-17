package model;

import java.time.LocalDate;
import java.util.Scanner;

import service.ContractProcessor;
public class Contract {
    private int contractNumber;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private String riskLevel;
    private int derivativeId;
    public Contract() {}
    public Contract(int contractNumber, int customerId, LocalDate startDate, LocalDate endDate, 
                   boolean isActive, String riskLevel , int derivativeId) {
        this.contractNumber = contractNumber;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.riskLevel = riskLevel;
        this.derivativeId = derivativeId;
    }
    

    public int getContractNumber() { return contractNumber; }
    public void setContractNumber(int contractNumber) { this.contractNumber = contractNumber; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getDerivativeId() { return derivativeId; }
    public void setDerivativeId(int derivativeId) { this.derivativeId =derivativeId; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    public static Contract input(int  contractNumber, Scanner scanner, int customerId, int derivativeId) {
        System.out.println("=== Введення даних контракту ===");
        System.out.print("Дата початку: ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Дата завершення: ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата завершення не може бути раніше дати початку");
        }
        
        
        
        
        return new Contract(contractNumber, customerId, startDate, endDate, true,"", derivativeId);
    }

    @Override
    public String toString() {
        return "Контракт #" + contractNumber + " (Ризик: " + riskLevel + ", Активний: " + isActive + ")";
    }
}
