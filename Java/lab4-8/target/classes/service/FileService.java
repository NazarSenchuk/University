package service;

import model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileService {
    private static final String CUSTOMERS_FILE = "customers.csv";
    private static final String DERIVATIVES_FILE = "derivatives.csv";
    private static final String CONTRACTS_FILE = "contracts.csv";
    private static final String CLAIMS_FILE = "claims.csv";
    
    private static final Logger logger = LogManager.getLogger(FileService.class);

    public static void saveAll(List<Customer> customers, List<Derivative> derivatives,
                               List<Contract> contracts, List<Claim> claims) {
        logger.debug("Початок збереження всіх даних");
        saveCustomers(customers);
        saveDerivatives(derivatives);
        saveContracts(contracts);
        saveClaims(claims);
        logger.info("Всі дані успішно збережено");
    }

    public static void saveCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                writer.printf("%d,%s,%s,%s,%s%n",
                        c.getId(), c.getFirstName(), c.getLastName(), c.getPhone(), c.getEmail());
            }
            logger.debug("Клієнтів збережено - Кількість: {}", customers.size());
        } catch (IOException e) {
            logger.error("Помилка збереження клієнтів у файл: {}", CUSTOMERS_FILE, e);
            System.err.println("Помилка збереження клієнтів: " + e.getMessage());
        }
    }

    public static void saveDerivatives(List<Derivative> derivatives) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DERIVATIVES_FILE))) {
            for (Derivative d : derivatives) {
                writer.printf("%d,%d%n", d.getDerivativeId(), d.getCustomerId());
            }
            logger.debug("Деривативів збережено - Кількість: {}", derivatives.size());
        } catch (IOException e) {
            logger.error("Помилка збереження деривативів у файл: {}", DERIVATIVES_FILE, e);
            System.err.println("Помилка збереження деривативів");
        }
    }

    public static void saveContracts(List<Contract> contracts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONTRACTS_FILE))) {
            for (Contract c : contracts) {
                writer.printf("%d,%d,%d,%s,%s,%s,%b%n",
                        c.getContractNumber(), c.getDerivativeId(), c.getCustomerId(),
                        c.getStartDate(), c.getEndDate(), c.getRiskLevel(), c.isActive());
            }
            logger.debug("Контрактів збережено - Кількість: {}", contracts.size());
        } catch (IOException e) {
            logger.error("Помилка збереження контрактів у файл: {}", CONTRACTS_FILE, e);
            System.err.println("Помилка збереження контрактів");
        }
    }

    public static void saveClaims(List<Claim> claims) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLAIMS_FILE))) {
            for (Claim c : claims) {
                String safeDescription = c.getDescription().replace(",", " ");
                writer.printf("%d,%d,%d,%.2f,%s,%s,%s%n",
                        c.getClaimId(), c.getContractNumber(), c.getCustomerId(),
                        c.getAmount(), c.getStatus(), c.getSubmissionDate(), safeDescription);
            }
            logger.debug("Вимог збережено - Кількість: {}", claims.size());
        } catch (IOException e) {
            logger.error("Помилка збереження вимог у файл: {}", CLAIMS_FILE, e);
            System.err.println("Помилка збереження вимог");
        }
    }

    public static void loadAll(List<Customer> customers, List<Derivative> derivatives,
                               List<Contract> contracts, List<Claim> claims) {
        logger.debug("Початок завантаження всіх даних");
        loadCustomers(customers);
        loadDerivatives(derivatives);
        loadContracts(contracts, derivatives);
        loadClaims(claims);
        logger.info("Дані завантажено - Клієнтів: {}, Деривативів: {}, Контрактів: {}, Вимог: {}", 
                   customers.size(), derivatives.size(), contracts.size(), claims.size());
        System.out.println("Дані успішно завантажено з файлів.");
    }

    private static void loadCustomers(List<Customer> list) {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            logger.warn("Файл клієнтів не знайдено: {}", CUSTOMERS_FILE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 5) {
                    list.add(new Customer(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]));
                    count++;
                }
            }
            logger.debug("Клієнтів завантажено - Кількість: {}", count);
        } catch (Exception e) {
            logger.error("Помилка завантаження клієнтів з файлу: {}", CUSTOMERS_FILE, e);
        }
    }

    private static void loadDerivatives(List<Derivative> list) {
        File file = new File(DERIVATIVES_FILE);
        if (!file.exists()) {
            logger.warn("Файл деривативів не знайдено: {}", DERIVATIVES_FILE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 2) {
                    Derivative d = new Derivative();
                    d.setDerivativeId(Integer.parseInt(p[0]));
                    d.setCustomerId(Integer.parseInt(p[1]));
                    list.add(d);
                    count++;
                }
            }
            logger.debug("Деривативів завантажено - Кількість: {}", count);
        } catch (Exception e) {
            logger.error("Помилка завантаження деривативів з файлу: {}", DERIVATIVES_FILE, e);
        }
    }

    private static void loadContracts(List<Contract> contractsList, List<Derivative> derivativesList) {
        File file = new File(CONTRACTS_FILE);
        if (!file.exists()) {
            logger.warn("Файл контрактів не знайдено: {}", CONTRACTS_FILE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 7) {
                    Contract.RiskLevel riskLevel = Contract.RiskLevel.valueOf(p[5]);
                    
                    Contract c = new Contract(
                            Integer.parseInt(p[0]), 
                            Integer.parseInt(p[2]),
                            LocalDate.parse(p[3]), 
                            LocalDate.parse(p[4]),
                            Boolean.parseBoolean(p[6]), 
                            riskLevel, 
                            Integer.parseInt(p[1]));
                    contractsList.add(c);

                    derivativesList.stream()
                            .filter(d -> d.getDerivativeId() == c.getDerivativeId())
                            .findFirst().ifPresent(d -> d.addContract(c));
                    count++;
                }
            }
            logger.debug("Контрактів завантажено - Кількість: {}", count);
        } catch (Exception e) {
            logger.error("Помилка завантаження контрактів з файлу: {}", CONTRACTS_FILE, e);
        }
    }

    private static void loadClaims(List<Claim> list) {
        File file = new File(CLAIMS_FILE);
        if (!file.exists()) {
            logger.warn("Файл вимог не знайдено: {}", CLAIMS_FILE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 7) {
                    float amount = Float.parseFloat(p[3].replace(",", "."));
                    list.add(new Claim(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]), amount, p[6], LocalDate.parse(p[5]), p[4]));
                    count++;
                }
            }
            logger.debug("Вимог завантажено - Кількість: {}", count);
        } catch (Exception e) {
            logger.error("Помилка завантаження вимог з файлу: {}", CLAIMS_FILE, e);
        }
    }
}