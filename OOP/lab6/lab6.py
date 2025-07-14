
# Описати і реалізувати базовий клас УПАКУВАННЯ ДЛЯ ЛІКІВ (задаються назва, розмір упакування (ширина, довжина, висота) і похідні класи УПАКУВАННЯ ДЛЯ ЛІКІВ У ФЛАКОНАХ (задаються вага речовини, вага флакону) та УПАКУВАННЯ ДЛЯ ЛІКІВ У ПОРОШКАХ (задається вага порошку). Введені дані про різні ліки, наявні в аптеці, 
# зберегти у списку об’єктів різних похідних класів, вивести дані кожного об’єкта на екран.
#  Обчислити сумарну вагу та сумарний об’єм і визначити 
# чи можна розмістити ці ліки у певній тарі із заданими максимально можливим об’ємом і максимально можливою вагою.

class Size:
    def __init__(self , width , height  ,  length ):
        self.width =  width
        self.height  = height 
        self.length  = length
    

    def volume(self):
        return self.height  * self.width * self.length
    
class MedicinesPackaging: 
    def __init__(self , name  ,  w , h , l ):
        self.__name =  name 
        self.__size =  Size(w, h  , l)

    @property
    def size(self):
        return self.__size 
    @property
    def name(self):
        return self.__name 
    @size.setter
    def size(self ,s):
        self.__size = s 
    @name.setter
    def name(self ,  n):
        self.__name = n 


    def __str__(self):
        return f"Упаковка: {self.name}, Висота: {self.size.height}   Довжина: {self.size.length} Ширина: {self.size.width} "


class FlaconsMedicinesPackaging(MedicinesPackaging):
    def __init__(self , name ,w ,h , l, sw , fw ):
        super().__init__( name  ,  w ,h , l )
        self.__substanceWeight =  sw  
        self.__flaconWeight =  fw  
        
    @property
    def substanceWeigth(self):
        return self.__substanceWeight
    @property
    def flaconWeight(self):
        return self.__flaconWeight 
    
    @substanceWeigth.setter
    def substanceWeigth(self ,s):
        self.__substanceWeight = s 

    @flaconWeight.setter
    def flaconWeight(self ,  n):
        self.__flaconWeight = n 

    def weigth(self):
        return self.substanceWeigth + self.flaconWeight
    

    def __str__(self):
        return super().__str__() + f" Тип: Флакон  Вага рідину: {self.substanceWeigth}  Вага флакону: {self.flaconWeight}  "


class PowderMedicinesPackaging(MedicinesPackaging):
    def __init__(self , name ,w ,h , l, weight ):
        super().__init__( name  ,  w ,h , l )
        self.__powderWeight  = weight  
      
        
    @property
    def powderWeight(self):
        return self.__powderWeight
   
    @powderWeight.setter
    def powderWeight(self ,s):
        self.__powderWeight = s 


    def weigth(self):
        return self.powderWeight
    
    def __str__(self):
        return super().__str__() + f"Тип: Порошок Вага порошку: {self.powderWeight}  "

class Medicine():
    def __init__(self , n , c ,  t,weigth):
        self.name = n
        self.weigth  = weigth
        self.type   = t
        self.count  =  c

    
        
class Program():
    @staticmethod
    def input_medicines(medicines):
        
        print("Введіть інформацію про ліки!\n")

        while True:
            print("\nВиберіть тип упаковки:")
            print("1. Флакон")
            print("2. Порошкові")
            print("0. Завершити введення")            
            choice = int(input("Ваш вибір: "))
            
            if choice not in[1,2] :
                break
                
            name = input("Введіть назву ліків: ")

            w = 0
            while 1:
                try:          
                    w = float(input("Введіть ширину упаковки: "))
                    if w < 1:
                        raise ValueError
                    break
                except:
                    print("Ви ввели некоректну ширину!!")
                    
            
            h = 0
            while 1:
                try:          
                    h = float(input("Введіть висоту упаковки: "))
                    if w < 1:
                        raise ValueError
                    break
                except:
                    print("Ви ввели некоректну висоту!!")


            l=0
            while 1:
                try:          
                    l = float(input("Введіть довжину упаковки: ")) 
                    if w < 1:
                        raise ValueError
                    break
                except:
                    print("Ви ввели некоректну довжину!!")
            
            match choice:
                case 1: 
                    sw=0
                    while 1:
                        try:          
                            sw = float(input("Введіть вагу речовини: ")) 
                            if sw < 1:
                                raise ValueError
                            break
                        except:
                            print("Ви ввели некоректну вагу речовини!!")

                    fw = 0

                    while 1:
                        try:          
                            fw = float(input("Введіть вагу флакону: ")) 
                            if fw < 1:
                                raise ValueError
                            break
                        except:
                            print("Ви ввели некоректну вагу флакону!!")


                    medicines.append(FlaconsMedicinesPackaging(name, w, h, l, sw, fw))
                case 2:
                    weight = 0
                    while 1:
                        try:          
                            weight = float(input("Введіть вагу флакону: ")) 
                            if weight < 1:
                                raise ValueError
                            break
                        except:
                            print("Ви ввели некоректну вагу флакону!!")
                    medicines.append(PowderMedicinesPackaging(name, w, h, l, weight))
            
        
        return medicines
    
    @staticmethod
    def check_package(medicines):
        max_volume = float(input("\nВведіть максимальний об'єм тари: "))
        max_weight = float(input(" Введіть максимальну вагу тари: "))

        total_volume = 0; 
        total_weight = 0; 

        for i in range(len(medicines)):
            total_volume += medicines[i].size.volume()
            total_weight  += medicines[i].weigth()

        if total_volume > max_volume:
            print("Ліки не вміщаться в задану тару")
        else: 
            print("Ліки вміщають в задану тару")
        if total_weight > max_weight:
            print("Ліки забагато важать  для заданої тари")
        else: 
            print("Вага ліків підходить для заданої тари")
        
        

        

    @staticmethod
    def print_medicines(medicines):
        print("\nСписок ліків в аптеці:")
        for  i in range(len(medicines)):
            print(f"{i+1}. {medicines[i]}")
                
                


        


def main():
    medicines  = []
    Program.input_medicines(medicines=medicines)

    Program.print_medicines(medicines=medicines)
    
    Program.check_package(medicines=medicines)




if __name__ == "__main__":
    main()