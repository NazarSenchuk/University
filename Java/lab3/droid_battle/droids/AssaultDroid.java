package droids;
import logger.BattleLogger;

public class AssaultDroid extends Droid {
    private int criticalStrikeChance;



    public AssaultDroid(String name, int health, int attack, int level, int experience) {
        super(name, health, attack, level, experience);
        this.criticalStrikeChance = 30 + level * 5;
    }
    @Override
    public void attack(Droid target) {
        logger.BattleLogger.attackLog(this, target, criticalStrikeChance);
        if ( Math.random() * 100  < getAccuracy() ){

        if (Math.random() * 100 < criticalStrikeChance) {
           
            target.takeDamage(getAttack() * 2);
             logger.BattleLogger.criticalAtackLog(this, target, criticalStrikeChance);
        } else {
        
            target.takeDamage(getAttack());
            logger.BattleLogger.simpleAtackLog(this, target, criticalStrikeChance);
        }} else {
            logger.BattleLogger.missLog(target);


        }
    }

    @Override
    public void takeDamage(int attack){
        setHealth(getHealth() -  getAttack() );
        

    }
    @Override
    public String toString() {
        return super.toString() + ", тип: Штурмовик, шанс критичного удару: " + criticalStrikeChance + "%";
    }
}
