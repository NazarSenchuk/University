#pragma once
#pragma once
#ifndef HEADERF_H
#define HEADERF_H
typedef struct inform {
	int index;
	char string[100];

} INFORM;

typedef struct single_list_elem {
	INFORM inform;
	struct single_list_elem* next;
} SL ;

typedef struct double_list_elem {
	INFORM inform;
	struct double_list_elem* next;
	struct double_list_elem* prev;
} DL;
int check_string(char* string);
void init_from_keyboard();
void add_to_double_list(const char* string);
void add_to_single_list(const char* string);
void print_single_list(SL* head);
void print_double_list(DL* head);
int init_single_list(); 
void free_lists();
char* input_string();
void insertion_sort_double_list(DL** head);
long convert_to_long(const char* string);
#endif