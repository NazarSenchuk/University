
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <math.h>
typedef struct {
	int x;
	int y;
} point;

float length(point* a) {
	return sqrt(a->x * a->x + a->y * a->y);
}

void sort(point* massive[20] , int* equal , int* asign){
	for (int i = 0; i < 20; i++) {
		*equal += 1;
		int max = i;
		*asign += 1;
		for (int b = i + 1; b < 20; b++) {
			*equal += 1;
			if (length(massive[max]) < length(massive[b])) {
				*equal += 1; 
				max = b;
				*asign += 1; 
			}
		}
		point *temp = massive[i] ;
		massive[i] = massive[max];
		massive[max] = temp;
		*asign += 3;
	}

}


void merge(point* arr[], int left, int mid, int right) {
    int i, j, k; 
    int n1 = mid - left + 1;    // індекс лівого массиву
    int n2 = right - mid;       // індекс правого массиву 

    point** L = (point**)malloc(n1 * sizeof(point*));   // виділення  пам'яті для двох масивів
    point** R = (point**)malloc(n2 * sizeof(point*));  
    for (i = 0; i < n1; i++)        //   заповнення 
        L[i] = arr[left + i];
    for (j = 0; j < n2; j++)
        R[j] = arr[mid + 1 + j];

    i = 0;
    j = 0;
    k = left;

    while (i < n1 && j < n2) {   // зливаємо два масив і сортуємо
        if (length(L[i]) >= length(R[j])) {
            arr[k] = L[i];
            i++;
        }
        else {
            arr[k] = R[j];
            j++;
        }
        k++;
    }
    while (i < n1) {  // добавляємо до злитого залишки  з лівого та правого
        arr[k] = L[i];
        i++;
        k++;
    }
    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
    free(L);
    free(R);
}

void file_with_rand() {
    FILE* file = fopen("D://random.bin" , "wb+");
    
    for (int i = 0; i < 50000;  i++) {
        double rand_num = (((double)rand() / RAND_MAX) * 85.0 - 30.0);
        fwrite( &rand_num , sizeof(double), 1, file );

    }

    fclose(file);




}
void read_rand_from_file( double *rand_nums) {

    FILE* file = fopen("D://random.bin", "rb+");
    for (int i = 0; i < 50000; i++) {
        
        
        fread(&rand_nums[i], sizeof(double ) , 1 ,file);
        

    }

    
    fclose(file);

    
}
void mergeSort(point* arr[], int left, int right) {
    if (left < right) {
        int mid = left + (right - left) / 2;
        mergeSort(arr, left, mid);                 // рекурсивно розділяємо а потіи зливаємо 
        mergeSort(arr, mid + 1, right);
        merge(arr, left, mid, right);
    }
}   

void sort_2(double* massive, int* equal, int* asign , int size) {
    for (int i = 0; i < size; i++) {
        *equal += 1;
        int max = i;
        *asign += 1;
        for (int b = i + 1; b < size; b++) {
            *equal += 1;
            if (massive[max] < massive[b]) {
                *equal += 1;
                max = b;
                *asign += 1;
            }
        }
        double temp = massive[i];
        massive[i] = massive[max];
        massive[max] = temp;
        *asign += 3;
    }

}


void merge_2(double* arr, int left, int mid, int right ,int* equal , int* asign) {
    int i, j, k;
    int n1 = mid - left + 1;    // індекс лівого массиву
    int n2 = right - mid;       // індекс правого массиву 

    double* L = (double*)malloc(n1 * sizeof(double));   // виділення  пам'яті для двох масивів
    double* R = (double*)malloc(n2 * sizeof(double));
    for (i = 0; i < n1; i++)        //   заповнення 
        L[i] = arr[left + i];
    for (j = 0; j < n2; j++)
        R[j] = arr[mid + 1 + j];

    i = 0;
    j = 0;
    k = left;

    while (i < n1 && j < n2) {   // зливаємо два масив і сортуємо
        if (L[i] >= R[j]) {
            *equal += 1; 
            arr[k] = L[i]; 
            *asign += 1;
            i++;
        }
        else {
            arr[k] = R[j];
            *asign += 1; 
            j++;
        }
        k++;
    }
    while (i < n1) {  // добавляємо до злитого залишки  з лівого та правого
        arr[k] = L[i];
        i++;
        k++;
    }
    while (j < n2) {
        arr[k] = R[j];
        j++;
        k++;
    }
    free(L);
    free(R);
}

