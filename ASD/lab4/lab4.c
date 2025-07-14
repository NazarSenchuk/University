#include <stdio.h>
#include <stdlib.h> 
#include <string.h>
char* parser(char *string);
int main() {
    FILE* f;
    char path[100];
    printf("Введіть шлях до файлу:");
    gets(path);
    
    
    char string[100];
    
    if ((f = fopen(path, "r")) == NULL) {
        puts("Файл не знайдено\n");
        return 0;
    }
    int i =0 ;
    while ( i < 99   && (string[i] = getc(f)) != EOF) {
        
        i++;
    }
    string[i] = '\0';
    fclose(f);

    FILE* f1;
    if ((f1 = fopen(strcat(path ,".new"), "w")) == NULL) {
        puts("Файл не знайдено\n");
        return 0;
    }
    printf("%s" ,parser(string));

    for( int b =0 ; string[b] != '\0' ;  b++){
        putc(string[b] , f1);


    }
    fclose;
    return 0;
}


char* parser(char *string){
    char tmp;
    int i=0 ;
    while( string[i] != '\0'){
        if(isdigit(string[i]) && ( isdigit(string[i+1]) || string[i+1] == '.'  )){
            string[i] = ' ';
         

        }else if( string[i] =='.' && isdigit(string[i+1])){
            string[i] = ' ';
     

        }else if(isdigit(string[i]))
            string[i] = '?';



        i++;


    }   
    return string;



}

