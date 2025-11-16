package model;

import java.time.LocalDate;
import java.util.Scanner;
public class Contract {
    private int contractNumber;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private String riskLevel;
    
    public Contract() {}
    
    public Contract(int contractNumber, int customerId, LocalDate startDate, LocalDate endDate, 
                   boolean isActive, String riskLevel) {
        this.contractNumber = contractNumber;
        this.customerId = customerId;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isActive = isActive;
        this.riskLevel = riskLevel;
    }
    
    public float calculateValue() {
        // Логіка розрахунку вартості контракту
        long days = java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate);
        float baseValue = days * 10.0f;
        
        // Коригування за ризиком
        float riskMultiplier = switch(riskLevel) {
            case "HIGH" -> 1.5f;
            case "MEDIUM" -> 1.2f;
            case "LOW" -> 1.0f;
            default -> 1.0f;
        };
        
        return baseValue * riskMultiplier;
    }
    
    // Гетери та сетери
    public int getContractNumber() { return contractNumber; }
    public void setContractNumber(int contractNumber) { this.contractNumber = contractNumber; }
    
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getRiskLevel() { return riskLevel; }
    public void setRiskLevel(String riskLevel) { this.riskLevel = riskLevel; }
    
    public static Contract input(Scanner scanner, int customerId) {
        System.out.println("=== Введення даних контракту ===");
        
        System.out.print("Тип контракту: ");
        String type = scanner.nextLine();
        
        System.out.print("Дата початку (yyyy-mm-dd): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        
        System.out.print("Дата завершення (yyyy-mm-dd): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());
        
        // Валідація дат
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата завершення не може бути раніше дати початку");
        }
        
        int contractNumber = (int) (Math.random() * 10000);
        
        return new Contract(contractNumber, customerId, startDate, endDate, true, "MEDIUM");
    }

    
    
    @Override
    public String toString() {
        return "Контракт #" + contractNumber + " (Ризик: " + riskLevel + ", Активний: " + isActive + ")";
    }
}
