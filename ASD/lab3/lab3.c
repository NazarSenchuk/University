#include <stdio.h>
#include <math.h>

double func1(unsigned n, double x, ...) {
    double *p = &x;  // Починаємо з адреси першого аргументу
    double sum = 0.0;

    for (unsigned i = 1; i <= n; i++) {
        sum += pow(*p, i);
        p++;  // Переходимо до наступного аргументу
    }

    return sum;
}

int main() {
    printf("%f\n", func1(4, 2.3, 4.3, 5.4, 5.4));
    return 0;
}