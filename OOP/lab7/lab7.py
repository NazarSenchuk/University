#Багатокутник на площині (не обов’язково опуклий) заданий цілочисельними координатами своїх N вершин у декартовій системі координат.
#  Сторони багатокутника не дотикаються (за винятком сусідніх – у вершинах) і не перетинаються. Знайти площу багатокутника.
import matplotlib.pyplot as plt

class Coordinate():
    def __init__(self , x ,y):
        self.__x = x
        self.__y =  y
    
    @property
    def x(self):
        return self.__x
    @property
    def y(self):
        return self.__y
    
    @x.setter
    def x(self,x):
        self__x = x

    @y.setter
    def y(self,y):
        self.__y =  y


class Polygon():
    def __init__(self ,cordinates ):
        self.__cordinates  = cordinates 

    @property
    def cordinates(self):
        return self.__cordinates
    
    @cordinates.setter
    def cordinates(self, cordinates):
        self.__cordinates = cordinates

    def polygon_area(self):
        n = len(self.cordinates)
        area = 0.0
        for i in range(n):
            x_i, y_i = self.cordinates[i].x , self.cordinates[i].y 
            x_j, y_j = self.cordinates[(i + 1) % n].x ,self.cordinates[(i + 1) % n].y 
            area += (x_i * y_j) - (x_j * y_i)
        return abs(area) / 2


class Program():
    @staticmethod
    def Solve(polygon):
        return polygon.polygon_area()
    
    @staticmethod
    def Draw(polygon):
        x =  [i.x  for i in polygon.cordinates]
        y =  [i.y  for i in polygon.cordinates]

        plt.figure(figsize=(6, 6))
        plt.fill(x, y, 'b-', alpha=0.3, label='Багатокутник')
        
        plt.xlabel('X')
        plt.ylabel('Y')
        plt.title('Багатокутник')
        plt.legend()
        plt.grid(True)
        plt.show()
        



def main():
    polygon1 = Polygon([
    Coordinate(2, 5),
    Coordinate(1, 2),
    Coordinate(9, 3),
    Coordinate(7, 7),
    Coordinate(5, 3),
])
    print(Program.Solve(polygon1))
    Program.Draw(polygon1)

if __name__ == "__main__":
    main()