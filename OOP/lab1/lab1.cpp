    #include <iostream>
    #include <cstring>
    #include <cmath>
    #include <algorithm>
    using namespace std;
    char* reverse(char* char_string){
        int len = strlen(char_string);
        for(int i = 0 ; i< len/2; i++){
                swap(char_string[i] , char_string[len-i-1]);

        }       

        return char_string;


    };

   
    class wholeNumber{
        private:
        int system;
        char number[20];
        public:
        wholeNumber(){
            
            cout << "Введіть число та систему числення\n";
            
            do{cout << "Система числення:";
            cin >> system;
            cin.clear(); // очищаємо стан помилки
            cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
            }while(valid_system(system) ==0 );
            

            
            do{cout << "Число:";
            cin >> number;
            }while(  valid_number(number) ==0 );
            
            
            
            }
        wholeNumber(int sys , char* num ){ 
            valid_system(sys);
            valid_number(num);
            setSystem(sys); setNumber(num);
            std::cout << "Створено число " << number << " в " << system << " системі числення\n"; 
        }
        ~wholeNumber(){}
        int getSystem(){ return system;  }
        char *getNumber(){return number;}
        int setSystem(int sys){valid_system(sys);system = sys;}
        char *setNumber(char *num){valid_number(number) ; strcpy(number, num);  }
        int change_to_10_system(void);
        char* change_to_another_system(int sys);
        int valid_system(int system ){
        if (system <= 0 ){
            cout<< "!!Система повинна бути  більше нуля!!";
            return 0;

            
        }
        
        
        return 1;
        
        };


        int valid_number(char *number){
        if(strchr(number , '-')){
            cout<<"!!Число повинне бути не від'ємним!!\n";
            return 0 ;


        }
        for(int i =0 ; number[i] != '\0' ; i++){
            if (isdigit(number[i])==0){
                cout<<"!!Неповинно бути букв!!\n";
                return 0;

            }
            
            if((number[i] - '0')>=system){
                cout<<"!!Цифри повинні бти менші за систему!!\n";
                return 0;


            }



        }
        return 1;
        }



    
    };
    int wholeNumber::change_to_10_system(void){
        int len = strlen(number);
        int new_number=0;
        for(char *ptr= number; *ptr!='\0'; ptr++){
            len= len-1;
            new_number += int(*ptr-'0') * pow(system,len);
        

        }
        return new_number;


    };

    char* wholeNumber::change_to_another_system(int sys){
        valid_system(sys);
        int in_10_system = this->change_to_10_system();
        char *new_number= new char[20] ;
        char *ptr= new_number;
        while(in_10_system>0){
            *ptr=( in_10_system%sys)+'0';
            in_10_system=in_10_system/sys;
            ptr++;

        }
        *ptr = '\0';
        return reverse(new_number); 

    }



    int main (){
       

        
        wholeNumber number_1;
        //number_1.setNumber("123");
        //cout << "Число змінилось на  "<<  number_1.getNumber() <<"\n";
        //number_1.setSystem(3);
        //cout << "Система змінилась на  "<<  number_1.getSystem() <<"\n";

        int system;
        cout << "Введіть  систему в яку хочете перевести:";
        cin >> system;
        cout << "Переведоно в систему " << system << '\n';
        cout << number_1.change_to_another_system(system);



        return 0 ;
    }