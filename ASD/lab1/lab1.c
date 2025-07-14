#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <locale.h>
//ПРОТОТИПИ ФУНКЦІЇ
void input_litera( char *ch  );
char *check_word( char litera , char *string , char *ptr_start ,char *ptr_end    );
void input_few_strings(char strings[][100], int count);
void check_word_in_few_strings(char strings[][100] , char litera  );

int main(void){
    
    system("chcp 1251") ;

    char litera; //ОГОЛОШУЄМО ЛІТЕРУ ЯКУ БУДЕМО ШУКАТИ
    char string[100]; // СИМВОЛЬНИЙ РЯДОК В ЯКОМУ БУДЕМО ШУКАТИ
    
    
    printf("Введіть речення:");
    gets(string); // ЗАПОВНЕННЯ СИМВОЛЬНОГО РЯДКА STRING
    
    input_litera(&litera); // ВВЕДЕННЯ ЛІТЕРИ ЯКУ БУДЕМО ШУКАТИ 
    
    char ptr_start,ptr_end; // ВКАЗІВНИКИ НА ПОЧАТОК ТА КІНЕЦЬ СЛОВА
    check_word(litera , string , &ptr_start , &ptr_end ); // ФУНКЦІЯ ЯКА ЗНАХОДИТЬ  ПЕРШЕ СЛОВО З БУКВОЮ LITERA  В РЯДКУ STRING

    char few_string[5][100]; // ОГОЛОШЕННЯ БАГАТО ВИМІРНОГО МАСИВУ ДЛЯ СИМВОЛЬНИХ РЯДКІВ
    input_few_strings(few_string,4); // ВВЕДЕННЯ СИМВОЛЬНИХ РЯДКІВ
    check_word_in_few_strings(few_string, litera); //ПЕРЕВІРКА НА СЛОВА  СИМВОЛЬНИХ РЯДКІВ
    
   

    return 0;
}



void input_litera(char *ch)
{
    printf("Введіть літеру:");
    *ch = getchar() ; // ВВЕДЕННЯ ЛІТЕРИ

}



char *check_word( char litera , char *string , char *ptr_start ,char *ptr_end    )
{   
    char *word = strtok(string," ");  
    while (word != NULL) {
        if(strchr(word,litera)!= NULL){ // ПРОХОДИМОСЬ ПО КОЖНОМУ СЛОВУ ЗА ДОПОМОГОЮ strtok ТА ПЕРЕВІРЯЄМО ЧИ Є СИМВОЛ
            printf("Перше слово з символом %c: %s \n", litera , word);
            ptr_start = &word[0]; // ЯКЩО Є ТО СТАВИМО ВКАЗІВНИКИ НА ПОЧАТОК ТА КІНЕЦЬ СЛОВА
            printf("Адрес початку слова:%p\n", ptr_start);
            ptr_end =  &word[strlen(word)-1];
            printf("Адрес кінця слова: %p\n" , ptr_end);
            return ptr_start;
            
        }

        word = strtok(NULL, " "); 
    }
    printf("!!!Слова з символом %c немає !!!\n", litera);
    return NULL; // ЯКЩО НІ ТО ПОВЕРТАЄМО НОЛЬ

}



void input_few_strings(char strings[][100], int count) //ВВЕДЕННЯ СИМВОЛЬНИХ РЯДКІВ
{
    printf("\n=========================================================\n");
    printf("=========================================================\n\n");
    printf("Введіть %d рядків\n" , count);
    rewind(stdin);
    for (int i =0 ; i <  count ; i++){
        printf("Введіть %d-ий рядок: " ,i+1);
        gets(strings[i]);
       
        
    }
    printf("\n=========================================================\n");
    printf("=========================================================\n\n");
    
    

}






void check_word_in_few_strings(char strings[][100] , char litera  ){ // ПЕРЕВІРЯЄМО СИМВОЛЬНІ РЯДКИ ЗА ДОПОМОГОЮ СТВОРЕНОЇ ФУНКЦІЇ check_word
    char ptr_start,ptr_end; 
    for (int i =0 ;  i < 4; i++ ){

        printf("Рядок:%s\n" , strings[i] );
        check_word(litera , strings[i] , &ptr_start ,&ptr_end);
        printf("\n");

    }


}
