#include "header.h"
#include <stdio.h>
#include <stdlib.h>

extern TN* root;


TIME parse_string(char* string) {

	TIME new_time;

	sscanf(string, "%d:%d:%d", &new_time.hours, &new_time.minutes, &new_time.seconds);

	return new_time;
};

int compare_time(TIME* t1, TIME* t2) {
	if (t1->hours > t2->hours) {
		return 1;
	
	}
	else if (t1->hours == t2->hours) {
		if (t1->minutes > t2->minutes) {
			return 1;

		}
		else if (t1->minutes == t2->minutes) {

			if (t1->seconds > t2->seconds) {
				return 1;

			}
			else if (t1->seconds == t2->seconds) {



			}
			else {
				return 0;
			}

		}
		else {
			return 0;
		}
		
	
	}
	else {
		return 0;
	}

}

int time_to_seconds(const TIME* t) {
	return t->hours * 3600 + t->minutes * 60 + t->seconds;
}


TN* insertNode(char *time_str ) {
	TIME new_time = parse_string(time_str);  // Парсим час 
	
	TN* new_node = (TN*)malloc(sizeof(TN));  // Виділяємо пам'ять для нової  ноди
	new_node->left = NULL; 
	new_node->right = NULL;
	new_node->time = new_time;		  // Привовуємо час  новій ноді
	strcpy(new_node->time_str , time_str);

	if (root == NULL) {
		root = new_node;   //Якщо  ще  корінь не ініціалізований ставимо нову ноду як  корінь
		return root;
	}

	TN* current = root;
	TN* parent = NULL;  
	while (current != NULL) {  // шукаємо підходящого батька
		parent = current;
		if (compare_time(&(current->time), &new_time)) {
			current = current->left;
		
		}else {
			current = current->right;
		}
	}


	new_node->parent = parent;  //Ставимо  нову ноду в право або в ліво для підходящого батька
	if ( compare_time( &(parent->time ) ,  &new_time) ) {
		parent->left = new_node;
	
	}else {
		parent->right = new_node;
	}
	return root;

}




void simetrical_print(TN* root, int depth ) { // Виводимо дерево симетричним обходом  рекурсивно
	if (root == NULL) return;

	simetrical_print(root->right, depth+1);
	
	printf("%s\n", root->time_str);


	simetrical_print(root->left , depth +1 );
}






void ShowTree(TN* proot, int level)
{
	if (proot == NULL) return;
	ShowTree(proot->right, level + 1); // відображення правого піддерева
	printf("%*c%s\n", level * 10, ' ', proot->time_str); // корінь
	ShowTree(proot->left, level + 1); // відображення лівого піддерева
}


void ShowTree_iterative(TN* proot, int level) {
	if (proot == NULL) return;

	StackNode* stack = NULL;
	TN* current = proot;
	int current_level = level;

	while (1) {
		// додаємо всі праві вузли в стек
		while (current != NULL) {
			push(&stack, current, current_level);
			current = current->right;
			current_level++;
		}

		// якщо стек порожній завершуємо
		if (is_empty(stack)) break;

		// вилучаємо вузол зі стеку
		current = pop(&stack, &current_level);

		// виводимо поточний вузол
		printf("%*c%s\n", current_level * 10, ' ', current->time_str);

		// переходимо до лівого піддерева
		current = current->left;
		current_level++;
	}
}


void ShowLevels(void)
{
	int lev, height = height_of_tree(root);
	for (lev = 1; lev <= height; lev++)
		printf("%-*d", 10, lev); // друк номерів рівнів
	printf("\n\n");
}


void delete_tree(TN* root)
{
	TN* pleft, * pright; // допоміжні вказівники
	if (root == NULL) // дерево порожнє
		return;
	pleft = root->left; pright = root->right; // адреси нащадків
	
	delete_tree(pleft); // витирання лівого піддерева  Витирається  самий лівий і піднімається догори
	delete_tree(pright); // витирання правого піддерева
	free(root); // видалення  root вузла
}



int get_time_of_tree() {

	TN* current_left = root;
	while (current_left->left != NULL) {
		current_left = current_left->left;
	}

	TN* current_right = root;
	while (current_left->right != NULL) {
		current_right = current_right->right;
	}

	printf("%s - %s " , current_left->time_str , current_right->time_str);
	printf("  Різниця часу: %d", time_to_seconds(&(current_right->time)) - time_to_seconds(&(current_left->time)));
}

int height_of_tree( TN* root)
{
	int lh, rh;
	if (root == NULL) return 0;
	lh = height_of_tree(root->left); // висота лівого піддерева
	rh = height_of_tree(root->right); // висота правого піддерева
	return 1 + (lh > rh ? lh : rh); // +1 – враховує корінь
}


void push(StackNode** top, TN* node, int depth) {
	StackNode* new_node = (StackNode*)malloc(sizeof(StackNode));
	new_node->node = node;
	new_node->depth = depth;
	new_node->next = *top;
	*top = new_node;

}

TN* pop(StackNode** top, int* depth) {
	if (*top == NULL)
		return NULL;
	
	StackNode* temp  = *top;
	TN* node = temp->node;
	*depth = temp->depth;
	*top = temp->next;
	free(temp);
	return node;

}



int is_empty(StackNode* top) {
	return top == NULL;
}

void print_longest_path_iterative(TN* root) {
	if (root == NULL) {
		printf("Шляху немає");
		return;
	}

	StackNode* stack = NULL;  // створюємо стек
	push(&stack, root, 1);    // добавляємо в стек першу ноду

	TN** longest_path = NULL;   // ініціалізуємо посилання на ноди найдовшого шляху
	TN** current_path = (TN**)malloc(sizeof(TN*) * 100);   //  ініціалізуємо  посилання на ноди шляху з яким будем працювати
	
	
	int max_depth =  0 ;   // максимальна глибина
	while (!is_empty(stack)) {
		int current_depth;       // Якщо  стек  порожній то цикл закінчується
		TN* node = pop(&stack, &current_depth);  // Дістаємо з  стеку ноду
		
		current_path[current_depth - 1] = node;  // Вставляємо посилання на ноду в список шляху який зараз ітеруємо

		if (node->right == NULL && node->left == NULL) { //Якщо нода листок то звіряємо глибину теперішнього шляху з найдовшим
			if (current_depth > max_depth) {  
				max_depth = current_depth;    // Якщо  глибина більша то  мінаємо максимальну глибину та змінюємо  список  найдовшого шляху
				longest_path = (TN**)realloc(longest_path,sizeof(TN*) * max_depth);
				for (int i = 0; i < max_depth; i++) {
					longest_path[i] = current_path[i];
				}
				
			}
		
		
		
		}

		if (node->right != NULL) {		//Добавляємо в стек лівого та правого сина 
			push(&stack, node->right, current_depth + 1);

		}
		if (node->left != NULL) {
			push(&stack, node->left, current_depth + 1);
		}
	
	
	
	}

	if (max_depth > 0) {
		printf("%d\n", max_depth);
		for (int i = 0; i < max_depth; i++) {
			printf("%s", longest_path[i]->time_str);
			if (i < max_depth - 1) printf(" -> ");
		}
		printf("\n");
	}
	else {
		printf("No path found.\n");
	}

	free(current_path);
	free(longest_path);
}