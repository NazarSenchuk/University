package logger;

import droids.Droid;
import droids.DroidType;
public class DroidLogger{
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";
    private static final String CYAN = "\033[0;36m";
    private static final String RED_BOLD = "\033[1;31m";
    private static final String GREEN_BOLD = "\033[1;32m";
    private static final String YELLOW_BOLD = "\033[1;33m";
    private static final String BLUE_BOLD = "\033[1;34m";
    

    public static void droidInfoLog(Droid droid) {
        System.out.println(YELLOW + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + "║            🤖 ІНФО ДРОЇДА 🤖           ║" + RESET);
        System.out.println(YELLOW + "╚══════════════════════════════════════╝" + RESET);
        System.out.println(droid.toString());
        System.out.println();
    }

    
    public  static  void inputDroidLog(){ 
        System.out.println(GREEN_BOLD + "╔══════════════════════════════════════════════════════╗" + RESET);
        System.out.println(GREEN_BOLD + "║             📈 ВВЕДІТЬ ДАНІ ПРО ДРОЇДА 📈            ║" + RESET);
        System.out.println(GREEN_BOLD + "╚══════════════════════════════════════════════════════╝" + RESET);

        
    }

    public  static  void inputNameLog(){ 
        System.out.println("Введіть назву дроїда:");
                
    }
    public static void inputTypeLog(){
        System.out.println("Виберіть тип дроїда:");
        DroidType.displayAllTypes();
    

        
    }
}