package factories;

import droids.ArmorDroid;
import droids.AssaultDroid;

import droids.Droid;
import droids.DroidType;
import logger.DroidLogger;
import java.util.Scanner;

public class  DroidFactory{
    public Droid createDroid(DroidType type, String name) {
        switch(type) {
            case ARMOR:
                return new ArmorDroid(name, 100, 10, 1, 0);
            case ASSAULT:
                return new AssaultDroid(name, 100, 14, 1, 0);
            default:
                return new ArmorDroid(name, 100, 10, 1, 0);
        }
    }
    
    
    public String inputDroidName(){
        Scanner sc = new Scanner(System.in);
        logger.DroidLogger.inputNameLog();
        String name  = sc.nextLine();
        
        return  name ;
    }   

    public DroidType inputDroidType(){
        logger.DroidLogger.inputDroidLog();
        Scanner sc = new Scanner(System.in);
        logger.DroidLogger.inputTypeLog();
        DroidType type    = DroidType.getById( sc.nextInt());
        
        return type ;
    }  
}