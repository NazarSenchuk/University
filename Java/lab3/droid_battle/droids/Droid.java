package droids;
public abstract class Droid {

    private String name;
    private int health;
    private int attack;
    private int level;
    private int experience;
    private int accuracy;
    public Droid(String name, int health, int attack, int level, int experience) {
        this.name = name;
        this.health = health;
        this.attack = attack;
        this.level = level;
        this.experience = experience;
        this.accuracy = 80;
    }
    public String getName() {
        return name;
    }
    public int getHealth() {
        return health;
    }
    public  int getMaxHealth() {
        return 100 + (level - 1) * 10;
    }
   
    public int getAttack() {
        return attack;
    }
    public int getLevel() {
        return level;
    }
    public int getExperience() {
        return experience;
    }
    public int getAccuracy() {
        return accuracy;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setHealth(int health) {
        this.health = health;
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }
    public void setAccuracy(int accuracy) {
        this.accuracy = accuracy;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setExperience(int experience) {
        this.experience = experience;
    }


    public abstract void attack(Droid target);
    public  abstract void takeDamage(int damage);
    public void levelUp(){
        level++;
        health += 10;
        attack += 10;
        experience = 0;
    }
    public void gainExperience(int experience){
        this.experience += experience;
    }
    public boolean isAlive(){

        return health > 0;  
    }

    @Override
    public String toString() {
        return "Дроїд{" +
                "назва='" + name + '\'' +
                ", здоров'я=" + health +
                ", атака=" + attack +
                ", лвл=" + level +
                ", досвіду=" + experience +
                ", точність=" + accuracy +
                '}';
    }
}