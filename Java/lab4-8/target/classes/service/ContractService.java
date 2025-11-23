package service;

import model.Contract;
import model.Customer;
import model.Derivative;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContractService {
    private final Scanner scanner;
    private int nextContractId = 1;
    private int nextDerivativeId = 1;
    private static final Logger logger = LogManager.getLogger(ContractService.class);
    
    public ContractService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void createContractInteractive(List<Customer> customers, List<Contract> contracts, ContractProcessor contractProcessor, List<Derivative> derivatives) {
        try {
            logger.info("Початок процесу створення контракту");
            System.out.println("=== Створення контракту ===");

            System.out.print("Введіть ID клієнта: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();
            
            Customer selectedCustomer = findCustomerById(customers, customerId);
            if (selectedCustomer == null) {
                logger.warn("Клієнт не знайдений з ID: {}", customerId);
                System.out.println("Клієнт з ID " + customerId + " не знайдений!");
                return;
            }

            logger.debug("Знайдено клієнта: {} {}", selectedCustomer.getFirstName(), selectedCustomer.getLastName());

            Derivative derivative = findDerivativeByCustomerId(derivatives, customerId);

            if (derivative == null) {
                derivative = new Derivative();
                int nextDerivId = derivatives.stream().mapToInt(Derivative::getDerivativeId).max().orElse(0) + 1;

                derivative.setDerivativeId(nextDerivId);
                derivative.setCustomerId(customerId);
                derivatives.add(derivative);

                FileService.saveDerivatives(derivatives);
                logger.info("Створено новий дериватив для клієнта ID: {}", customerId);
                System.out.println("Створено новий дериватив для клієнта");
            }

            System.out.println("Обраний клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());

            int nextContractId = contracts.stream().mapToInt(Contract::getContractNumber).max().orElse(0) + 1;

            Contract contract = Contract.input(nextContractId, scanner, customerId, derivative.getDerivativeId());
            ContractProcessor.calculateRisk(contract);
            derivative.addContract(contract);
            contracts.add(contract);

            FileService.saveContracts(contracts);

            logger.info("Контракт успішно створено - Номер: {}, Клієнт: {}", contract.getContractNumber(), customerId);
            System.out.println("=== Контракт успішно створено! ===");
            System.out.println("Номер контракту: " + contract.getContractNumber());
            System.out.println("Клієнт: " + selectedCustomer.getFirstName() + " " + selectedCustomer.getLastName());
            System.out.println("Дериватив ID: " + derivative.getDerivativeId());
            System.out.println("Період дії: з " + contract.getStartDate() + " по " + contract.getEndDate());
            System.out.println("Рівень ризику: " + contract.getRiskLevel());

        } catch (Exception e) {
            logger.error("Помилка при створенні контракту", e);
            System.out.println("Помилка при створенні контракту: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void findContract(List<Customer> customers, List<Contract> contracts) {
        try {
            logger.debug("Початок пошуку контракту");
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
                    logger.debug("Пошук контракту за номером: {}, знайдено: {}", contractNumber, results.size());
                    break;
                    
                case 2:
                    System.out.print("Введіть ID клієнта: ");
                    int customerId = scanner.nextInt();
                    scanner.nextLine();
                    results = contracts.stream()
                        .filter(c -> c.getCustomerId() == customerId)
                        .toList();
                    logger.debug("Пошук контрактів за клієнтом: {}, знайдено: {}", customerId, results.size());
                    break;
                    
                case 3:
                    System.out.print("Введіть рівень ризику (LOW/MEDIUM/HIGH): ");
                    String riskLevelInput = scanner.nextLine();
                    try {
                        Contract.RiskLevel riskLevel = Contract.RiskLevel.valueOf(riskLevelInput.toUpperCase());
                        results = contracts.stream()
                            .filter(c -> c.getRiskLevel() == riskLevel)
                            .toList();
                        logger.debug("Пошук контрактів за ризиком: {}, знайдено: {}", riskLevel, results.size());
                    } catch (IllegalArgumentException e) {
                        logger.warn("Невірний рівень ризику введено: {}", riskLevelInput);
                        System.out.println("Невірний рівень ризику!");
                        results = List.of();
                    }
                    break;
                    
                default:
                    logger.warn("Невірний вибір при пошуку контракту: {}", choice);
                    System.out.println("Невірний вибір!");
                    return;
            }
            
            displayContractResults(results, customers);
            
        } catch (Exception e) {
            logger.error("Помилка при пошуку контракту", e);
            System.out.println("Помилка при пошуку: " + e.getMessage());
            scanner.nextLine();
        }
    }

    public void registerCustomer(List<Customer> customers) {
        Customer newCustomer = Customer.input(scanner);

        int nextCustomerId = customers.stream().mapToInt(Customer::getId).max().orElse(0) + 1;
        newCustomer.setId(nextCustomerId);

        customers.add(newCustomer);

        FileService.saveCustomers(customers);

        logger.info("Клієнта успішно зареєстровано - ID: {}", newCustomer.getId());
        System.out.println("Клієнта успішно зареєстровано! ID: " + newCustomer.getId());
    }
    
    private Derivative findDerivativeByCustomerId(List<Derivative> derivatives, int customerId) {
        return derivatives.stream()
            .filter(d -> d.getCustomerId() == customerId)
            .findFirst()
            .orElse(null);
    }
    
    private Customer findCustomerById(List<Customer> customers, int customerId) {
        return customers.stream()
            .filter(c -> c.getId() == customerId) 
            .findFirst()
            .orElse(null);
    }
     public void viewCustomerContracts(List<Customer> customers, List<Contract> contracts) {
        try {
            logger.debug("Початок перегляду контрактів клієнта");
            System.out.println("=== Перегляд контрактів клієнта ===");
            
            System.out.print("Введіть ID клієнта: ");
            int customerId = scanner.nextInt();
            scanner.nextLine();
            
            List<Contract> customerContracts = contracts.stream()
                .filter(c -> c.getCustomerId() == customerId)
                .toList();
                
            if (customerContracts.isEmpty()) {
                logger.warn("Контракти не знайдені для клієнта з ID: {}", customerId);
                System.out.println("Контракти не знайдені для клієнта з ID " + customerId);
                return;
            }
            
            Customer customer = findCustomerById(customers, customerId);
            System.out.println("Контракти клієнта: " + 
                (customer != null ? customer.getFirstName() + " " + customer.getLastName() : "ID " + customerId));
            
            displayContractResults(customerContracts, customers);
            
        } catch (Exception e) {
            logger.error("Помилка при перегляді контрактів клієнта", e);
            System.out.println("Помилка: " + e.getMessage());
            scanner.nextLine();
        }
    }
    
    public void viewAllContracts(List<Contract> contracts, List<Customer> customers) {
        logger.debug("Перегляд всіх контрактів");
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
        logger.debug("Показ статистики контрактів");
        System.out.println("=== Статистика контрактів ===");
        System.out.println("Загальна кількість: " + contracts.size());
        
        long activeCount = contracts.stream().filter(Contract::isActive).count();
        System.out.println("Активних: " + activeCount);
        System.out.println("Неактивних: " + (contracts.size() - activeCount));
        
        long lowRisk = contracts.stream().filter(c -> Contract.RiskLevel.LOW.equals(c.getRiskLevel())).count();
        long mediumRisk = contracts.stream().filter(c -> Contract.RiskLevel.MEDIUM.equals(c.getRiskLevel())).count();
        long highRisk = contracts.stream().filter(c -> Contract.RiskLevel.HIGH.equals(c.getRiskLevel())).count();
        
        System.out.println("Ризик LOW: " + lowRisk);
        System.out.println("Ризик MEDIUM: " + mediumRisk);
        System.out.println("Ризик HIGH: " + highRisk);
    }

    public void displayAllDerivatives(List<Derivative> derivatives, List<Customer> customers) {
        logger.debug("Показ всіх деривативів");
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
        logger.debug("Пошук деривативу за клієнтом");
        System.out.print("Введіть ID клієнта: ");
        int customerId = scanner.nextInt();
        
        Derivative derivative = findDerivativeByCustomerId(derivatives, customerId);
            
        if (derivative == null) {
            logger.warn("Дериватив не знайдений для клієнта з ID: {}", customerId);
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
        logger.debug("Сортування контрактів деривативу за ризиком");
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            logger.warn("Дериватив не знайдений з ID: {}", derivativeId);
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        derivative.sortByRisk();
        logger.info("Контракти деривативу {} відсортовано за ризиком", derivativeId);
    }
    
    public void searchContractsInDerivative(List<Derivative> derivatives) {
        logger.debug("Пошук контрактів у деривативі");
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        scanner.nextLine();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            logger.warn("Дериватив не знайдений з ID: {}", derivativeId);
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        System.out.print("Введіть рівень ризику: ");
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
        logger.debug("Розрахунок вартості деривативу");
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            logger.warn("Дериватив не знайдений з ID: {}", derivativeId);
            System.out.println("Дериватив з ID " + derivativeId + " не знайдено.");
            return;
        }
        
        float totalValue = ContractProcessor.calculateTotalValue(derivative);
        System.out.println("Загальна вартість деривативу: " + totalValue);
    }
    
    public void viewDerivativeContracts(List<Derivative> derivatives, List<Customer> customers) {
        logger.debug("Перегляд контрактів деривативу");
        System.out.print("Введіть ID деривативу: ");
        int derivativeId = scanner.nextInt();
        
        Derivative derivative = findDerivativeById(derivatives, derivativeId);
            
        if (derivative == null) {
            logger.warn("Дериватив не знайдений з ID: {}", derivativeId);
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
        System.out.println("Дериватив ID: " + contract.getDerivativeId());
        System.out.printf("Статус: %s%n", contract.isActive() ? "Активний" : "Неактивний");
        System.out.printf("Вартість: %.2f%n", ContractProcessor.calculateValue(contract));
    }
    

    
    private Derivative findDerivativeById(List<Derivative> derivatives, int derivativeId) {
        return derivatives.stream()
            .filter(d -> d.getDerivativeId() == derivativeId)
            .findFirst()
            .orElse(null);
    }
    

}