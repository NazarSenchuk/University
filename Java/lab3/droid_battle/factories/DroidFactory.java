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
                #logger
                break;

            case ASSAULT:
                return new AssaultDroid(name, 100, 14, 1, 0);
                #logger
                break;
            default:
                break;
        }

        


    }
    
    public Droid[] printListofDroids(Droid[] droids){
        #logger
        for (Droid droid : droids){
            System.out.println(droid);

        }


    }


}