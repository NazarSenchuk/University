package service;

import model.*;
import java.io.*;
import java.time.LocalDate;
import java.util.List;

public class FileService {
    private static final String CUSTOMERS_FILE = "customers.csv";
    private static final String DERIVATIVES_FILE = "derivatives.csv";
    private static final String CONTRACTS_FILE = "contracts.csv";
    private static final String CLAIMS_FILE = "claims.csv";

    // === ЗБЕРЕЖЕННЯ (SAVE) ===
    public static void saveAll(List<Customer> customers, List<Derivative> derivatives,
                               List<Contract> contracts, List<Claim> claims) {
        saveCustomers(customers);
        saveDerivatives(derivatives);
        saveContracts(contracts);
        saveClaims(claims);
    }

    public static void saveCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer c : customers) {
                writer.printf("%d,%s,%s,%s,%s%n",
                        c.getId(), c.getFirstName(), c.getLastName(), c.getPhone(), c.getEmail());
            }
        } catch (IOException e) { System.err.println("Помилка збереження клієнтів: " + e.getMessage()); }
    }

    public static void saveDerivatives(List<Derivative> derivatives) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DERIVATIVES_FILE))) {
            for (Derivative d : derivatives) {
                writer.printf("%d,%d%n", d.getDerivativeId(), d.getCustomerId());
            }
        } catch (IOException e) { System.err.println("Помилка збереження деривативів"); }
    }

    public static void saveContracts(List<Contract> contracts) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CONTRACTS_FILE))) {
            for (Contract c : contracts) {
                writer.printf("%d,%d,%d,%s,%s,%s,%b%n",
                        c.getContractNumber(), c.getDerivativeId(), c.getCustomerId(),
                        c.getStartDate(), c.getEndDate(), c.getRiskLevel(), c.isActive());
            }
        } catch (IOException e) { System.err.println("Помилка збереження контрактів"); }
    }

    public static void saveClaims(List<Claim> claims) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CLAIMS_FILE))) {
            for (Claim c : claims) {
                String safeDescription = c.getDescription().replace(",", " ");
                writer.printf("%d,%d,%d,%.2f,%s,%s,%s%n",
                        c.getClaimId(), c.getContractNumber(), c.getCustomerId(),
                        c.getAmount(), c.getStatus(), c.getSubmissionDate(), safeDescription);
            }
        } catch (IOException e) { System.err.println("Помилка збереження вимог"); }
    }

    // === ЗАВАНТАЖЕННЯ (LOAD) ===
    public static void loadAll(List<Customer> customers, List<Derivative> derivatives,
                               List<Contract> contracts, List<Claim> claims) {
        loadCustomers(customers);
        loadDerivatives(derivatives);
        loadContracts(contracts, derivatives); // Тут відновлюємо зв'язок
        loadClaims(claims);
        System.out.println("Дані успішно завантажено з файлів.");
    }

    private static void loadCustomers(List<Customer> list) {
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 5) list.add(new Customer(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]));
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadDerivatives(List<Derivative> list) {
        File file = new File(DERIVATIVES_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 2) {
                    Derivative d = new Derivative();
                    d.setDerivativeId(Integer.parseInt(p[0]));
                    d.setCustomerId(Integer.parseInt(p[1]));
                    list.add(d);
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadContracts(List<Contract> contractsList, List<Derivative> derivativesList) {
        File file = new File(CONTRACTS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 7) {
                    Contract c = new Contract(
                            Integer.parseInt(p[0]), Integer.parseInt(p[2]),
                            LocalDate.parse(p[3]), LocalDate.parse(p[4]),
                            Boolean.parseBoolean(p[6]), p[5], Integer.parseInt(p[1]));
                    contractsList.add(c);

                    // Знаходимо дериватив і додаємо туди контракт
                    derivativesList.stream()
                            .filter(d -> d.getDerivativeId() == c.getDerivativeId())
                            .findFirst().ifPresent(d -> d.addContract(c));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static void loadClaims(List<Claim> list) {
        File file = new File(CLAIMS_FILE);
        if (!file.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] p = line.split(",");
                if(p.length >= 7) {
                    float amount = Float.parseFloat(p[3].replace(",", "."));
                    list.add(new Claim(Integer.parseInt(p[0]), Integer.parseInt(p[1]),
                            Integer.parseInt(p[2]), amount, p[6], LocalDate.parse(p[5]), p[4]));
                }
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}