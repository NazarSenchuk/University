import menu.LanternaGameMenu;

public class Main {
    public static void main(String[] args) {
        LanternaGameMenu gameMenu = new LanternaGameMenu();
        
        try {
            gameMenu.showMainMenu();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            gameMenu.cleanup();
        }
    }
}