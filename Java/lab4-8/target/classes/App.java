import menu.*;
import menu.commands.*;
import model.*;
import service.*;
import java.time.LocalDate;
import java.util.*;
import service.FileService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import util.LoggerUtil;


public class App {
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Contract> contracts = new ArrayList<>();
    private static final List<Claim> claims = new ArrayList<>();
    private static final List<Derivative> derivatives = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final ContractService contractService = new ContractService(scanner);
    private static final ClaimService claimService = new ClaimService(scanner);
    private static final ContractProcessor contractProcessor = new ContractProcessor();
    private static final Logger logger = LogManager.getLogger(App.class);

    public static void main(String[] args) {
            System.setProperty("mail.smtp.starttls.enable", "true");

        try {
            logger.info("Додаток запущено");
            
            FileService.loadAll(customers, derivatives, contracts, claims);
            logger.info("Дані завантажено - Клієнтів: {}, Контрактів: {}, Вимог: {}, Деривативів: {}", 
                       customers.size(), contracts.size(), claims.size(), derivatives.size());
            
            Menu mainMenu = MenuFactory.createMainMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives);
            MenuManager.pushMenu(mainMenu);

            while (true) {
                Menu currentMenu = MenuManager.getCurrentMenu();

                if (currentMenu != null) {
                    currentMenu.show();
                } else {
                    FileService.saveAll(customers, derivatives, contracts, claims);
                    logger.info("Додаток завершив роботу - Дані збережено");
                    System.out.println("Роботу завершено. Дані збережено.");
                    break;
                }
            }
        } catch (Exception e) {
            logger.error("Критична помилка в додатку", e);
            System.err.println("Сталася критична помилка. Перевірте логи для деталей.");
        }
    }
}