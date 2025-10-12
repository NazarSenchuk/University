package logger;

import droids.Droid;

public class BattleLogger {
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


    public static void roundLog(int round) {
        System.out.println(PURPLE + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║            🔄 РАУНД " + round + " 🔄            ║" + RESET);
        System.out.println(PURPLE + "╚══════════════════════════════════════╝" + RESET);
    }
    public static void attackLog(Droid attacker, Droid defender, int damage) {
        System.out.println("\n" + CYAN + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(CYAN + "║              ⚔️  АТАКА ⚔️              ║" + RESET);
        System.out.println(CYAN + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sатакує%s %s%s%s\n",
                YELLOW_BOLD, attacker.getName(), RESET,
                RED, RESET,
                RED_BOLD, defender.getName(), RESET);
        System.out.printf("💥 Нанесено ушкоджень: %s%d%s\n", 
                RED_BOLD, damage, RESET);
        System.out.printf("❤️  Здоров'я %s: %d/%d\n\n", 
                defender.getName(), defender.getHealth(), defender.getMaxHealth());
    }
    
    public static void criticalAtackLog(Droid attacker, Droid defender, int damage) {
        System.out.println("\n" + RED_BOLD + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(RED_BOLD + "║           💥 КРИТИЧНА АТАКА 💥          ║" + RESET);
        System.out.println(RED_BOLD + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sзавдав критичного удару%s по %s%s%s\n",
                YELLOW_BOLD, attacker.getName(), RESET,
                RED_BOLD, RESET,
                RED_BOLD, defender.getName(), RESET);
        System.out.printf("💥💥 Нанесено КРИТИЧНИХ ушкоджень: %s%d%s\n", 
                RED_BOLD, damage, RESET);
        System.out.printf("❤️  Здоров'я %s: %d/%d\n\n", 
                defender.getName(), defender.getHealth(), defender.getMaxHealth());
    }
    
    public static void missLog(Droid attacker) {
        System.out.println("\n" + BLUE + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(BLUE + "║              🎯 ПРОМАХ 🎯              ║" + RESET);
        System.out.println(BLUE + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sпромахнувся!%s\n",
                YELLOW_BOLD, attacker.getName(), RESET,
                BLUE, RESET);
        System.out.println("🎯 Атака не влучила в ціль!\n");
    }
    
    public static void simpleAtackLog(Droid attacker, Droid defender, int damage) {
        System.out.println("\n" + YELLOW + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(YELLOW + "║             🗡️  ЗВИЧАЙНА АТАКА 🗡️           ║" + RESET);
        System.out.println(YELLOW + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sзавдав звичайного удару%s по %s%s%s\n",
                YELLOW_BOLD, attacker.getName(), RESET,
                YELLOW, RESET,
                RED_BOLD, defender.getName(), RESET);
        System.out.printf("🗡️  Нанесено ушкоджень: %s%d%s\n", 
                YELLOW_BOLD, damage, RESET);
        System.out.printf("❤️  Здоров'я %s: %d/%d\n\n", 
                defender.getName(), defender.getHealth(), defender.getMaxHealth());
    }
    
    public static void armorLog(Droid defender, int armor) {
        System.out.println("\n" + GREEN + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(GREEN + "║             🛡️  БРОНЯ 🛡️               ║" + RESET);
        System.out.println(GREEN + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sактивував броню!%s\n",
                GREEN_BOLD, defender.getName(), RESET,
                GREEN, RESET);
        System.out.printf("🛡️  Броня поглинула: %s%d%s ушкоджень\n", 
                GREEN_BOLD, armor, RESET);
        System.out.printf("❤️  Здоров'я %s: %d/%d\n\n", 
                defender.getName(), defender.getHealth(), defender.getMaxHealth());
    }
    
    public static void presentationLog(Droid attacker, Droid defender) {
        System.out.println("\n" + PURPLE + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║           🎭 ПОЧАТОК БОЮ 🎭           ║" + RESET);
        System.out.println(PURPLE + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sпроти%s %s%s%s\n",
                YELLOW_BOLD, attacker.getName(), RESET,
                PURPLE, RESET,
                RED_BOLD, defender.getName(), RESET);
        System.out.println("🎭 Бій починається!\n");
    }
    
    public static void battleStartLog() {
        System.out.println(PURPLE + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + "║          🏁 БИТВА ПОЧИНАЄТЬСЯ 🏁         ║" + RESET);
        System.out.println(PURPLE + "╚══════════════════════════════════════╝" + RESET);
    }
    
    public static void battleEndLog(Droid winner) {
        System.out.println(GREEN_BOLD + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(GREEN_BOLD + "║             🏆 ПЕРЕМОГА 🏆             ║" + RESET);
        System.out.println(GREEN_BOLD + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("🎉 Переможець: %s%s%s\n", 
                YELLOW_BOLD, winner.getName(), RESET);
        System.out.printf("❤️  Залишилось здоров'я: %s%d%s\n\n", 
                GREEN_BOLD, winner.getHealth(), RESET);
    }
    
    
    public static void levelUpLog(Droid droid) {
        System.out.println(GREEN_BOLD + "╔══════════════════════════════════════╗" + RESET);
        System.out.println(GREEN_BOLD + "║             📈 ЛЕВЕЛ-АП 📈            ║" + RESET);
        System.out.println(GREEN_BOLD + "╚══════════════════════════════════════╝" + RESET);
        System.out.printf("%s%s%s %sпідвищив рівень до %d!%s\n",
                YELLOW_BOLD, droid.getName(), RESET,
                GREEN_BOLD, droid.getLevel(), RESET);
        System.out.printf("❤️  Здоров'я: +10 | 🗡️  Атака: +10\n\n");
    }
    

    public  static  void inputNameLog(){ 
        System.out.println("Введіть назву дроїда:");
                
    }
    public static void inputTypeLog(){
        System.out.println("Виберіть тип дроїда:");
        System.out.println("1. Броньований");
        System.out.println("2. Штурмовий");
        
    }
    public static void sleepLog(int milliseconds){
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
    }
}