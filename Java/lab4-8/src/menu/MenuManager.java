package menu;

import java.util.Stack;

public class MenuManager {
    private static final Stack<Menu> menuStack = new Stack<>();
    
    public static void pushMenu(Menu menu) {
        menuStack.push(menu);
    }
    
    public static void popMenu() {
        if (!menuStack.isEmpty()) {
            menuStack.pop();
        }
    }
    
    public static Menu getCurrentMenu() {
        return menuStack.isEmpty() ? null : menuStack.peek();
    }
    
    public static void clear() {
        menuStack.clear();
    }
}
