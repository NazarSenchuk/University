package menu;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private final String title;
    private final Map<Integer, MenuItem> items = new LinkedHashMap<>();
    private final Scanner scanner = new Scanner(System.in);

    public Menu(String title) {
        this.title = title;
    }

    public void addItem(int key, String label, Command command) {
        items.put(key, new MenuItem(label, command));
    }

    public void show() {
        System.out.println("\n=== " + title + " ===");
        for (var entry : items.entrySet()) {
            System.out.println(entry.getKey() + ") " + entry.getValue().getLabel());
        }
        System.out.print("Ваш вибір: ");

        int choice = scanner.nextInt();
        execute(choice);
    }

    private void execute(int key) {
        MenuItem item = items.get(key);
        if (item != null) {
            item.getCommand().execute();
        } else {
            System.out.println("Невірний вибір!");
        }
    }

    private static class MenuItem {
        private final String label;
        private final Command command;

        public MenuItem(String label, Command command) {
            this.label = label;
            this.command = command;
        }

        public String getLabel() { return label; }
        public Command getCommand() { return command; }
    }
}
