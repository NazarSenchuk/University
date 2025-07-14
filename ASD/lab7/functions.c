#include "headerf.h"
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
extern int** matrix;
extern int rows, cols;
extern  char file_name[];
void init_matrix_from_keyboard() {
	

	printf("Введіть кількість рядків:");
	scanf("%d" , &rows);
	rewind(stdin);

	printf("Введіть кількість стопців:");
	scanf("%d", &cols);
	rewind(stdin);
	matrix = (int**)malloc(rows * sizeof(int*));

	for (int i = 0; i < rows; i++) {
		matrix[i] = (int*)malloc(cols * sizeof(int));
		for (int b = 0; b < cols; b++) {
			printf("Ведіть елемент матриці [%d][%d] : " , i,b);
			scanf("%d" , &matrix[i][b]);
			rewind(stdin);
		
		}
	}



}
void init_matrix_from_file() {
	if (file_name[0] == '\0') {
		printf("Введіть назву файла звідки  хочете імпортувати:");
		gets(file_name);
	}
	FILE* file = fopen(file_name, "rb+");
	if (file == NULL) {
		printf("Помилка відкриття файлу для запису.\n");
		return;
	}
	fread(&rows , sizeof(int ) , 1 , file);
	fread(&cols, sizeof(int), 1, file);

	
	matrix = (int**)malloc(rows * sizeof(int*));



	for (int i = 0; i < rows; i++) {
		matrix[i] = (int*)malloc(cols * sizeof(int));

		
		fread(matrix[i], sizeof(int), cols, file);
			
	}

	
	fclose(file);
	printf("Дані успішно зчитано з файлу '%s'.\n", file_name );
};

void save_to_file() {
	if (file_name[0] == '\0') {
		printf("Введіть назву файла куда хочете зберегти:");
		gets(file_name);
	}


	FILE* file = fopen(file_name, "wb+");
	if (file == NULL) {
		printf("Помилка відкриття файлу для запису.\n");
		return;
	}

	
	fwrite(&rows, sizeof(int), 1, file);
	fwrite(&cols, sizeof(int), 1, file);

	for (int i = 0; i < rows; i++) {
		fwrite(matrix[i], sizeof(int), cols, file);
	}

	fclose(file);
	printf("Дані успішно збережено у файл '%s'.\n", file_name);
	

};
void print_matrix() {
	printf("Матриця: \n");
	for (int i = 0; i < rows; i++) {
		
		for (int b = 0; b < cols; b++) {
			
			printf("%d " , matrix[i][b]);

		}
		printf("\n");
	}
		
	

}

int smallest_avarage(  ) {
	int smallest_row = 0 ; 
	int smallest = get_avarage(matrix[0],cols);
	for (int i = 0; i < rows; i++) {
		int avarage_num = get_avarage(matrix[i] , cols);
		if (smallest > avarage_num) {
			smallest = avarage_num;
			smallest_row = i;
		
		}

	}
	return smallest_row;


}

void zsyv( int row) {

	for (int i = row; i >= 1 ;  i--) {
		int* temp = matrix[i - 1];
		matrix[i - 1] = matrix[i] ; 
		matrix[i] = temp;
	
	}


}
void zsyv_na_1() {
	int* temp = (int*)malloc(cols * sizeof(int)); // зберігаємо перший рядок
	for (int j = 0; j < cols; j++) {
		temp[j] =matrix[0][j];
	}
	for (int i = 1; i < rows; i++) {
		for (int j = 0; j < cols; j++) {
			matrix[i - 1][j] = matrix[i][j];
		}
	}
	for (int j = 0; j < cols; j++) {
		matrix[rows - 1][j] = temp[j];
	}

	free(temp);
}

int get_avarage(int *row , int  count ) {
	int sum = 0;
	for (int i = 0; i < count; i++) {
		sum += row[i];
	
	
	}
	return  sum / count;



}