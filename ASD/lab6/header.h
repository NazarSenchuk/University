#pragma once
#ifndef HEADERF_H
#define HEADERF_H // захист від повторного під’єднання

#include <time.h>
#define MAX_RECORDS 100
typedef struct {
    unsigned int id;
    time_t created;
    char number_16[100];
    char number_2[100];
    char number_10[100];
} NumberRecord;



void convert_16_to_10(char number[100], char output[100]);
void convert_10_to_2(char number[100], char output[100]);
int valid_16(char number[100]);
void db_add_record(NumberRecord* record);
void db_delete_record(void);
void db_print_records(void);
NumberRecord* input_record(void);
void print_menu(void);
int search_records(void);
void sort_records(void);
void load_from_file(void);
void save_to_file(void);

#endif