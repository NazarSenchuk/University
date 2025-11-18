package service;

import model.Claim;
import model.Contract;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ClaimService {
    private final Scanner scanner;
    private int nextClaimId = 1;
    
    public ClaimService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void submitClaim(List<Contract> contracts, List<Claim> claims) {
        try {
            System.out.println("=== Подача вимоги ===");
            System.out.println("Доступні контракти:");
            List<Contract> activeContracts = contracts.stream()
                    .filter(Contract::isActive)
                    .toList();

            if (activeContracts.isEmpty()) {
                System.out.println("Немає активних контрактів!");
                return;
            }

            for (Contract contract : activeContracts) {
                System.out.println("Контракт #" + contract.getContractNumber());
            }

            System.out.print("Номер контракту (тільки цифри, без #): ");
            String input = scanner.nextLine().trim();
            if (input.startsWith("#")) {
                input = input.substring(1);
            }

            int contractNumber;
            try {
                contractNumber = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Помилка: введіть тільки цифри для номера контракту!");
                return;
            }

            Contract contract = contracts.stream()
                    .filter(c -> c.getContractNumber() == contractNumber && c.isActive())
                    .findFirst()
                    .orElse(null);

            if (contract == null) {
                System.out.println("Контракт не знайдений або неактивний!");
                return;
            }

            // === ЗМІНИ ТУТ: Генеруємо ID і зберігаємо ===
            int nextClaimId = claims.stream().mapToInt(Claim::getClaimId).max().orElse(0) + 1;

            Claim claim = Claim.input(scanner, contractNumber, contract.getCustomerId(), nextClaimId);
            claims.add(claim);


            FileService.saveClaims(claims); // <--- ЗБЕРЕЖЕННЯ

            System.out.println("Вимогу успішно подано! ID: " + claim.getClaimId());
        } catch (Exception e) {
            System.out.println("Помилка при подачі вимоги: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void reviewClaim(List<Claim> claims , List<Contract> contracts) {
        try {
            System.out.println("=== Перегляд вимоги ===");

            System.out.print("ID вимоги: ");
            int claimId = scanner.nextInt();
            scanner.nextLine();

            Claim claim = claims.stream().filter(c -> c.getClaimId() == claimId).findFirst().orElse(null);

            if (claim == null) {
                System.out.println("Вимога не знайдена!");
                return;
            }
            System.out.println("Вимога #" + claimId);
            System.out.println("Контракт: " + claim.getContractNumber());
            System.out.println("Сума: " + claim.getAmount());
            System.out.println("Статус: " + claim.getStatus());
            System.out.println("Опис: " + claim.getDescription());
            System.out.print("Дія (1-схвалити, 2-відхилити): ");
            int action = scanner.nextInt();
            scanner.nextLine();

            if (action == 1) {
                claim.setStatus("Схвалено");

                // Шукаємо контракт для деактивації
                Contract contract = contracts.stream()
                        .filter(c -> c.getContractNumber() == claim.getContractNumber())
                        .findFirst()
                        .orElse(null);

                if (contract != null) {
                    contract.setActive(false);
                }

                // Зберігаємо обидва файли, бо змінилися і контракти, і вимоги
                FileService.saveClaims(claims);
                FileService.saveContracts(contracts);

                System.out.println("Вимогу " + claimId + " схвалено");
            } else if (action == 2) {
                claim.setStatus("Відхилено");

                FileService.saveClaims(claims); // Зберігаємо тільки вимоги

                System.out.println("Вимогу " + claimId + " відхилено");
            }
        } catch (Exception e) {
            System.out.println("Помилка: " + e.getMessage());
        }
    }
    
    public void viewCustomerClaims(List<Claim> claims, int customerId) {
        System.out.println("=== Вимоги клієнта " + customerId + " ===");
        List<Claim> customerClaims = claims.stream()
            .filter(c -> c.getCustomerId() == customerId)
            .toList();
            
        if (customerClaims.isEmpty()) {
            System.out.println("Вимог не знайдено");
            return;
        }
        
        for (Claim claim : customerClaims) {
            System.out.printf("Вимога #%d - Контракт #%d - Сума: %.2f - Статус: %s%n - Опис: %s%n",
                claim.getClaimId(), claim.getContractNumber(), 
                claim.getAmount(), claim.getStatus() , claim.getDescription());
        }
    }
    
    public void viewAllClaims(List<Claim> claims) {
        System.out.println("=== Всі вимоги ===");
        for (Claim claim : claims) {
            System.out.printf("Вимога #%d - Контракт #%d - Сума: %.2f - Статус: %s%n - Опис: %s%n",
                claim.getClaimId(), claim.getContractNumber(), 
                claim.getAmount(), claim.getStatus() , claim.getDescription());
        }
    }
}