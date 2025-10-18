package strategies;

import droids.Droid;
import logger.BattleLogger;
import java.util.List;

public class OneVsOneBattle  implements  BattleStrategy{
    private Droid winner;
    @Override
    public void startBattle(List<Droid> team1 , List<Droid>  team2 ){
        BattleLogger.battleStartLog();
        

        Droid droid1 = team1.get(0);
        Droid droid2 = team2.get(0);
        int round = 1;
        while (droid1.isAlive() && droid2.isAlive()) {
            BattleLogger.roundLog(round);
            
            BattleLogger.sleepLog(1000);
            droid1.attack(droid2);
            BattleLogger.sleepLog(1000);
            if (!droid2.isAlive()) {
                winner = droid1;
                BattleLogger.battleEndLog(winner);
                break;
            }
            
            
            droid2.attack(droid1);
            BattleLogger.sleepLog( 1000);
            if (!droid1.isAlive()) {
                winner = droid2;
                BattleLogger.sleepLog(round);
                BattleLogger.battleEndLog(winner);
                break;
            }
            
            round++;
        }

    }

    @Override
    public List<Droid> getWinner(){
        return List.of(winner);
    }

}

