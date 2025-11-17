import menu.*;
import menu.commands.*;
import model.*;
import service.*;
import java.time.LocalDate;
import java.util.*;

public class App {
    private static final List<Customer> customers = new ArrayList<>();
    private static final List<Contract> contracts = new ArrayList<>();
    private static final List<Claim> claims = new ArrayList<>();
    private static final List<Derivative> derivatives = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);
    private static final ContractService contractService = new ContractService(scanner);
    private static final ClaimService claimService = new ClaimService(scanner);
    private static final ContractProcessor contractProcessor = new ContractProcessor();

    public static void main(String[] args) {
        Menu mainMenu = MenuFactory.createMainMenu(customers, contracts, claims, contractService, claimService, contractProcessor, derivatives);
        MenuManager.pushMenu(mainMenu);
        
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