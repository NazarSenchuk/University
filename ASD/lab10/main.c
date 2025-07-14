#include "header.h"
#include <stdio.h>
#include <stdlib.h>


TN* root = NULL;


int main() {
	FILE* file;
	char tmp_str[20];

	file = fopen("D://times_1.txt", "r");

	if (file == NULL) {
		printf("Файл не відкрито");
		return 0;
	}

	while (fscanf(file , "%s"  , tmp_str) == 1 ) {
		insertNode(tmp_str);
	}

	fclose(file);

	printf("\nСиметричний обхід дерева\n");
	simetrical_print(root,0);

	printf("\nСиметричний вивід дерева\n");
	ShowLevels();
	ShowTree(root ,0);
	
	printf("\nНайдовший шлях:");
	print_longest_path_iterative(root);

	printf("\nЧасовий діапазон:\n");
	get_time_of_tree();

	printf("\nДрук дерева ітеративно:\n");
	ShowTree_iterative(root , 0);

	delete_tree(root);


}
