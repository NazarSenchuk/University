package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import service.ContractProcessor;



public class Contract {
    public enum RiskLevel {
    LOW, MEDIUM, HIGH , UNDEFINED
}


    private int contractNumber;
    private int customerId;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
    private RiskLevel riskLevel;
    private int derivativeId;
    public Contract() {}
    public Contract(int contractNumber, int customerId, LocalDate startDate, LocalDate endDate, 
                   boolean isActive, RiskLevel riskLevel , int derivativeId) {
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
    public RiskLevel getRiskLevel() { return riskLevel; }
    public void setRiskLevel(RiskLevel riskLevel) { this.riskLevel = riskLevel; }














    public static Contract input(int  contractNumber, Scanner scanner, int customerId, int derivativeId) {
        System.out.println("=== Введення даних контракту ===");

        LocalDate startDate = yearInput("Дата початку: ", scanner);

        LocalDate endDate = yearInput("Дата завершення: ", scanner);
        
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("Дата завершення не може бути раніше дати початку");
        }
        
        
        
        
        return new Contract(contractNumber, customerId, startDate, endDate, true,RiskLevel.UNDEFINED, derivativeId);
    }
    public static LocalDate yearInput(String whatToIn, Scanner scanner) {
        while(true){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try{
                System.out.println("Введіть " +  whatToIn+"(формат дд.мм.рррр)"+":");

                String input = scanner.nextLine().trim();

                LocalDate date = LocalDate.parse(input, formatter);
                return date;
            }
            catch (DateTimeParseException e) {

                System.out.println(" Помилка! Введіть повну дату, наприклад: 25.10.2023");
            }
        }
    }
    @Override
    public String toString() {
        return "Контракт #" + contractNumber + " (Ризик: " + riskLevel + ", Активний: " + isActive + ")";
    }
}
