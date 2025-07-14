import random
import time 

class Hero():
    def __init__(self , name ,health ,atack_power ,level):
        self.__name =name
        self.__health = health
        self.__atack_power = atack_power
        self.__level  = level
        self.__crit = 0
        self.__dodge = 0 
        self.__wins =  0
        self.__gold = 0 
        print("Створено персонажа") 

    @property 
    def name(self):
        return self.__name
    
    @property 
    def wins(self):
        return self.__wins
    
    @property 
    def health(self):
        return self.__health
    
    @property 
    def atack_power(self):
        return self.__atack_power
    
    @property 
    def level(self):
        return self.__level
    
    @property 
    def crit(self):
        return self.__crit
    
    @property 
    def dodge(self):
        return self.__dodge
    
    @property 
    def gold(self):
        return self.__gold
    
    @name.setter
    def name(self , value):
        self.__name = value

    @wins.setter
    def wins(self , value):
        self.__wins= value
        if( self.wins == 3 ):
            self.level +=1 
            self.health = 100

    @health.setter
    def health(self , value):
        self.__health = value

    @crit.setter
    def crit(self , value):
        self.__crit = value

    @dodge.setter
    def dodge(self , value):
        self.__dodge = value


    @atack_power.setter
    def atack_power(self , value):
        self.__atack_power = value
    
    @level.setter
    def level(self , value):
        self.__level = value

    @level.setter
    def gold(self , value):
        self.__gold = value

    def attack(self, enemy):

        round = 1 
        while (enemy.health!= 0):
            print(f"Раунд {round}")
            time.sleep(3) 
            print( f"Персонаж  { self.name } атакує {enemy.type} ")
            if(random.random( ) < self.crit):
                enemy.take_damage(self.atack_power* 5) 
                print(f"Персонаж використовує критичний урон ({self.atack_power*5})")
                time.sleep(3)
            else:
                enemy.take_damage(self.atack_power)
                print(f"Персонаж завдає {self.atack_power} урона ")
                time.sleep(3)
        
            print(f"У ворога залишилось {enemy.health} здоров'я ") 
            time.sleep(3)
            if (enemy.health == 0 ):
                print("Ваш персонаж виграв")
                self.wins +=1 
                self.gold += 10
                break
            else:
                print(f"Ворог атакує персонажа {self.name}")
                if ( random.random() < self.dodge ):
                    print(f"Персонаж {self.name} ухилився")
                    time.sleep(1)
                else:
                    self.take_damage(enemy.atack_power)
                    print(f"У персонажа залишилось {self.health} здоров'я")
                    time.sleep(1)

            if (self.health <= 0 ):
                print("Ваш персонаж помер")
                break
            
            round+=1 
        

    def take_damage(self, amount):
        self.health = (self.health - amount )
         

    def print_info(self):
        print(self)
    
    def __str__(self):
        return (f"Ім'я: {self.name}\n"
                f"Рівень: {self.level}\n"
                f"Здоров'я: {self.health}\n"
                f"Атака: {self.atack_power}\n"
                f"Перемог: {self.wins}")

class Warior(Hero):
    def __init__(self, name , level =1  ):
        health  = 120  * (level)/1 
        atack_power  =  12 * (level) / 1
        super().__init__(name , health= health, atack_power=atack_power ,  level=level )
        print("з типом мечник ")
        self.dodge= 0.1

class Mage(Hero):
    def __init__(self, name , level =1  ):
        health  = 80  * (level)/1
        atack_power  =  8 * (level) / 1
        super().__init__(name , health= health, atack_power=atack_power ,  level=level )
        print("з типом маг ")
        self.crit= 0.5

class Archer(Hero):
    def __init__(self, name , level =1  ):
        health  = 100  * (level)/1 
        atack_power  =  10 * (level) / 1

        super().__init__(name , health= health, atack_power=atack_power ,  level=level )
        print("з типом лучник ") 
        self.dodge  = 0.6

class Enemy:
    def __init__(self, name, health, atack_power, enemy_type):
        self.name = name
        self.health = health
        self.atack_power = atack_power
        self.type = enemy_type
    
    def take_damage(self, amount):
        self.health -= amount
        if self.health < 0:
            self.health = 0

def create_random_enemy(hero_level):
    enemy_types = ["Гоблін", "Орк", "Скелет", "Демон", "Вовк"]
    enemy_type = random.choice(enemy_types)
    
    
    health = 50 
    atack_power = 5
    
    return Enemy(f"{enemy_type}", health, atack_power, enemy_type)

def create_hero():
    print("\nВиберіть клас героя:")
    print("1. Воїн")
    print("2. Маг")
    print("3. Лучник")
    
    choice = int(input("Ваш вибір: "))
    name = input("Введіть ім'я героя: ")
    
    
    level = int(input("Введіть початковий рівень: "))

    
    match choice:
        case 1:
            return Warior(name, level)
        case 2:
            return Mage(name, level)
        case 3:
            return Archer(name, level)
        case _:
            print("Невірний вибір, створюється воїн за замовчуванням")
            return Warior(name, level)

def main():
    hero = None
    
    while True:
        print("\n--- Арена Героїв ---")
        print("1. Створити героя")
        print("2. Почати бій")
        print("3. Інформація про героя")
        print("4. Вихід")
        
        choice = int(input("Виберіть пункт меню") )
        
        match choice:
            case 1:
                if(hero != None):
                    print("Герой вже створений")
                else: 
                    hero = create_hero()
            case 2:
                if hero is None:
                    print("Спочатку створіть героя!")
                    continue
                    
                enemy = create_random_enemy(hero.level)
                print(f"\nСтворно ворог: {enemy.name} ({enemy.type})!")
                print(f"Здоров'я: {enemy.health}, Атака: {enemy.atack_power}")
                
                hero.attack(enemy)
            case 3:
                if hero is None:
                    print("Героя ще не створено!")
                else:
                    hero.print_info()
            case  2:
                print("Дякуємо за гру!")
                break
            case _:
                print("Невірний вибір, спробуйте ще раз")

if __name__ == "__main__":
    main()