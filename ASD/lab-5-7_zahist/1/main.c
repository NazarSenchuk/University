#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <ctype.h>
#include <math.h>
#include "headerf.h"
int count = 0;
char filename[100];




Character* database = NULL;



int main() {
    printf("Введіть назву файла наявної бази даних(якщо  такої нема то створиться автоматично):");
    gets(filename);

    load_from_file();
    int choice;
    do {
        print_menu();
        printf("Виберіть опцію: ");
        scanf("%d", &choice);
        rewind(stdin);

        switch (choice) {
        case 1: db_add_record(input_record()); break;
        case 2: db_print_records(); break;
        case 3: search_records(); break;
        case 4: db_delete_record(); break;
        case 5: sort_records(); break;
        case 6: save_to_file(); break;
        case 0: printf("Вихід з програми...\n"); free(database ) ; break;
        default: printf("Невірний вибір. Спробуйте ще раз.\n");
        }
    } while (choice != 0);

    return 0;


}