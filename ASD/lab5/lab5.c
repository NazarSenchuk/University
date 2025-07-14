#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <ctype.h>
#include <math.h>
#define MAX_RECORDS 100
int count = 0;
char filename[100]; 


typedef struct {
    unsigned int id;
    time_t created;
    char number_16[100];
    char number_2[100];
    char number_10[100];
} NumberRecord;


NumberRecord database[MAX_RECORDS];

void convert_16_to_10(char number[100], char output[100]) {
    int sum = 0;
    int rozrad = strlen(number) - 1;

    for (int i = 0; i < strlen(number); i++) {
        if (isdigit(number[i])) {
            sum += (number[i] - '0') * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'A') {
            sum += 10 * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'B') {
            sum += 11 * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'C') {
            sum += 12 * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'D') {
            sum += 13 * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'E') {
            sum += 14 * pow(16, rozrad);
        }
        else if (toupper(number[i]) == 'F') {
            sum += 15 * pow(16, rozrad);
        }
        rozrad -= 1;
    }
    sprintf(output, "%d", sum);
}

void convert_10_to_2(char number[100], char output[100]) {
    char decimal[100];
    convert_16_to_10(number, decimal);
    int num = atoi(decimal);

    if (num == 0) {
        strcpy(output, "0");
        return;
    }

    int i = 0;
    char temp[100];
    while (num > 0) {
        temp[i++] = (num % 2) + '0';
        num = num / 2;
    }

    // Reverse the string
    int j = 0;
    for (int k = i - 1; k >= 0; k--) {
        output[j++] = temp[k];
    }
    output[j] = '\0';
}


int valid_16(char number[100]) {
    char allowed[] = "01234567890ABCDEF";
    
    for (int i=0; number[i] != '\0'; i++) {
        if (strchr( allowed, number[i]) == NULL) {
            return 0;
        
        }
        
        }
    return 1;
         

}


void db_add_record( NumberRecord *record ) {
    
    
    database[count] = *record;
    count+=1;
    printf("Запис Додано\n");

}



void db_delete_record(  ) {

    int id; 
    printf("Введіть ід запису який хочете видалити:");
    scanf("%d", &id);
    for (int i = id-1 ; i< count - 1 ; i++) {

        database[i] = database[i + 1];
    
    
    }
    
    count -= 1;
    printf("Запис видалено\n");
}

void db_print_records() {
    printf("Список записів \n");
    for (int i = 0; i < count; i++) {
        struct tm* created = localtime( &(database[i].created) );
        
        printf("Індетифікатор: %d  Створено: %d.%d.%d  Число_16: %s Число_10: %s  Число_2: %s \n", database[i].id, created->tm_year+1900, created->tm_mon, created->tm_mday, database[i].number_16, database[i].number_10, database[i].number_2) ;



    }
}


NumberRecord* input_record() {
    NumberRecord new_record;
    printf("Введіть число:");
    char temp_number[100];
    
    do {
        printf("Введіть коректне число 16-вої системи числення:");
        scanf("%s", temp_number);
        rewind(stdin);
    } while (valid_16(temp_number) == 0);
    strcpy(new_record.number_16, temp_number);
    convert_16_to_10(new_record.number_16 , new_record.number_10);
    convert_10_to_2(new_record.number_10, new_record.number_2);
    

    time( &(new_record.created));
    new_record.id = count + 1;
    return &new_record;

}

void print_menu() {
    printf("\nМеню:\n");
    printf("1. Додати новий запис\n");
    printf("2. Роздрукувати всі дані\n");
    printf("3. Пошук даних\n");
    printf("4. Видалити запис\n");
    printf("5. Відсортувати записи\n");
    printf("6. Зберегти дані у файл\n");
    printf("0. Вихід\n");
}

int search_records() {
    int id;
    printf("Введи ід запису якого хочеш знайти:");
    scanf("%d", &id);
    rewind(stdin);
    for (int i = 0; i < count; i++) {
        if (database[i].id = id) {
            printf("Запис з таким індетифікатором існує\n");
            return 1;
        }
    
    }
    printf("Запису з таким індетифікатором неіснує\n");
    return 0;
    
};
void sort_records() {
    for (int i = 0; i < count - 1; i++) {
        int min_index = i;
        for (int j = i + 1; j < count; j++) {
            if ( atoi(database[j].number_10) < atoi(   database[min_index].number_10)) {
                min_index = j;
            }
        }

        if (min_index != i) {
            NumberRecord temp = database[i];
            database[i] = database[min_index];
            database[min_index] = temp;
        }
    }

    printf("Записи успішно відсортовані за зростанням десяткових значень.\n");


};

void load_from_file() {
    FILE* file = fopen(filename, "rb+");
    if (file == NULL) {
        printf("Файл бази даних не знайдено. Буде створено нову базу даних.\n");
        return;
    }

    fread(&count, sizeof(int), 1, file);
    fread(database, sizeof(NumberRecord), count, file);

    fclose(file);
    printf("Дані успішно завантажено з файлу '%s'.\n", filename);
}

void save_to_file() {
    

    FILE* file = fopen(filename, "wb+");
    if (file == NULL) {
        printf("Помилка відкриття файлу для запису.\n");
        return;
    }

    fwrite(&count, sizeof(int), 1, file);
    fwrite(database, sizeof(NumberRecord), count, file);

    fclose(file);
    printf("Дані успішно збережено у файл '%s'.\n", filename);
}

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
        case 0: printf("Вихід з програми...\n"); break;
        default: printf("Невірний вибір. Спробуйте ще раз.\n");
        }
    } while (choice != 0);

    return 0;
    
  
}