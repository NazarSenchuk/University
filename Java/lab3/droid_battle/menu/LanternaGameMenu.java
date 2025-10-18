package menu;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.TextColor;
import factories.DroidFactory;
import droids.Droid;
import droids.DroidType;
import strategies.OneVsOneBattle;
import strategies.TeamVsTeamBattle;
import logger.DroidLogger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LanternaGameMenu {
    private List<Droid> droids;
    private DroidFactory factory;
    private Screen screen;
    private MultiWindowTextGUI gui;
    
    public LanternaGameMenu() {
        this.droids = new ArrayList<>();
        this.factory = new DroidFactory();
        initializeTerminal();
    }
    
    private void initializeTerminal() {
        try {
            screen = new TerminalScreen(new DefaultTerminalFactory().createTerminal());
            screen.startScreen();
            gui = new MultiWindowTextGUI(screen, new DefaultWindowManager(), 
                new EmptySpace(TextColor.ANSI.BLUE));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void showMainMenu() {
        if (gui == null) {
            showConsoleMenu();
            return;
        }

        BasicWindow mainWindow = new BasicWindow("БИТВА ДРОЇДІВ");
        mainWindow.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel mainPanel = new Panel();
        mainPanel.setLayoutManager(new LinearLayout(Direction.VERTICAL));
        
        // Заголовок
        mainPanel.addComponent(new Label("Оберіть дію:"));
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        
        // Кнопки меню (без емодзі)
        Button createDroidBtn = new Button("Створити дроїда", this::createDroid);
        Button viewDroidsBtn = new Button("Переглянути дроїдів", this::showDroids);
        Button oneVsOneBtn = new Button("Бій 1 на 1", this::oneVsOneBattle);
        Button teamBattleBtn = new Button("Командний бій", this::teamBattle);
        Button exitBtn = new Button("Вихід", () -> {
            try {
                screen.stopScreen();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        });
        
        mainPanel.addComponent(createDroidBtn);
        mainPanel.addComponent(viewDroidsBtn);
        mainPanel.addComponent(oneVsOneBtn);
        mainPanel.addComponent(teamBattleBtn);
        mainPanel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        mainPanel.addComponent(exitBtn);
        
        mainWindow.setComponent(mainPanel);
        gui.addWindowAndWait(mainWindow);
    }
    
    private void createDroid() {
        BasicWindow window = new BasicWindow("Створення дроїда");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new GridLayout(2));
        
        panel.addComponent(new Label("Ім'я дроїда:"));
        TextBox nameField = new TextBox();
        panel.addComponent(nameField);
        
        panel.addComponent(new Label("Тип дроїда:"));
        ComboBox<String> typeCombo = new ComboBox<>("Броньований", "Штурмовий");
        panel.addComponent(typeCombo);
        
        Panel buttonPanel = new Panel(new LinearLayout(Direction.HORIZONTAL));
        Button createBtn = new Button("Створити", () -> {
            String name = nameField.getText();
            if (name.isEmpty()) {
                showMessage("Помилка", "Введіть ім'я дроїда!");
                return;
            }
            
            DroidType type = typeCombo.getSelectedIndex() == 0 ? DroidType.ARMOR : DroidType.ASSAULT;
            Droid newDroid = factory.createDroid(type, name);
            droids.add(newDroid);
            
            showMessage("Успіх", "Дроїд " + name + " створений!");
            window.close();
        });
        
        Button cancelBtn = new Button("Скасувати", window::close);
        
        buttonPanel.addComponent(createBtn);
        buttonPanel.addComponent(cancelBtn);
        
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(buttonPanel);
        
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
    
    private void showDroids() {
        if (droids.isEmpty()) {
            showMessage("Інформація", "Дроїдів ще не створено!");
            return;
        }
        
        BasicWindow window = new BasicWindow("Список дроїдів");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        
        for (int i = 0; i < droids.size(); i++) {
            Droid droid = droids.get(i);
            String droidInfo = String.format("%d. %s (%s) HP:%d ATK:%d LVL:%d", 
                i + 1, droid.getName(), 
                droid.getClass().getSimpleName(),
                droid.getHealth(), droid.getAttack(), droid.getLevel());
            
            panel.addComponent(new Label(droidInfo));
        }
        
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Закрити", window::close));
        
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
    
    private void oneVsOneBattle() {
        if (droids.size() < 2) {
            showMessage("Помилка", "Потрібно щонайменше 2 дроїди для бою!");
            return;
        }
        
        BasicWindow window = new BasicWindow("Бій 1 на 1 - Вибір першого дроїда");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label("Оберіть першого дроїда:"));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        
        for (Droid droid : droids) {
            Button droidBtn = new Button(droid.getName() + " (" + droid.getClass().getSimpleName() + ")", 
                () -> selectSecondDroid(droid, window));
            panel.addComponent(droidBtn);
        }
        
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Назад", window::close));
        
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
    
    private void selectSecondDroid(Droid firstDroid, Window parentWindow) {
        parentWindow.close();
        
        BasicWindow window = new BasicWindow("Бій 1 на 1 - Вибір другого дроїда");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label("Перший дроїд: " + firstDroid.getName()));
        panel.addComponent(new Label("Оберіть другого дроїда:"));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        
        for (Droid droid : droids) {
            if (droid != firstDroid) {
                Button droidBtn = new Button(droid.getName() + " (" + droid.getClass().getSimpleName() + ")", 
                    () -> startOneVsOneBattle(firstDroid, droid, window));
                panel.addComponent(droidBtn);
            }
        }
        
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("Назад", window::close));
        
        window.setComponent(panel);
        gui.addWindowAndWait(window);
    }
    private List<Droid> selectDroidsForTeamBattle(Window parentWindow) {
        parentWindow.close();
        
        BasicWindow window = new BasicWindow("Командний бій - Вибір дроїдів");
        window.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label("Оберіть дроїдів для двох команд (по:"));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        
        List<Droid> selectedDroids = new ArrayList<>();
        
        for (Droid droid : droids) {
            Button droidBtn = new Button(droid.getName() + " (" + droid.getClass().getSimpleName() + ")", 
                () -> {
                    if (selectedDroids.contains(droid)) {
                        selectedDroids.remove(droid);
                        droidBtn.setLabel(droid.getName() + " (" + droid.getClass().getSimpleName() + ")");
                    } else if (selectedDroids.size() < 4) {
                        selectedDroids.add(droid);
                        droidBtn.setLabel(droid.getName() + " (" + droid.getClass().getSimpleName() + ") [ВИБРАНО]");
                    }
                });
            panel.addComponent(droidBtn);
        }
        
       
        window.setComponent(panel);
        gui.addWindowAndWait(window);
        return selectedDroids;
        
    }
    private void startTeamBattle(List<Droid> team1, List<Droid> team2, Window parentWindow) {
        parentWindow.close();
        team1 = selectDroidsForTeamBattle(parentWindow);    
        team2 = selectDroidsForTeamBattle(parentWindow);
        
        
        try {
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    private void startOneVsOneBattle(Droid droid1, Droid droid2, Window parentWindow) {
        parentWindow.close();
        
        // Зупиняємо GUI перед боєм
        try {
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        // Запускаємо бій у консольному режимі
        System.out.println("\n=== ПОЧАТОК БОЮ ===");
        System.out.println(droid1.getName() + " vs " + droid2.getName());
        System.out.println("===================\n");
        
        List<Droid> team1 = new ArrayList<>();
        team1.add(droid1);
        
        List<Droid> team2 = new ArrayList<>();
        team2.add(droid2);
        
        OneVsOneBattle battle = new OneVsOneBattle();
        battle.startBattle(team1, team2);
        
        Droid winner = battle.getWinner().get(0);
        System.out.println("\n=== РЕЗУЛЬТАТ БОЮ ===");
        System.out.println("ПЕРЕМОЖЕЦЬ: " + winner.getName());
        System.out.println("=====================\n");
        
        droids.removeIf(d -> !d.isAlive());

        System.out.println("Натисніть Enter для повернення в меню...");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        initializeTerminal();
        showMainMenu();
    }
    
    private void teamBattle() {
        if (droids.size() < 4) {
            showMessage("Помилка", "Потрібно щонайменше 4 дроїди для командного бою!");
            return;
        }
        
       try {
            screen.stopScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void showMessage(String title, String message) {
        BasicWindow messageWindow = new BasicWindow(title);
        messageWindow.setHints(Arrays.asList(Window.Hint.CENTERED));
        
        Panel panel = new Panel(new LinearLayout(Direction.VERTICAL));
        panel.addComponent(new Label(message));
        panel.addComponent(new EmptySpace(new TerminalSize(1, 1)));
        panel.addComponent(new Button("OK", messageWindow::close));
        
        messageWindow.setComponent(panel);
        gui.addWindowAndWait(messageWindow);
    }
    
    // Резервне консольне меню
    private void showConsoleMenu() {
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        
        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║           БИТВА ДРОЇДІВ              ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  1. Створити дроїда                  ║");
            System.out.println("║  2. Переглянути дроїдів              ║");
            System.out.println("║  3. Бій 1 на 1                       ║");
            System.out.println("║  4. Вихід                            ║");
            System.out.println("╚══════════════════════════════════════╝");
            System.out.print("Оберіть опцію: ");
            
            String choice = scanner.nextLine();
            
            switch (choice) {
                case "1":
                    createDroidConsole(scanner);
                    break;
                case "2":
                    showDroidsConsole();
                    break;
                case "3":
                    oneVsOneBattleConsole(scanner);
                    break;
                case "4":
                    System.out.println("Дякуємо за гру!");
                    return;
                default:
                    System.out.println("Неправильний вибір!");
            }
        }
    }
    
    private void createDroidConsole(java.util.Scanner scanner) {
        System.out.println("Створення дроїда:");
        System.out.print("Введіть ім'я дроїда: ");
        String name = scanner.nextLine();
        
        System.out.println("Оберіть тип дроїда:");
        System.out.println("1. Броньований");
        System.out.println("2. Штурмовий");
        System.out.print("Ваш вибір: ");
        int typeChoice = scanner.nextInt();
        scanner.nextLine(); 
        
        DroidType type = typeChoice == 1 ? DroidType.ARMOR : DroidType.ASSAULT;
        Droid newDroid = factory.createDroid(type, name);
        droids.add(newDroid);
        
        System.out.println("Дроїд " + name + " створений!");
        DroidLogger.droidInfoLog(newDroid);
    }
    
    private void showDroidsConsole() {
        if (droids.isEmpty()) {
            System.out.println("Дроїдів ще не створено!");
            return;
        }
        
        System.out.println("Список дроїдів:");
        for (int i = 0; i < droids.size(); i++) {
            System.out.println((i + 1) + ". " + droids.get(i));
        }
    }
    
    private void oneVsOneBattleConsole(java.util.Scanner scanner) {
        if (droids.size() < 2) {
            System.out.println("Потрібно щонайменше 2 дроїди для бою!");
            return;
        }
        
        System.out.println("Оберіть першого дроїда:");
        for (int i = 0; i < droids.size(); i++) {
            System.out.println((i + 1) + ". " + droids.get(i).getName());
        }
        System.out.print("Ваш вибір: ");
        int firstChoice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        System.out.println("Оберіть другого дроїда:");
        for (int i = 0; i < droids.size(); i++) {
            if (i != firstChoice) {
                System.out.println((i + 1) + ". " + droids.get(i).getName());
            }
        }
        System.out.print("Ваш вибір: ");
        int secondChoice = scanner.nextInt() - 1;
        scanner.nextLine();
        
        List<Droid> team1 = new ArrayList<>();
        team1.add(droids.get(firstChoice));
        
        List<Droid> team2 = new ArrayList<>();
        team2.add(droids.get(secondChoice));
        
        OneVsOneBattle battle = new OneVsOneBattle();
        battle.startBattle(team1, team2);
    }
    
    public void cleanup() {
        try {
            if (screen != null) {
                screen.stopScreen();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}