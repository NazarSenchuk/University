#pragma once
#ifndef HEADERF_H
#define HEADERF_H

typedef struct {
	int hours;
	int minutes;
	int seconds;
}TIME ; 

typedef struct TreeNode {

	TIME time;
	char time_str[20];

	struct TreeNode* parent;
	struct TreeNode* right;
	struct TreeNode* left;
} TN ; 


typedef struct StackNode {
	int depth;
	TN* node;
	struct Stacknode* next;
}StackNode;

void print_longest_path_iterative(TN* root);
void simetrical_print(TN* root, int depth);
void ShowLevels(void);
void ShowTree(TN* proot , int level);
TN* insertNode(char* time_str);
int time_to_seconds(const TIME* t);
void delete_tree(TN* root);
int get_time_of_tree();
int height_of_tree(TN* root);

void push(StackNode** top, TN* node, int depth);
TN* pop(StackNode** top, int* depth);
int is_empty(StackNode* top);
#endif