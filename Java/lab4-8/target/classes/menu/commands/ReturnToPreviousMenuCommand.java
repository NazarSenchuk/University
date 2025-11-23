package menu.commands;

import menu.Command;
import menu.MenuManager;

public class ReturnToPreviousMenuCommand implements Command {
    @Override
    public void execute() {
        MenuManager.popMenu();
    }
}
