package strategies;

import droids.Droid;
import java.util.List;
public interface BattleStrategy{
    List<Droid> getWinner();
    void startBattle(List<Droid> team1 , List<Droid> team2) ;

}


