package droids;
import logger.BattleLogger;
public class ArmorDroid extends Droid {
    private int armor;



    public ArmorDroid(String name, int health, int attack, int level, int experience) {
        super(name, health, attack, level, experience);
        this.armor = 20 + level * 5;
    }
    @Override
    public void attack(Droid target) {
        
        logger.BattleLogger.attackLog(this, target,this.getAttack());
        if (Math.random() * 100 < getAccuracy() ) {
            target.takeDamage(getAttack());
            logger.BattleLogger.simpleAtackLog(this, target, getAttack());
            
        } else {
            logger.BattleLogger.missLog(this);
        }
    }
    @Override
    public void takeDamage(int incomingDamage) { 
        int actualDamage = incomingDamage; 
        
        if (Math.random() * 100 <= 50) {

            actualDamage = Math.max(0, incomingDamage - armor);
            BattleLogger.armorLog(this, armor);
        }
        
        setHealth(getHealth() - actualDamage); 
    }

    @Override
    public String toString() {
        return super.toString() + ", тип: Броньований, броні: " + armor + "%";
    }
}
