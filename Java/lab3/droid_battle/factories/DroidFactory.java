import droids.ArmorDroid;
import droids.AssaultDroid;



public enum DroidType{
    ARMOR,
    ASSAULT

}

public class  DroidFactory{
    public Droid createDroid(DroidType type  , string name){

        switch(type ){
            case ARMOR:
                return new ArmorDroid(name, 100, 10, 1, 0);
                break;

            case ASSAULT:
                return new AssaultDroid(name, 100, 14, 1, 0);
                break;
            default:
                break;
        }


    }



} 