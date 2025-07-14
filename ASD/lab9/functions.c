
#include "header.h"
#include <stdio.h>
#include <stdlib.h>
#include <ctype.h>
extern const  char allowed[];
extern SL* single_list;
extern DL* double_list;
extern DL* last_double_node;
extern int  size_single;
extern int  size_double;

int init_single_list() {

	int choice;
	printf("Виберіть тип ініціалізацій:\n");
    printf("1. З клавіатури\n");
    printf("0. Вихід з програми\n");
    printf("Виберіть пункт:");

	scanf("%d", &choice);
	rewind(stdin);

    switch (choice) {
    case 1: init_from_keyboard(); break;
    case 2:  break;
    case 0: printf("Вихід з програми");  break;
    default:  printf("Виберіть ще раз:");

    }
}

void add_to_single_list(const char* string) {
    SL* new_node = (SL*)malloc(sizeof(SL));
    if (!new_node) return;

    strcpy(new_node->inform.string , string);
    new_node->inform.index = ++size_single;
    new_node->next = NULL;

    if (!single_list) {
        single_list = new_node;
    }
    else {
        SL* current = single_list;
        while (current->next) {
            current = current->next;
        }
        current->next = new_node;
    }
}

void add_to_double_list(const char* string) {
    DL* new_node = (DL*)malloc(sizeof(DL));
    if (!new_node) return;

    strcpy( new_node->inform.string  , string);
  
    new_node->inform.index = ++size_double;


    if (!double_list) {
        new_node->next = new_node;
        new_node->prev = new_node;
        double_list = new_node;
        last_double_node = new_node;
        return;
    }
    else {
      
        new_node->prev = last_double_node;
        new_node->next = double_list;
        last_double_node->next = new_node;
        last_double_node = new_node;

        
    }
    
}

void init_from_keyboard() {
    printf("Введіть кількість символьних рядків: ");
    int n = 0; 
    scanf("%d", &n);
    rewind(stdin);

    for (int i = 0; i < n; i++) {
        printf("Елемент %d: ", i);
        char string[100];
        gets(string);
         

        if (check_string(string)) {
            add_to_single_list(string);
        }
        else {
            printf("Неправильний рядок: %s\n", string);
        }
    }

    
    SL* current = single_list;
    while (current) {
        if (check_string(current->inform.string)) {
            add_to_double_list(current->inform.string);
        }
        current = current->next;
    }
}

int check_string(char* string) {
    for (int i = 0; string[i] != '\0'; i++) {
        char c = toupper(string[i]);
        if (!strchr(allowed, c)) {
            return 0; 
        }
    }

	int count = 0;
	for (int i = 0 ; string[i] != '\0' ; i++ ){
		
		if (isdigit(string[i])) {
			count += 1;
		}
	
	}
	if (count >= 7) {
		return 0;
	}
	return 1;
}


char* input_string() {
	printf("Введіть символьний рядок:");
	char* string = malloc(100 * sizeof(char));
	gets(string);
	

	return string;

}
void free_lists() {
    SL* sl_current = single_list;
    while (sl_current != NULL) {
        SL* next = sl_current->next;
        free(sl_current);
        sl_current = next;
    }
    single_list = NULL; 

  
    if (double_list != NULL) {
        DL* dl_current = double_list;
        do {
            DL* next = dl_current->next;
            free(dl_current);
            dl_current = next;
        } while (dl_current != double_list);
        double_list = NULL; 
    }
}

void print_double_list(DL* head) {
    if (head == NULL) {
        printf("Список порожній\n");
        return;
    }

    printf("Двосв'язний список:\n");
    DL* current = head;
    do {
        printf("Індекс: %d, Рядок: %s\n",
            current->inform.index,
            current->inform.string);
        current = current->next;
    } while (current != head);
}   

void print_single_list(SL* head) {
    if (head == NULL) {
        printf("Список порожній\n");
        return;
    }

    printf("Односв'язний список:\n");
    SL* current = head;
    while (current != NULL) {
        printf("Індекс: %d, Рядок: %s\n",
            current->inform.index,
            current->inform.string);
        current = current->next;
    }
}


long convert_to_long(const char* string) {
    long value = 0;
    for (int i = 0; string[i] != '\0'; i++) {
        char c = toupper(string[i]);
        int digit;
        if (c >= '0' && c <= '9') {
            digit = c - '0';
        }
        else {
            digit = 10 + (c - 'A');
        }
        value = value * 20 + digit;
    }
    return value;
}


void insertion_sort_double_list(DL** head) {
    if (*head == NULL || (*head)->next == *head) {
        return;
    }
    DL* sorted = NULL;   
    DL* current = *head; 
    do {
        DL* next = current->next;

        if (sorted == NULL || convert_to_long(current->inform.string) <= convert_to_long(sorted->inform.string)) {
            current->next = sorted;
            current->prev = (sorted == NULL) ? current : sorted->prev;
            if (sorted != NULL) {
                sorted->prev->next = current;
                sorted->prev = current;
            }
            else {
                current->next = current;
                current->prev = current;
            }
            sorted = current;
        }
        else {
            DL* temp = sorted;
            while (temp->next != sorted &&
                convert_to_long(current->inform.string) > convert_to_long(temp->next->inform.string)) {
                temp = temp->next;
            }
            current->next = temp->next;
            current->prev = temp;
            temp->next->prev = current;
            temp->next = current;
        }
        current = next;
    } while (current != *head);  
    *head = sorted;
    last_double_node = sorted->prev;
}