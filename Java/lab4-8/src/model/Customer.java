package model;

import java.time.LocalDate;
import java.util.Scanner;

public class Customer {
    private int id;
    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    
    public Customer() {}
    
    public Customer(int id, String firstName, String lastName, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }
    
    // Гетери та сетери
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public static Customer input(Scanner scanner) {
        System.out.println("=== Введення даних клієнта ===");
        
        System.out.print("Ім'я: ");
        String firstName = scanner.nextLine();
        
        System.out.print("Прізвище: ");
        String lastName = scanner.nextLine();
        
        System.out.print("Телефон: ");
        String phone = scanner.nextLine();
        
        System.out.print("Email: ");
        String email = scanner.nextLine();
        
        return new Customer(0, firstName, lastName, phone, email);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " (ID: " + id + ")";
    }
}