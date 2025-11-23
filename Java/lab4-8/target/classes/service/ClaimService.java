package service;

import model.Claim;
import model.Contract;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClaimService {
    private final Scanner scanner;
    private int nextClaimId = 1;
    private static final Logger logger = LogManager.getLogger(ClaimService.class);
    
    public ClaimService(Scanner scanner) {
        this.scanner = scanner;
    }

    public void submitClaim(List<Contract> contracts, List<Claim> claims) {
        try {
            logger.info("Початок процесу подачі вимоги");
            System.out.println("=== Подача вимоги ===");
            List<Contract> activeContracts = contracts.stream()
                    .filter(Contract::isActive)
                    .toList();

            if (activeContracts.isEmpty()) {
                logger.warn("Спроба подати вимогу - немає активних контрактів");
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
                logger.warn("Невірний формат номера контракту: {}", input);
                System.out.println("Помилка: введіть тільки цифри для номера контракту!");
                return;
            }

            Contract contract = contracts.stream()
                    .filter(c -> c.getContractNumber() == contractNumber && c.isActive())
                    .findFirst()
                    .orElse(null);

            if (contract == null) {
                logger.warn("Контракт не знайдений або неактивний: {}", contractNumber);
                System.out.println("Контракт не знайдений або неактивний!");
                return;
            }

            int nextClaimId = claims.stream().mapToInt(Claim::getClaimId).max().orElse(0) + 1;

            Claim claim = Claim.input(scanner, contractNumber, contract.getCustomerId(), nextClaimId);
            claims.add(claim);

            FileService.saveClaims(claims);

            logger.info("Вимогу успішно подано - ID: {}, Контракт: {}", claim.getClaimId(), contractNumber);
            System.out.println("Вимогу успішно подано! ID: " + claim.getClaimId());
        } catch (Exception e) {
            logger.error("Помилка при подачі вимоги", e);
            System.out.println("Помилка при подачі вимоги: " + e.getMessage());
        }
    }

    public void reviewClaim(List<Claim> claims , List<Contract> contracts) {
        try {
            logger.debug("Початок перегляду вимоги");
            System.out.println("=== Перегляд вимоги ===");

            System.out.print("ID вимоги: ");
            int claimId = scanner.nextInt();
            scanner.nextLine();

            Claim claim = claims.stream().filter(c -> c.getClaimId() == claimId).findFirst().orElse(null);

            if (claim == null) {
                logger.warn("Вимога не знайдена: {}", claimId);
                System.out.println("Вимога не знайдена!");
                return;
            }

            System.out.print("Дія (1-схвалити, 2-відхилити): ");
            int action = scanner.nextInt();
            scanner.nextLine();

            if (action == 1) {
                claim.setStatus("Схвалено");

                Contract contract = contracts.stream()
                        .filter(c -> c.getContractNumber() == claim.getContractNumber())
                        .findFirst()
                        .orElse(null);

                if (contract != null) {
                    contract.setActive(false);
                }

                FileService.saveClaims(claims);
                FileService.saveContracts(contracts);

                logger.info("Вимогу схвалено - ID: {}, Контракт: {}", claimId, claim.getContractNumber());
                System.out.println("Вимогу " + claimId + " схвалено");
            } else if (action == 2) {
                claim.setStatus("Відхилено");

                FileService.saveClaims(claims);

                logger.info("Вимогу відхилено - ID: {}", claimId);
                System.out.println("Вимогу " + claimId + " відхилено");
            } else {
                logger.warn("Невірна дія при перегляді вимоги: {}", action);
                System.out.println("Невірна дія!");
            }
        } catch (Exception e) {
            logger.error("Помилка при перегляді вимоги", e);
            System.out.println("Помилка: " + e.getMessage());
        }
    }
        public void viewCustomerClaims(List<Claim> claims, int customerId) {
        logger.debug("Перегляд вимог клієнта з ID: {}", customerId);
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
        logger.debug("Перегляд всіх вимог");
        System.out.println("=== Всі вимоги ===");
        for (Claim claim : claims) {
            System.out.printf("Вимога #%d - Контракт #%d - Сума: %.2f - Статус: %s%n - Опис: %s%n",
                claim.getClaimId(), claim.getContractNumber(), 
                claim.getAmount(), claim.getStatus() , claim.getDescription());
        }
    }

}