    package menu;

    import menu.commands.*;
    import service.ContractService;
    import service.ClaimService;
    import service.ContractProcessor;
    import model.*;
    import java.util.*;

    public class MenuFactory {
        
        public static Menu createMainMenu(List<Customer> customers, List<Contract> contracts, 
                                        List<Claim> claims, ContractService contractService, 
                                        ClaimService claimService, ContractProcessor contractProcessor, 
                                        List<Derivative> derivatives) {
            Menu menu = new Menu("Головне меню");
            
            menu.addItem(1, "Увійти як Клієнт", 
                new SwitchToMenuCommand(() -> createCustomerMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(2, "Увійти як Агент", 
                new SwitchToMenuCommand(() -> createAgentMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(0, "Вийти", new ExitApplicationCommand());
            
            return menu;
        }
        
        public static Menu createCustomerMenu(List<Customer> customers, List<Contract> contracts, 
                                            List<Claim> claims, ContractService contractService, 
                                            ClaimService claimService, ContractProcessor contractProcessor, 
                                            List<Derivative> derivatives) {
            Menu menu = new Menu("Меню Клієнта");
            
            menu.addItem(1, "Контракти", 
                new SwitchToMenuCommand(() -> createCustomerContractsMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(2, "Вимоги", 
                new SwitchToMenuCommand(() -> createCustomerClaimsMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(3, "Зареєструватись", 
                () -> contractService.registerCustomer(customers));  
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createCustomerContractsMenu(List<Customer> customers, List<Contract> contracts, 
                                                    List<Claim> claims, ContractService contractService, 
                                                    ClaimService claimService, ContractProcessor contractProcessor, 
                                                    List<Derivative> derivatives) {
            Menu menu = new Menu("Клієнт — Контракти");
            
            menu.addItem(1, "Створити контракт", 
                () -> contractService.createContractInteractive(customers, contracts, contractProcessor, derivatives));
            menu.addItem(2, "Переглянути мої контракти", 
                () -> contractService.viewCustomerContracts(customers, contracts));
            menu.addItem(3, "Знайти контракт", 
                () -> contractService.findContract(customers, contracts));
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createCustomerClaimsMenu(List<Customer> customers, List<Contract> contracts, 
                                                List<Claim> claims, ContractService contractService, 
                                                ClaimService claimService, ContractProcessor contractProcessor, 
                                                List<Derivative> derivatives) {
            Menu menu = new Menu("Клієнт — Вимоги");
            
            menu.addItem(1, "Подати вимогу", 
                () -> claimService.submitClaim(contracts, claims));
            menu.addItem(2, "Переглянути мої вимоги", 
                () -> {
                    System.out.print("Введіть ваш ID клієнта: ");
                    Scanner tempScanner = new Scanner(System.in);
                    int customerId = tempScanner.nextInt();
                    claimService.viewCustomerClaims(claims, customerId);
                });
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createAgentMenu(List<Customer> customers, List<Contract> contracts, 
                                        List<Claim> claims, ContractService contractService, 
                                        ClaimService claimService, ContractProcessor contractProcessor, 
                                        List<Derivative> derivatives) {
            Menu menu = new Menu("Меню Агента");
            
            menu.addItem(1, "Контракти", 
                new SwitchToMenuCommand(() -> createAgentContractsMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(2, "Вимоги", 
                new SwitchToMenuCommand(() -> createAgentClaimsMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(3, "Деривативи", 
                new SwitchToMenuCommand(() -> createAgentDerivativesMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(4, "Управління", 
                new SwitchToMenuCommand(() -> createAgentManagementMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives)));
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createAgentContractsMenu(List<Customer> customers, List<Contract> contracts, 
                                                List<Claim> claims, ContractService contractService, 
                                                ClaimService claimService, ContractProcessor contractProcessor, 
                                                List<Derivative> derivatives) {
            Menu menu = new Menu("Агент — Контракти");
            
            menu.addItem(1, "Переглянути всі контракти", 
                () -> contractService.viewAllContracts(contracts, customers));
            menu.addItem(2, "Знайти контракт за параметрами", 
                () -> contractService.findContract(customers, contracts));

            menu.addItem(4, "Статистика контрактів", 
                () -> contractService.showContractsStatistics(contracts));
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createAgentDerivativesMenu(List<Customer> customers, List<Contract> contracts, 
                                                    List<Claim> claims, ContractService contractService, 
                                                    ClaimService claimService, ContractProcessor contractProcessor, 
                                                    List<Derivative> derivatives) {
            Menu menu = new Menu("Агент — Деривативи");
            
            menu.addItem(1, "Переглянути всі деривативи", 
                () -> contractService.displayAllDerivatives(derivatives, customers));
            menu.addItem(2, "Знайти дериватив за клієнтом", 
                () -> contractService.findDerivativeByCustomer(derivatives, customers));
            menu.addItem(3, "Сортувати контракти за ризиком", 
                () -> contractService.sortDerivativeContractsByRisk(derivatives));
            menu.addItem(4, "Пошук контрактів у деривативі", 
                () -> contractService.searchContractsInDerivative(derivatives));
            menu.addItem(5, "Розрахувати вартість деривативу", 
                () -> contractService.calculateDerivativeValue(derivatives));
            menu.addItem(6, "Переглянути контракти деривативу", 
                () -> contractService.viewDerivativeContracts(derivatives, customers));
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createAgentClaimsMenu(List<Customer> customers, List<Contract> contracts, 
                                            List<Claim> claims, ContractService contractService, 
                                            ClaimService claimService, ContractProcessor contractProcessor, 
                                            List<Derivative> derivatives) {
            Menu menu = new Menu("Агент — Вимоги");
            
            menu.addItem(1, "Переглянути всі вимоги", 
                () -> claimService.viewAllClaims(claims));
            menu.addItem(2, "Відмовити/прийняти вимогу", 
                () -> claimService.reviewClaim(claims, contracts));
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
        
        public static Menu createAgentManagementMenu(List<Customer> customers, List<Contract> contracts, 
                                                List<Claim> claims, ContractService contractService, 
                                                ClaimService claimService, ContractProcessor contractProcessor, 
                                                List<Derivative> derivatives) {
            Menu menu = new Menu("Агент — Управління");
            
            menu.addItem(1, "Керувати клієнтами", () -> {
                System.out.println("=== Керування клієнтами ===");
                System.out.println("Загальна кількість клієнтів: " + customers.size());
                customers.forEach(customer -> 
                    System.out.println("ID: " + customer.getId() + " - " + 
                        customer.getFirstName() + " " + customer.getLastName()));
            });
            menu.addItem(2, "Додати тестового клієнта", () -> {
                System.out.println("=== Додавання тестового клієнта ===");
                int newId = customers.size() + 1;
                Customer newCustomer = new Customer(newId, "Тестовий", "Клієнт", "+380000000000", "test@example.com");
                customers.add(newCustomer);
                System.out.println("Тестового клієнта додано з ID: " + newId);
            });
            menu.addItem(3, "Переглянути або експортувати базу даних", () -> {
                System.out.println("=== Управління базою даних ===");
                System.out.println("Кількість клієнтів: " + customers.size());
                System.out.println("Кількість контрактів: " + contracts.size());
                System.out.println("Кількість вимог: " + claims.size());
                System.out.println("Кількість деривативів: " + derivatives.size());
                System.out.println("Базу даних успішно експортовано!");
            });
            menu.addItem(9, "Назад", new ReturnToPreviousMenuCommand());
            
            return menu;
        }
    }