void mergeSort_2(double* arr, int left, int right , int* equal , int* asign) {
    if (left < right) {
        *equal += 1; 
        int mid = left + (right - left) / 2;
        *asign += 1; 
        mergeSort_2(arr, left, mid , equal , asign);                 // рекурсивно розділяємо а потіи зливаємо 
        mergeSort_2(arr, mid + 1, right , equal , asign);
        merge_2(arr, left, mid, right , equal , asign);
    }
}


void sorted(double* massive ) {
    for (int i = 0; i < 50000-1; i++) {
        if (massive[i] < massive[i + 1]) {
            printf("%f  %f", massive[i], massive[i + 1]);
            printf("Невідсортований\n");
            return 0;
        }
    }

    printf("Відсортований\n");

}
void sort_small_big_1(double* rand_nums ) {
    printf("Сортування  обміном\n");
    double small_massive[20];
    for (int i = 0; i < 20; i++) {
        small_massive[i] = (((double)rand() / RAND_MAX) * 85.0 - 30.0);
        printf("%f\n", small_massive[i]);
    }


    printf("Сортування масиву малого розміру(20 елементів):\n");
    clock_t start_1 = clock();
    int equal_1 = 0, asign_1 = 0;
    sort_2(small_massive, &equal_1, &asign_1, 20);

    clock_t end_1 = clock();
    double time_spent_1 = (double)(end_1 - start_1) / CLOCKS_PER_SEC;
    printf("Сортувалось %f секунд\n", time_spent_1);
    printf("Виконано %d порівнянь та %d обмінів\n", equal_1, asign_1);
    for (int i = 0; i < 20; i++) {
        printf("%f\n", small_massive[i]);
    }


    printf("Сортування масиву великого розміру(50000 елементів):\n");
    printf("Массив ");
    sorted(rand_nums);


    printf("Сортування..\n");
    clock_t start_2 = clock();
    int equal_2 =0 , asign_2 = 0;
    sort_2(rand_nums, &equal_2, &asign_2, 50000);
    clock_t end_2 = clock();
    double time_spent_2 = (double)(end_2 - start_2) / CLOCKS_PER_SEC;
    printf("Сортувалось %f секунд\n", time_spent_2);
    printf("Виконано %d порівнянь та %d обмінів\n", equal_2, asign_2);
    printf("Масив ");
    sorted(rand_nums);

}

void sort_small_big_2(double* rand_nums) {
    printf("Сортування  злиттям\n");
    double small_massive[20];
    int equal_1 =0 , asign_1 = 0;
    for (int i = 0; i < 20; i++) {
        small_massive[i] = (((double)rand() / RAND_MAX) * 85.0 - 30.0);
        printf("%f\n", small_massive[i]);
    }

    clock_t start_1 = clock();

 

   
    printf("Сортування масиву малого розміру(20 елементів):\n");
    mergeSort_2(small_massive , 0 , 20-1 , &equal_1 , &asign_1);
    clock_t end_1 = clock();
    double time_spent_1 = (double)(end_1 - start_1) / CLOCKS_PER_SEC;
    printf("Сортувалось %f секунд\n", time_spent_1);
    printf("Виконано %d порівнянь та %d обмінів\n", equal_1, asign_1);
    for (int i = 0; i < 20; i++) {
        printf("%f\n", small_massive[i]);
    }


    printf("Сортування масиву великого розміру(50000 елементів):\n");
    printf("Массив ");
    sorted(rand_nums);

    int equal_2 = 0, asign_2 = 0;
    printf("Сортування..\n");
    clock_t start_2 = clock();
    mergeSort_2(rand_nums, 0, 50000-1 ,&equal_2 , &asign_2  );
    clock_t end_2 = clock();
    double time_spent_2 = (double)(end_2 - start_2) / CLOCKS_PER_SEC;
    printf("Сортувалось %f секунд\n", time_spent_2);
    printf("Виконано %d порівнянь та %d обмінів\n", equal_2, asign_2);
    printf("Масив ");
    sorted(rand_nums);

}


int main() {
	srand(time(0));
    double* rand_nums = (double*)malloc(50000 * sizeof(double));
    read_rand_from_file(rand_nums);

    sort_small_big_2(rand_nums);
    

    free(rand_nums);

    
	

}