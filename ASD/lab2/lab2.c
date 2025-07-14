#include <stdio.h>
#include <wchar.h>
#include <locale.h>
#include <string.h>
#define MAX_m 3
#define MAX_n 3
double avarage_massive(double massive[]);
void reverse_massive(double massive[], int size);
void print_matritsa(double matrix[MAX_n][MAX_m]);
double recursive_avarage_massive(double massive[], int index, int size, double sum);
void input_matritsa(double matrix[MAX_n][MAX_m]);
int main() {
   
    

    //double matrix[MAX_n][MAX_m] = { {1,2,3,4,5},{6,7,8,9,10} ,{11,12,13,14,15} };
    double matrix[MAX_n][MAX_m];               //ОГОЛОШЕННЯ МАТРИЦІ
    input_matritsa(matrix);   //ФУНКЦІЯ ВВЕДЕННЯ
    printf("\n=======================================\n"); 
    printf("=======================================\n\n");
    printf("Звичайна Матриця %dx%d:\n", MAX_m, MAX_n);  //ВИВЕДЕННЯ МАТРИЦІ
    print_matritsa(matrix);
    double avarage_first =  avarage_massive(matrix[0]); //ФУНКЦІЯ ЯКА ЗНАХОДИТЬ СЕРЕДНЄ ЗНАЧЕННЯ РЯДКА
    
    for (int i = 0; i < MAX_n; i++ ) {
        if (avarage_massive(matrix[i]) > avarage_first) {
            reverse_massive(matrix[i], MAX_m);              //ПЕРЕВЕРТАЄМО РЯДОК ЯКЩО СЕРЕДНЄ ЗНАЧЕННЯ МЕНШЕ ЗА ЗНАЧЕННЯ ПЕРШОГО РЯДКУ
        }
    }
    printf("\n=======================================\n");
    printf("=======================================\n\n");
    printf("Переверсована Матриця %dx%d:\n", MAX_m, MAX_n);
    print_matritsa(matrix);                                  //ВИВОДИМО РЕВЕРСОВАНУ МАТРИЦЮ

    printf("\n=======================================\n");
    printf("=======================================\n\n");
    printf("Рекурсивна версія функції визначення середнього значення першого рядка:");
    printf("\n Результат рекурсивної функції: %f",recursive_avarage_massive(matrix[0], 0,MAX_m, 0)); // РЕЗУЛЬТАТИ РЕКУРСИВНОЇ ВЕРСІЇ ФУНКЦІЇ
    
    

}
double avarage_massive(double massive[]) { // ІТЕРАТИВНА ВЕРСІЯ ФУНКЦІЇ
    double sum = 0; 
    for (int i = 0; i < MAX_m; i++) {
        sum += *(massive + i);
    
    }
    return sum/MAX_m;


}
void reverse_massive(double massive[], int size) {
    for (int i = 0; i < size / 2; i++) {            //ЗМІНА ЗНАЧЕНЯ МІСЦЯМИ
        double temp = *(massive+i);
        *(massive + i) =*( massive+(size - 1 - i));
        *(massive + (size - 1 - i)) = temp;
    }
}

void input_matritsa( double matrix[MAX_n][MAX_m]) {
    printf("Введення значень матриці:\n");
    for (int i = 0; i < MAX_n; i++){
        for (int j = 0; j < MAX_m; j++) {
            printf("Введіть елемент [%d][%d]:", i ,j  );
            scanf_s("%lf" , &matrix[i][j] ,stdin);
        }
    
    
    
    }
    

}

void print_matritsa(double matrix[MAX_n][MAX_m]) {

    
    for (int i = 0; i < MAX_n; i++) {
        for (int j = 0; j < MAX_m; j++) {
            printf("%.2f\t", matrix[i][j]);
        }
        printf("Середнє значенння:  %f"  , avarage_massive(matrix[i]));
        printf("\n");
    }


}




double recursive_avarage_massive(double massive[], int index, int size, double sum) {
  
    if (index == size) {
        printf("\n Досягнуто БАЗИС!! ");
        printf("  Значення середнього числа: %f ", sum / size);         //РЕКУРСИВНА ВЕРСІЯ 
        return  sum / size;

    }
    printf("\nРівень рекурсії входу: %f ", index);
    printf("  Значення середнього числа: %f ", sum/size);
    double result = recursive_avarage_massive(massive, index + 1, size, sum + massive[index]);
    printf("\nРівень рекурсії виходу: %f ", index);
    printf("  Значення середнього числа: %f ", sum/size);
    return result;
}