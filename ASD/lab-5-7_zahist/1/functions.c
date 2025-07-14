
#include "headerf.h"
#include <stdio.h>
#include <string.h>
#include <time.h>
#include <stdlib.h>
#include <ctype.h>
#include <math.h>

extern int count;
extern char filename[];
extern Character* database;

void db_add_record(Character* c) {

    Character* temp = (Character*)realloc(database, (count + 1) * sizeof(Character));
    database = temp;
    database[count] = *c;
    count += 1;
    printf("Запис Додано\n");

}



void db_delete_record() {

    int id;
    printf("Введіть ід запису який хочете видалити:");
    scanf("%d", &id);
    for (int i = id - 1; i < count - 1; i++) {

        database[i] = database[i + 1];


    }

    count -= 1;
    printf("Запис видалено\n");
}

void db_print_records() {
    printf("Список записів \n");
    for (int i = 0; i < count; i++) {


        printf("Індетифікатор: %i  Назва: %s Рівень: %i Здоров'я: %i  Сила: %lg \n", database[i].id, database[i].name, database[i].level, database[i].health, database[i].power);



    }
}


Character* input_record() {
    Character* new_record = malloc(sizeof(Character));
    if (new_record == NULL) {
        return NULL;
    }

    printf("Введіть характеристики персонажа:\n");
    printf("Назва:");
    gets(new_record->name);
    printf("Рівень:");
    scanf("%d", &new_record->level);
    rewind(stdin);
    printf("Здоров'я:");
    scanf("%d", &new_record->health);
    rewind(stdin);
    printf("Сила:");
    scanf("%f", &new_record->power);
    rewind(stdin);
    new_record->id = count + 1;
    return new_record;

}

void print_menu() {
    printf("\nМеню:\n");
    printf("1. Додати персонажа\n");
    printf("2. Роздрукувати всіх персонажів\n");
    printf("3. Пошук персонажа за іменем\n");
    printf("4. Видалити персонажа\n");
    printf("5. Відсортувати за рівнем\n");
    printf("6. Зберегти дані у файл\n");
    printf("0. Вихід\n");
}

int search_records() {
    char name[100];
    printf("Введи назву персонажа якого хочеш знайти:");
    scanf("%s", &name);
    rewind(stdin);
    for (int i = 0; i < count; i++) {
        if (strstr(database[i].name, name) != NULL) {
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
            if (database[j].level < database[min_index].level) {
                min_index = j;
            }
        }

        if (min_index != i) {
            Character temp = database[i];
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
    Character* temp = (Character*)malloc(count * sizeof(Character));
    database = temp;
    fread(database, sizeof(Character), count, file);
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
    fwrite(database, sizeof(Character), count, file);

    fclose(file);
    printf("Дані успішно збережено у файл '%s'.\n", filename);
}

