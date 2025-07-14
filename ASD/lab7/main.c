#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include "headerf.h"


int **matrix ;
int rows;
int cols;
char *file_name[100]; 

int main() {
	
	printf("1. Файл\n2. Клавіатура\n");
	int choice;
	printf("Виберіть тип введення:");
	while (scanf("%d" , &choice )!= 1) {
		rewind(stdin);
		printf("Виберіть коректний тип введення:\n");
	}
	rewind(stdin);
	if (choice == 1) {
		init_matrix_from_file();
	}
	else if (choice == 2) {
		init_matrix_from_keyboard();
	}

    
	print_matrix();

	zsyv(smallest_avarage());
	print_matrix();



	zsyv_na_1(smallest_avarage());
	print_matrix();
	save_to_file();
	return 0;

}