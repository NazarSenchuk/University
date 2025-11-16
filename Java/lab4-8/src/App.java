import menu.*;
import menu.commands.*;
import model.*;
import service.*;
import java.time.LocalDate;
import java.util.*;

public class App {
    // Головні списки даних
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Contract> contracts = new ArrayList<>();
    private static final List<Claim> claims = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    
    // Сервіси (без зберігання даних всередині)
    private static final ContractService contractService = new ContractService(scanner);
    private static final ClaimService claimService = new ClaimService(scanner);
    private static final ContractProcessor contractProcessor = new ContractProcessor();

    public static void main(String[] args) {

        // Створення головного меню з передачею всіх даних
        Menu mainMenu = MenuFactory.createMainMenu(customers, contracts, claims, contractService, claimService, contractProcessor);
        MenuManager.pushMenu(mainMenu);
        
        // Головний цикл програми
        while (true) {
            Menu currentMenu = MenuManager.getCurrentMenu();
            if (currentMenu != null) {
                currentMenu.show();
            } else {
                MenuManager.pushMenu(mainMenu);
            }
        }
    }

}