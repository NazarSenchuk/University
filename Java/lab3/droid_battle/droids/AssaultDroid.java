package droids;

public class AssaultDroid extends Droid {
    private int criticalStrikeChance;



    public AssaultDroid(String name, int health, int attack, int level, int experience) {
        super(name, health, attack, level, experience);
        this.criticalStrikeChance = 20 + level * 5;
    }
    @Override
    public void attack(Droid target) {
        System.out.println(" Дроїд штурмовик з назвою " + getName() + " атакує!");
        if ( Math.random() * 100  < getAccuracy() ){
        if (Math.random() * 100 < criticalStrikeChance) {
            System.out.println("Дроїд штурмовик завдав критичного удару!");
            target.takeDamage(getAttack() * 2);
        } else {
            System.out.println("Дроїд штурмовик завдав удару!");
            target.takeDamage(getAttack());
        }} else {
            System.out.println( "Дроїд штурмовик промахнувся");


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
