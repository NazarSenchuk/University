package service;

import model.Contract;
import model.Customer;
import model.Derivative;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ContractService {
    private final Scanner scanner;
    private int nextContractId = 1;
    private int nextDerivativeId = 1;
    public ContractService(Scanner scanner) {
        this.scanner = scanner;
    }

    // Замініть старий метод createContractInteractive на цей:
    public void createContractInteractive(List<Customer> customers, List<Contract> contracts, ContractProcessor contractProcessor, List<Derivative> derivatives) {
        try {
            System.out.println("=== Створення контракту ===");

            System.out.print("Введіть ID клієнта: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();
            Customer selectedCustomer = findCustomerById(customers, customerId);
            if (selectedCustomer == null) {
                System.out.println("Клієнт з ID " + customerId + " не знайдений!");
                return;
            }

            Derivative derivative = findDerivativeByCustomerId(derivatives, customerId);

            // Логіка створення деривативу + ЗБЕРЕЖЕННЯ
            if (derivative == null) {
                derivative = new Derivative();
                // Генеруємо ID: беремо максимальний існуючий + 1
                int nextDerivId = derivatives.stream().mapToInt(Derivative::getDerivativeId).max().orElse(0) + 1;

                derivative.setDerivativeId(nextDerivId);
                derivative.setCustomerId(customerId);
                derivatives.add(derivative);

                FileService.saveDerivatives(derivatives); // <--- ЗБЕРЕЖЕННЯ
                System.out.println("Створено новий дериватив для клієнта");
            }

            System.out.println("Обраний клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());

            // Логіка створення контракту + ЗБЕРЕЖЕННЯ
            int nextContractId = contracts.stream().mapToInt(Contract::getContractNumber).max().orElse(0) + 1;

            Contract contract = Contract.input(nextContractId, scanner, customerId, derivative.getDerivativeId());
            ContractProcessor.calculateRisk(contract);
            derivative.addContract(contract);
            contracts.add(contract);

            FileService.saveContracts(contracts); // <--- ЗБЕРЕЖЕННЯ

            System.out.println("=== Контракт успішно створено! ===");
            System.out.println("Номер контракту: " + contract.getContractNumber());
            System.out.println("Клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            System.out.println("Дериватив ID: " + derivative.getDerivativeId());
            System.out.println("Період дії: з " + contract.getStartDate() + " по " + contract.getEndDate());
            System.out.println("Рівень ризику: " + contract.getRiskLevel());

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
        long lowRisk = contracts.stream().filter(c -> "LOW".equals(c.getRiskLevel())).count();
        long mediumRisk = contracts.stream().filter(c -> "MEDIUM".equals(c.getRiskLevel())).count();
        long highRisk = contracts.stream().filter(c -> "HIGH".equals(c.getRiskLevel())).count();
        
        System.out.println("Ризик LOW: " + lowRisk);
        System.out.println("Ризик MEDIUM: " + mediumRisk);
        System.out.println("Ризик HIGH: " + highRisk);
    }
    
    // Derivative methods
    public void displayAllDerivatives(List<Derivative> derivatives, List<Customer> customers) {
        System.out.println("=== Всі деривативи ===");
        if (derivatives.isEmpty()) {
            System.out.println("Деривативів не знайдено.");
            return;
        }
        
        for (Derivative derivative : derivatives) {
            Customer customer = findCustomerById(customers, derivative.getCustomerId());
            System.out.println("Дериватив ID: " + derivative.getDerivativeId() + 
                " | Клієнт: " + (customer != null ? 
                    customer.getFirstName() + " " + customer.getLastName() : "Не знайдено") +
                " | Контрактів: " + derivative.getContracts().size());
        }
    }
    
    public void findDerivativeByCustomer(List<Derivative> derivatives, List<Customer> customers) {
        System.out.print("Введіть ID клієнта: ");
        int customerId = scanner.nextInt();
        
        Derivative derivative = findDerivativeByCustomerId(derivatives, customerId);
            
        if (derivative == null) {
            System.out.println("Дериватив для клієнта з ID " + customerId + " не знайдено.");
            return;
        }
        
        Customer customer = findCustomerById(customers, customerId);
        System.out.println("Знайдено дериватив:");
        System.out.println("ID: " + derivative.getDerivativeId());
        System.out.println("Клієнт: " + (customer != null ? 
            customer.getFirstName() + " " + customer.getLastName() : "Не знайдено"));
        System.out.println("Кількість контрактів: " + derivative.getContracts().size());
    }
    
    public void sortDerivativeContractsByRisk(List<Derivative> derivatives) {
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        derivative.sortByRisk();
        System.out.println("Контракти деривативу відсортовано за рівнем ризику.");
    }
    
    public void searchContractsInDerivative(List<Derivative> derivatives) {
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        scanner.nextLine();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        System.out.print("Введіть критерій пошуку: ");
        String criteria = scanner.nextLine();
        
        List<Contract> results = derivative.searchContract(criteria);
        if (results.isEmpty()) {
            System.out.println("Контракти не знайдені.");
        } else {
            System.out.println("Знайдені контракти:");
            results.forEach(contract -> 
                System.out.println("Контракт #" + contract.getContractNumber() + 
                    " | Ризик: " + contract.getRiskLevel()));
        }
    }
    
    public void calculateDerivativeValue(List<Derivative> derivatives) {
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        float totalValue = ContractProcessor.calculateTotalValue(derivative);
        System.out.println("Загальна вартість деривативу: " + totalValue);
    }
    
    public void viewDerivativeContracts(List<Derivative> derivatives, List<Customer> customers) {
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        System.out.println("=== Контракти деривативу " + derivativeId + " ===");
        List<Contract> contracts = derivative.getContracts();
        if (contracts.isEmpty()) {
            System.out.println("Контрактів не знайдено.");
            return;
        }
        
        for (Contract contract : contracts) {
            displayContractDetails(contract, customers);
            System.out.println("---");
        }
    }
    private void displayContractResults(List<Contract> contracts, List<Customer> customers) {
        if (contracts.isEmpty()) {
            System.out.println("Контракти не знайдені.");
            return;
        }
        
        System.out.prdwaintln("Знайдено контрактів: " + contracts.size());
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
        System.out.println("Дериватив ID: " + contract.getDerivativeId());
        System.out.printf("Статус: %s%n", contract.isActive() ? "Активний" : "Неактивний");
        System.out.printf("Вартість: %.2f%n", ContractProcessor.calculateValue(contract));
    }
    
    private Derivative findDerivativeByCustomerId(List<Derivative> derivatives, int customerId) {
        return derivatives.stream()
            .filter(d -> d.getCustomerId() == customerId)
            .findFirst()
            .orElse(null);
    }
    
    private Derivative findDerivativeById(List<Derivative> derivatives, int derivativeId) {
        return derivatives.stream()
            .filter(d -> d.getDerivativeId() == derivativeId)
            .findFirst()
            .orElse(null);
    }
    
    private Customer findCustomerById(List<Customer> customers, int customerId) {
        return customers.stream()
            .filter(c -> c.getId() == customerId) 
            .findFirst()
            .orElse(null);
    }

    // Замініть старий метод registerCustomer на цей:
    public void registerCustomer(List<Customer> customers) {
        Customer newCustomer = Customer.input(scanner);

        // Генеруємо ID
        int nextCustomerId = customers.stream().mapToInt(Customer::getId).max().orElse(0) + 1;
        newCustomer.setId(nextCustomerId);

        customers.add(newCustomer);

        FileService.saveCustomers(customers); // <--- ЗБЕРЕЖЕННЯ

        System.out.println("Клієнта успішно зареєстровано! ID: " + newCustomer.getId());
    }
}