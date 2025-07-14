
from abc import ABC, abstractmethod





class Rectangle():
    def __init__(self , height, weight):
        self.__height = height
        self.__width = weight

    @property
    def width(self):
        return self.__width
    @property 
    def height(self):
        return self.__height
    
    @width.setter
    def width(self , value):
        self.__width = value

    @height.setter
    def height(self  ,value ):
        self.__height = value
    

    def area(self):
        return self.height * self.width
    

    def __lt__(self, other ):
        if (isinstance(other ,  Rectangle)): 
            return self.area() < other.area()
        
    def __isub__(self , other ):
        self.height-=other.height
        self.width-=other.width
        

    def __str__(self) -> str:
        return f"Прямокутник  довжина={self.width}, висота={self.height}, площа={self.area()})\n"
    

class RectangleProcessor():

        
    @staticmethod
    def printRectangles(rectangles):
        for i in rectangles:
            print(i)

    @staticmethod
    def add_rectangle(rectangles:list):
        tmp_rectangle = Rectangle( 0, 0 )
        print("Введіть інформацію про прямокутник\n")
        print("Довжина:")
        
        while True:
        
            try:
                width = int(input())
                if width > 0:
                    tmp_rectangle.width = width 
                    break
                else:
                    print("Довжина повинна бути більше 0. Спробуйте ще раз.")
            except ValueError:
                print("Будь ласка, введіть ціле число.")
        
        print("Висота:")
        while True:
        
            try:
                height = int(input())
                if width > 0:
                    tmp_rectangle.height = height 
                    break
                else:
                    print("Висота повинна бути більше 0. Спробуйте ще раз.")
            except ValueError:
                print("Будь ласка, введіть ціле число.")


        rectangles.append(tmp_rectangle)
        

    @staticmethod
    def sort(rectangles:list):
        print("Сортування!!")
        size = len(rectangles)
        for a  in  range(size):
            min = a
            for b  in range(  a+1 , size    ):
                if (rectangles[b].area() <  rectangles[min].area()):
                    min  = b

            rectangles[min]  , rectangles[a] =  rectangles[a] , rectangles[min]
        
    
    @staticmethod
    def decrease(rectangles):
        
        num = int(input("Введіть число на яке хочете зменшити довжину прямокутників:"))

        for i in rectangles:
            i -=  Rectangle(0,num)
        



def main():
    rectangles = []

   

    while(1):
        print('''1. Список прямокутників:\n2. Додати прямокутник:\n3. Сортувати\n4. Зменшити Довжити\n''')
        command =  int(input("Введіть пункт меню:"))

        
        match command:
            case 1:
                RectangleProcessor.printRectangles(rectangles=rectangles)
            case 2:
                RectangleProcessor.add_rectangle(rectangles= rectangles)
            case 3:
                RectangleProcessor.sort(rectangles=rectangles)
            case 4:
                RectangleProcessor.decrease(rectangles=rectangles)
                RectangleProcessor.sort(rectangles=rectangles)
            case _:  # default-випадок
                print("Невідомий пункт")

    

if __name__ == '__main__':
    main()