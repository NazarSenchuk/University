#include <iostream>
#include <cstring>
#include <cmath>
#include <algorithm>
using namespace std;
char* reverse(char* char_string) {
    int len = strlen(char_string);
    for (int i = 0; i < len / 2; i++) {
        swap(char_string[i], char_string[len - i - 1]);

    }

    return char_string;


};


class wholeNumber {
private:
    int system;
    char* number;
public:
    wholeNumber() {
        number = new char[20];
        number[19] = '\0';
        system = 10;



    }
    wholeNumber(int sys, char* num) {
        number = new char[20];
        valid_system(sys);
        valid_number(num);
        setSystem(sys); setNumber(num);
        std::cout << "Створено число " << number << " в " << system << " системі числення\n";
    }
    wholeNumber(const wholeNumber& other) {
        system = other.system;
        number = new  char[20];
        strncpy(number, other.number, 20);


    }
    wholeNumber& operator=(const wholeNumber& other) {
        if (this != &other) {
            delete[] number;
            system = other.system;
            number = new char[20];
            strncpy(number, other.number, 20);



        }
        return *this;


    }
    int operator <=(wholeNumber& other) {

        if (this->change_to_10_system() <= other.change_to_10_system()) {

            return 1;
        }
        return 0;



    }
    int operator== (wholeNumber& other){
        if (this->change_to_10_system() == other.change_to_10_system()) {
        
            return 1;
        }
        return 0; 
    
    
    }
    int operator*(wholeNumber& other) {
        return this->change_to_10_system() * other.change_to_10_system();



    }
    friend istream& operator>>(istream&, wholeNumber&);
    friend ostream& operator<<(ostream&, wholeNumber&);
    ~wholeNumber() {
        delete[] number;  // Додано звільнення пам'яті
    }
    int pow_w(int number);
    int getSystem() { return system; }
    char* getNumber() { return number; }
    void setSystem(int sys) { valid_system(sys); system = sys; }
    void setNumber(char* num) { strcpy(number, num); }
    int change_to_10_system(void);
    char* change_to_another_system(int sys);
    int spiliniy_dilynik(wholeNumber& other);
    int valid_system(int system) {
        if (system <= 0) {
            cout << "!!Система повинна бути  більше нуля!!";
            return 0;


        }


        return 1;

    };


    int valid_number(char* number) {
        if (strchr(number, '-')) {
            cout << "!!Число повинне бути не від'ємним!!\n";
            return 0;


        }
        for (int i = 0; number[i] != '\0'; i++) {
            if (isdigit(number[i]) == 0) {
                cout << "!!Неповинно бути букв!!\n";
                return 0;

            }

            if ((number[i] - '0') >= system) {
                cout << "!!Цифри повинні бти менші за систему!!\n";
                return 0;


            }



        }
        return 1;
    }




};
ostream& operator<<(ostream& os, wholeNumber& obj) {
    os << "Число:" << obj.number << " в системі:" << obj.system << '\n';
    return os;




}
istream& operator>>(istream& is, wholeNumber& obj) {
    cout << "Введіть число та систему числення\n";
    int tempSystem;
    char tempNumber[20];
    do {
        cout << "Система числення:";
        is >> tempSystem;
        cin.clear(); // очищаємо стан помилки
        cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
    } while (obj.valid_system(tempSystem) == 0);



    do {
        cout << "Число:";
        is.getline(tempNumber, 20);
    } while (obj.valid_number(tempNumber) == 0);

    obj.setSystem(tempSystem);
    obj.setNumber(tempNumber);
    return is;



}
int wholeNumber::change_to_10_system(void) {
    int len = strlen(number);
    int new_number = 0;
    for (char* ptr = number; *ptr != '\0'; ptr++) {
        len = len - 1;
        new_number += int(*ptr - '0') * pow(system, len);


    }
    return new_number;


};

char* wholeNumber::change_to_another_system(int sys) {
    valid_system(sys);
    int in_10_system = this->change_to_10_system();
    char* new_number = new char[20];
    char* ptr = new_number;
    while (in_10_system > 0) {
        *ptr = (in_10_system % sys) + '0';
        in_10_system = in_10_system / sys;
        ptr++;

    }
    *ptr = '\0';
    return reverse(new_number);

}

void sorting(int size, wholeNumber* numbers) {


    int start = 0;
    
    for (; start < size; start++) {
        int min = start;
        for (int i = start + 1; i < size; i++) {
            if ((numbers[i].change_to_10_system()) < numbers[min].change_to_10_system()) {

                min = i;


            }

        }
        if (min != start) {
            wholeNumber temp = numbers[start];
            numbers[start] = numbers[min];
            numbers[min] = temp;
        }


    }

}
int avarage_geometry(int size,wholeNumber* numbers){
    int dobutok = 1; 
    for (int i = 0; i < size; i++) {
        dobutok *= numbers[i].change_to_10_system();
        
    }
    return sqrt(dobutok);

    

}
int wholeNumber::spiliniy_dilynik(wholeNumber& other) {

    for (int i = 0; i != this->change_to_10_system(); i++) {


        if (this->change_to_10_system() % i == 0 || this->change_to_10_system() % i == 0) {
            return  i;
        }
        
    
    }
    return 0;


}
int wholeNumber::pow_w(int number) {

    return pow(this->change_to_10_system() , number);


}
int main() {
    const int size = 5;
    wholeNumber* numbers = new wholeNumber[size];

    for (int i = 0; i < size; i++) {
        cout << "Масив із чисел \n" << "Число " << i + 1 << ':' << "\n";
        cin >> numbers[i];
        cout << "\n_______________________________________\n\n";
    }
    sorting(size, numbers);
    cout << "Відсортований:\n";
    for (int i = 0; i < size; i++) {

        cout << numbers[i];
    }
    cout << "Середнє геометричне \n" << avarage_geometry(size,numbers) << '\n';
    
    

    wholeNumber* number_copied = new wholeNumber(numbers[0]);
    cout << "Скопійоване число:";
    cout << *number_copied << '\n';
    
    cout << "Множення" << '\n' << numbers[0] << "та \n" << numbers[1] ;
    cout << "Результат" << numbers[0] * numbers[1] << '\n';

    cout << "Оператор <= \n";
    cout << "Результат:";
    int i =  numbers[0]<=numbers[1];
    cout << i << '\n';

    cout << "Оператор == \n";
    cout << "Результат:";
    i = numbers[0] == numbers[1];
    cout << i << '\n';


    cout << "Степінь : \n";
    for (int b = 0; b < size; b++) {
        cout << numbers[b].pow_w(2)<< '\n';
    }



    
    return 0;
}