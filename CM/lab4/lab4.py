from fractions import Fraction
import numpy as np

group = "OI-25"
student_name = "Сенчук Назарій Михайлович"
lab_number = 4
variant = 11
print(f"Лабораторна робота №{lab_number}")
print(f"Тема: Розв’язування СЛАР методами Гауса, Жордана–Гауса та простої ітерації (Якобі)")
print(f"Група: {group}")
print(f"ПІБ студента: {student_name}")
print(f"Варіант завдання: {variant}\n")

A_int = [
    [-8, 7, -10, -7, -9],
    [0, 6, 20, -18, 20],
    [-2, -2, 6, -9, 6],
    [-1, -17, 0, -6, 24],
    [5, 5, 15, -11, 2]
]

b_int = [-8, 11, 18, 20, 16]

# Вивід початкової матриці
print("Початкова система рівнянь (A | b):\n")
for i in range(len(A_int)):
    row = "  ".join(f"{A_int[i][j]:>6}" for j in range(len(A_int[i])))
    print(f"[ {row} | {b_int[i]:>6} ]")
print("\n")

A = [[Fraction(a) for a in row] for row in A_int]
b = [Fraction(bb) for bb in b_int]

def gauss_solve(A, b):
    n = len(A)
    M = [A[i][:] + [b[i]] for i in range(n)]
    for k in range(n):
        pivot = max(range(k, n), key=lambda i: abs(M[i][k]))
        if M[pivot][k] == 0:
            raise ValueError("Система вироджена або має нескінченну кількість розв’язків.")
        M[k], M[pivot] = M[pivot], M[k]
        for i in range(k + 1, n):
            factor = M[i][k] / M[k][k]
            for j in range(k, n + 1):
                M[i][j] -= factor * M[k][j]
    x = [Fraction(0) for _ in range(n)]
    for i in reversed(range(n)):
        s = sum(M[i][j] * x[j] for j in range(i + 1, n))
        x[i] = (M[i][n] - s) / M[i][i]
    return x

def jordan_gauss(A, b):
    n = len(A)
    M = [A[i][:] + [b[i]] for i in range(n)]
    for k in range(n):
        pivot = M[k][k]
        if pivot == 0:
            raise ValueError("Неможливо знайти ненульовий головний елемент.")
        M[k] = [elem / pivot for elem in M[k]]
        for i in range(n):
            if i != k:
                factor = M[i][k]
                M[i] = [M[i][j] - factor * M[k][j] for j in range(n + 1)]
    return [M[i][-1] for i in range(n)]

def residual_norm_2(A_num, b_num, x_num):
    A_f = np.array(A_num, dtype=float)
    b_f = np.array(b_num, dtype=float)
    x_f = np.array([float(xx) for xx in x_num], dtype=float)
    r = A_f.dot(x_f) - b_f
    return float(np.linalg.norm(r, 2))

def true_error_norm_2(x_num, x_star):
    x_f = np.array([float(xx) for xx in x_num], dtype=float)
    x_star_f = np.array([float(xx) for xx in x_star], dtype=float)
    return float(np.linalg.norm(x_f - x_star_f, 2))

def jacobi_simple_iteration(A_num, b_num, x0=None, tol=1e-10, max_iter=50000, blowup=1e12):
    A = np.array(A_num, dtype=float)
    b = np.array(b_num, dtype=float)
    n = len(b)
    D = np.diag(np.diag(A))
    if np.any(np.diag(D) == 0):
        raise ValueError("Нуль на діагоналі: Якобі неможливий без перестановки/масштабування.")
    L = np.tril(A, -1)
    U = np.triu(A, 1)
    D_inv = np.linalg.inv(D)
    B = -D_inv @ (L + U)
    c = D_inv @ b
    eigvals = np.linalg.eigvals(B)
    rhoB = float(np.max(np.abs(eigvals)))
    x = np.zeros(n) if x0 is None else np.array(x0, dtype=float)
    converged = False
    for it in range(1, max_iter + 1):
        x_new = B @ x + c
        rn = float(np.linalg.norm(A @ x_new - b, 2))
        if np.isnan(rn) or rn > blowup:
            return x_new, it, False, rhoB
        if rn < tol:
            converged = True
            x = x_new
            break
        if np.linalg.norm(x_new - x, np.inf) < tol:
            converged = True
            x = x_new
            break
        x = x_new
    return x, it, converged, rhoB

def substitution_error(A, b, x):
    A_f = np.array(A, dtype=float)
    b_f = np.array(b, dtype=float)
    x_f = np.array([float(xx) for xx in x], dtype=float)
    residual = A_f.dot(x_f) - b_f
    print("\nПеревірка підстановкою (Ax - b):")
    for i, r in enumerate(residual, start=1):
        print(f"   Рівняння {i}: похибка = {r:.4e}")
    print(f"   ||Ax - b||₂ ≈ {np.linalg.norm(residual, 2):.4e}\n")

x_gauss = gauss_solve(A, b)
x_jordan = jordan_gauss(A, b)
x_star = x_gauss

print("1) Метод Гауса:")
for i, xi in enumerate(x_gauss, start=1):
    print(f"   x{i} = {float(xi):.10f}  (точно: {xi})")
print(f"   ||Ax - b||₂ ≈ {residual_norm_2(A_int, b_int, x_gauss):.3e}")
print(f"   ||x - x*||₂ ≈ {true_error_norm_2(x_gauss, x_star):.3e}")
substitution_error(A_int, b_int, x_gauss)

print("2) Метод Жордана–Гауса:")
for i, xi in enumerate(x_jordan, start=1):
    print(f"   x{i} = {float(xi):.10f}  (точно: {xi})")
print(f"   ||Ax - b||₂ ≈ {residual_norm_2(A_int, b_int, x_jordan):.3e}")
print(f"   ||x - x*||₂ ≈ {true_error_norm_2(x_jordan, x_star):.3e}")
substitution_error(A_int, b_int, x_jordan)

x_jacobi, iters, ok, rhoB = jacobi_simple_iteration(A_int, b_int, tol=1e-12, max_iter=10000)

print("3) Метод простої ітерації (Якобі):")
print(f"   Спектральний радіус ρ(B) ≈ {rhoB:.4f}")
if ok:
    for i, xi in enumerate(x_jacobi, start=1):
        print(f"   x{i} ≈ {xi:.10f}")
else:
    print("   Метод не збігся (ρ(B) ≥ 1 або розбігання).")
    for i, xi in enumerate(x_jacobi, start=1):
        print(f"   x{i} (ост.) ≈ {xi:.10f}")
print(f"   Ітерацій виконано: {iters}")
print(f"   ||Ax - b||₂ ≈ {residual_norm_2(A_int, b_int, x_jacobi):.3e}")
print(f"   ||x - x*||₂ ≈ {true_error_norm_2(x_jacobi, x_star):.3e}")
substitution_error(A_int, b_int, x_jacobi)
