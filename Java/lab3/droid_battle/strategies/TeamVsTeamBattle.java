
package strategies;

import droids.Droid;
import logger.BattleLogger;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class TeamVsTeamBattle implements BattleStrategy{
    
    private List<Droid> winner;
    @Override
    public void startBattle(List<Droid> team1 , List<Droid> team2) {
        BattleLogger.battleStartLog();
        BattleLogger.sleepLog(1000);
        Droid winner = null;
        int round = 1;
        while (true) {
            BattleLogger.roundLog(round);
            BattleLogger.sleepLog(1000);
            for (Droid attacker : team1) {
                if (!attacker.isAlive()) continue;
                    BattleLogger.sleepLog(1000);
                    Droid defender = getRandomDroid(team2);
                    attacker.attack(defender);                
                }
                if (!getAliveDroids( team1).isEmpty()) {break;
            }
            for (Droid attacker : team2) {
                if (!attacker.isAlive()) continue;
                    BattleLogger.sleepLog(1000);
                    Droid defender = getRandomDroid(team1);
                    attacker.attack(defender);                
                }
                if (!getAliveDroids( team2).isEmpty()) {break;
            }
            if (getAliveDroids(team1).isEmpty()) {
                winner = getAliveDroids(team2).get(0);
                BattleLogger.sleepLog(1000);
                BattleLogger.battleEndLog(winner);
                break;
            }
            if (getAliveDroids(team2).isEmpty()) {
                winner = getAliveDroids(team1).get(0);
                BattleLogger.sleepLog(1000);
                BattleLogger.battleEndLog(winner);
                break;
            }   
            round++;
        }
    }
    @Override
    public List<Droid> getWinner(){
        return winner;
    }

    public List<Droid> getAliveDroids(List<Droid> team){
        return team.stream().filter(Droid::isAlive).collect(Collectors.toList());
    }
    public Droid getRandomDroid(List<Droid> team){
        List<Droid> aliveDroids = getAliveDroids(team);
        if (aliveDroids.isEmpty()) return null;
        Random rand = new Random();
        return aliveDroids.get(rand.nextInt(aliveDroids.size()));
    }
}