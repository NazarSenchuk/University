#pragma once
#ifndef HEADERF_H
#define HEADERF_H




typedef struct {
    unsigned int id;
    char name[100];
    int level;
    int health;
    float power;

} Character;


void db_add_record(Character* c);
void db_delete_record(void);
void db_print_records(void);
Character* input_record(void);
void print_menu(void);
int search_records(void);
void sort_records(void);
void load_from_file(void);
void save_to_file(void);


#endif