package service;

import model.Contract;
import model.Customer;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ContractService {
    private final Scanner scanner;
    private int nextContractNumber = 1000;
    
    public ContractService(Scanner scanner) {
        this.scanner = scanner;
    }
    
    public void createContractInteractive(List<Customer> customers, List<Contract> contracts, ContractProcessor contractProcessor) {
        try {
            System.out.println("=== Створення контракту ===");
            
            // Вибір клієнта
            System.out.print("Введіть ID клієнта: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();
            
            // Перевірити чи клієнт існує
            Customer selectedCustomer = findCustomerById(customers, customerId);
            if (selectedCustomer == null) {
                System.out.println("Клієнт з ID " + customerId + " не знайдений!");
                return;
            }
            
            System.out.println("Обраний клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            
            // Створення контракту
            Contract contract = new Contract();
            contract.setContractNumber(++nextContractNumber);
            contract.setCustomerId(customerId);
            contract.setStartDate(LocalDate.now());
            
            System.out.print("Введіть термін дії контракту (у місяцях): ");
            int months = scanner.nextInt();
            scanner.nextLine();
            
            if (months <= 0) {
                System.out.println("Термін дії має бути більше 0 місяців!");
                return;
            }
            
            contract.setEndDate(LocalDate.now().plusMonths(months));
            
            // Додаткові параметри контракту
            System.out.print("Введіть тип страхування: ");
            String insuranceType = scanner.nextLine();
            
            System.out.print("Введіть суму страхування: ");
            double insuranceAmount = scanner.nextDouble();
            scanner.nextLine();
            
            // Розрахунок ризику
            contractProcessor.calculateRisk(contract);
            contract.setActive(true);
            
            // Збереження контракту
            contracts.add(contract);
            
            System.out.println("=== Контракт успішно створено! ===");
            System.out.println("Номер контракту: " + contract.getContractNumber());
            System.out.println("Клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            System.out.println("Період дії: з " + contract.getStartDate() + " по " + contract.getEndDate());
            System.out.println("Рівень ризику: " + contract.getRiskLevel());
            System.out.println("Тип страхування: " + insuranceType);
            System.out.println("Сума страхування: " + insuranceAmount);
            
        } catch (Exception e) {
            System.out.println("Помилка при створенні контракту: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void findContract(List<Customer> customers, List<Contract> contracts) {
        try {
            System.out.println("=== Пошук контракту ===");
            System.out.println("Оберіть критерій пошуку:");
            System.out.println("1 - За номером контракту");
            System.out.println("2 - За ID клієнта");
            System.out.println("3 - За рівнем ризику");
            System.out.print("Ваш вибір: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            List<Contract> results;
            
            switch (choice) {
                case 1:
                    System.out.print("Введіть номер контракту: ");
                    int contractNumber = scanner.nextInt();
                    scanner.nextLine();
                    results = contracts.stream()
                        .filter(c -> c.getContractNumber() == contractNumber)
                        .toList();
                    break;
                    
                case 2:
                    System.out.print("Введіть ID клієнта: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine();
                    results = contracts.stream()
                        .filter(c -> c.getCustomerId() == customerId)
                        .toList();
                    break;
                    
                case 3:
                    System.out.print("Введіть рівень ризику (LOW/MEDIUM/HIGH): ");
                    String riskLevel = scanner.nextLine();
                    results = contracts.stream()
                        .filter(c -> c.getRiskLevel().equalsIgnoreCase(riskLevel))
                        .toList();
                    break;
                    
                default:
                    System.out.println("Невірний вибір!");
                    return;
            }
            
            displayContractResults(results, customers);
            
        } catch (Exception e) {
            System.out.println("Помилка при пошуку: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void reviewContract(List<Customer> customers, List<Contract> contracts) {
        try {
            System.out.println("=== Перегляд та оновлення контракту ===");
            
            System.out.print("Введіть номер контракту: ");
            int contractNumber = scanner.nextInt();
            scanner.nextLine();
            
            Contract contract = contracts.stream()
                .filter(c -> c.getContractNumber() == contractNumber)
                .findFirst()
                .orElse(null);
            
            if (contract == null) {
                System.out.println("Контракт не знайдений!");
                return;
            }
            
            displayContractDetails(contract, customers);
            
            System.out.println("\nОберіть дію:");
            System.out.println("1 - Схвалити контракт");
            System.out.println("2 - Відхилити контракт");
            System.out.println("3 - Змінити рівень ризику");
            System.out.println("4 - Продовжити термін дії");
            System.out.print("Ваш вибір: ");
            
            int action = scanner.nextInt();
            scanner.nextLine();
            
            switch (action) {
                case 1:
                    contract.setActive(true);
                    System.out.println("Контракт " + contractNumber + " схвалено");
                    break;
                    
                case 2:
                    contract.setActive(false);
                    System.out.println("Контракт " + contractNumber + " відхилено");
                    break;
                    
                case 3:
                    System.out.print("Введіть новий рівень ризику (LOW/MEDIUM/HIGH): ");
                    String newRisk = scanner.nextLine();
                    contract.setRiskLevel(newRisk.toUpperCase());
                    System.out.println("Рівень ризику оновлено");
                    break;
                    
                case 4:
                    System.out.print("Введіть кількість місяців для продовження: ");
                    int extraMonths = scanner.nextInt();
                    scanner.nextLine();
                    contract.setEndDate(contract.getEndDate().plusMonths(extraMonths));
                    System.out.println("Термін дії продовжено");
                    break;
                    
                default:
                    System.out.println("Невірна дія!");
            }
            
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void viewCustomerContracts(List<Customer> customers, List<Contract> contracts) {
        try {
            System.out.println("=== Перегляд контрактів клієнта ===");
            
            System.out.print("Введіть ID клієнта: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();
            
            List<Contract> customerContracts = contracts.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .toList();
                
            if (customerContracts.isEmpty()) {
                System.out.println("Контракти не знайдені для клієнта з ID " + customerId);
                return;
            }
            
            Customer customer = findCustomerById(customers, customerId);
            System.out.println("Контракти клієнта: " + 
                (customer != null ? customer.getFirstName() + " " + customer.getLastName() : "ID " + customerId));
            
            displayContractResults(customerContracts, customers);
            
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void viewAllContracts(List<Contract> contracts, List<Customer> customers) {
        System.out.println("=== Всі контракти ===");
        System.out.println("Загальна кількість контрактів: " + contracts.size());
        
        if (contracts.isEmpty()) {
            System.out.println("Контрактів немає.");
            return;
        }
        
        for (Contract contract : contracts) {
            displayContractDetails(contract, customers);
            System.out.println("---");
        }
    }
    
    public void showContractsStatistics(List<Contract> contracts) {
        System.out.println("=== Статистика контрактів ===");
        System.out.println("Загальна кількість: " + contracts.size());
        
        long activeCount = contracts.stream().filter(Contract::isActive).count();
        System.out.println("Активних: " + activeCount);
        System.out.println("Неактивних: " + (contracts.size() - activeCount));
        
        // Статистика за ризиком
        long lowRisk = contracts.stream().filter(c -> "LOW".equals(c.getRiskLevel())).count();
        long mediumRisk = contracts.stream().filter(c -> "MEDIUM".equals(c.getRiskLevel())).count();
        long highRisk = contracts.stream().filter(c -> "HIGH".equals(c.getRiskLevel())).count();
        
        System.out.println("Ризик LOW: " + lowRisk);
        System.out.println("Ризик MEDIUM: " + mediumRisk);
        System.out.println("Ризик HIGH: " + highRisk);
    }
    
    private void displayContractResults(List<Contract> contracts, List<Customer> customers) {
        if (contracts.isEmpty()) {
            System.out.println("Контракти не знайдені.");
            return;
        }
        
        System.out.println("Знайдено контрактів: " + contracts.size());
        for (Contract contract : contracts) {
            displayContractDetails(contract, customers);
        }
    }
    
    private void displayContractDetails(Contract contract, List<Customer> customers) {
        Customer customer = findCustomerById(customers, contract.getCustomerId());
        String customerName = customer != null ? 
            customer.getFirstName() + " " + customer.getLastName() : "Невідомий";
            
        System.out.printf("Контракт #%d%n", contract.getContractNumber());
        System.out.printf("Клієнт: %s (ID: %d)%n", customerName, contract.getCustomerId());
        System.out.printf("Період: з %s по %s%n", contract.getStartDate(), contract.getEndDate());
        System.out.printf("Ризик: %s%n", contract.getRiskLevel());
        System.out.printf("Статус: %s%n", contract.isActive() ? "Активний" : "Неактивний");
        System.out.printf("Вартість: %.2f%n", contract.calculateValue());
    }
    
    private Customer findCustomerById(List<Customer> customers, int customerId) {
        return customers.stream()
            .filter(c -> c.getId() == customerId)
            .findFirst()
            .orElse(null);
    }

    public void registerCustomer(List<Customer> customers) {
        Customer newCustomer = Customer.input(scanner);
        customers.add(newCustomer);
        System.out.println("Клієнта успішно зареєстровано! ID: " + newCustomer.getId());
    }
}