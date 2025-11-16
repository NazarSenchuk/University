package menu.commands;

import menu.Command;
import menu.Menu;
import menu.MenuManager;
import java.util.function.Supplier;

public class SwitchToMenuCommand implements Command {
    private final Supplier<Menu> menuFactory;
    
    public SwitchToMenuCommand(Supplier<Menu> menuFactory) {
        this.menuFactory = menuFactory;
    }
    
    @Override
    public void execute() {
        MenuManager.pushMenu(menuFactory.get());
    }
}
