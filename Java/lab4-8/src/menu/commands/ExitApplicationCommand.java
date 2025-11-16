package menu.commands;

import menu.Command;

public class ExitApplicationCommand implements Command {
    @Override
    public void execute() {
        System.out.println("Дякуємо за використання програми. До побачення!");
        System.exit(0);
    }
}
