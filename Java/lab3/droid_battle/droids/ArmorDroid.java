package droids;

public class ArmorDroid extends Droid {
    private int armor;



    public ArmorDroid(String name, int health, int attack, int level, int experience) {
        super(name, health, attack, level, experience);
        this.armor = 20 + level * 5;
    }
    @Override
    public void attack(Droid target) {
        
        System.out.println(" Броньований дроїд з назвою " + getName() + " атакує!");
        if (Math.random() * 100 < getAccuracy() ) {
            System.out.println("Броньований дроїд завдав удару!");
            target.takeDamage(getAttack());
        } else {
            System.out.println( "Дроїд штурмовик промахнувся");
        }
    }
    @Override
    public void takeDamage(int attack){
        if (Math.random() * 100 <= 50 )
            setHealth(getHealth() -  getAttack() );
        else 
            setHealth(getHealth() -  (getAttack()  - armor * 2));
    }
    @Override
    public String toString() {
        return super.toString() + ", тип: Броньований, броні: " + armor + "%";
    }
}
