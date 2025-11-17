package model;

import java.time.LocalDate;
import java.util.Scanner;

public class Claim {
    private int claimId;
    private int contractNumber;
    private int customerId;
    private float amount;
    private String description;
    private LocalDate submissionDate;
    private String status;
    
    public Claim() {}
    
    public Claim(int claimId, int contractNumber, int customerId, float amount, 
                 String description, LocalDate submissionDate, String status) {
        this.claimId = claimId;
        this.contractNumber = contractNumber;
        this.customerId = customerId;
        this.amount = amount;
        this.description = description;
        this.submissionDate = submissionDate;
        this.status = status;
    }
    public int getClaimId() { return claimId; }
    public void setClaimId(int claimId) { this.claimId = claimId; }
    public int getContractNumber() { return contractNumber; }
    public void setContractNumber(int contractNumber) { this.contractNumber = contractNumber; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; } 
    public LocalDate getSubmissionDate() { return submissionDate; }
    public void setSubmissionDate(LocalDate submissionDate) { this.submissionDate = submissionDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public static Claim input(Scanner scanner, int contractNumber, int customerId , int claimId) {
        System.out.println("=== Введення даних вимоги ===");
        
        System.out.print("Сума вимоги: ");
        float amount = scanner.nextFloat();
        scanner.nextLine();
        
        System.out.print("Опис вимоги: ");
        String description = scanner.nextLine();
        return new Claim(claimId, contractNumber, customerId, amount, 
                        description, LocalDate.now(), "На розгляді");
    }

    @Override
    public String toString() {
        return "Вимога #" + claimId + " - Контракт: " + contractNumber + " - Сума: " + amount;
    }
}