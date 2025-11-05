import math
import matplotlib.pyplot as plt
import numpy as np

def f(x):
    return x**3 - x**2 - 4*x + 4 - 9*math.sin(x)

def df(x):
    return 3*x**2 - 2*x - 4 - 9*math.cos(x)

def d2f(x):
    return 6*x - 2 + 9*math.sin(x)

def check_bisection_convergence(a, b):
    """Перевірка умови збіжності методу бісекції"""
    if f(a) * f(b) > 0:
        return False, "f(a) * f(b) > 0 - не виконується умова існування кореня"
    return True, "Умова збіжності виконується"

def check_chord_convergence(a, b):
    """Перевірка умови збіжності методу хорд"""
    if f(a) * f(b) > 0:
        return False, "f(a) * f(b) > 0 - не виконується умова існування кореня"
    
    # Перевірка, що похідна не міняє знак на інтервалі
    x_test = np.linspace(a, b, 100)
    df_values = [df(x) for x in x_test]
    if min(df_values) * max(df_values) < 0:
        return False, "f'(x) міняє знак на інтервалі"
    
    # Перевірка, що друга похідна не міняє знак
    d2f_values = [d2f(x) for x in x_test]
    if min(d2f_values) * max(d2f_values) < 0:
        return False, "f''(x) міняє знак на інтервалі"
    
    return True, "Умова збіжності виконується"

def check_newton_convergence(x0, interval=None):
    """Перевірка умови збіжності методу Ньютона"""
    # Перевірка, що f'(x0) ≠ 0
    if abs(df(x0)) < 1e-15:
        return False, f"f'({x0}) ≈ 0 - похідна близька до нуля"
    
    if interval:
        a, b = interval
        # Перевірка сталості знаку другої похідної
        x_test = np.linspace(a, b, 100)
        d2f_values = [d2f(x) for x in x_test]
        if min(d2f_values) * max(d2f_values) < 0:
            return False, "f''(x) міняє знак на інтервалі"
    
    return True, "Умова збіжності виконується"

def get_iterative_function(phi_type="chord", a=None, b=None, x0=None):
    """Побудова ітераційної функції φ(x)"""
    if phi_type == "chord" and a is not None and b is not None:
        # Для методу хорд: φ(x) = x - f(x) * (b - a) / (f(b) - f(a))
        lambda_val = (b - a) / (f(b) - f(a))
        phi = lambda x: x - f(x) * lambda_val
        phi_str = f"φ(x) = x - f(x) * {lambda_val:.6f}"
        return phi, phi_str
    
    elif phi_type == "newton":
        # Для методу Ньютона: φ(x) = x - f(x)/f'(x)
        phi = lambda x: x - f(x) / df(x)
        phi_str = "φ(x) = x - f(x)/f'(x)"
        return phi, phi_str
    
    elif phi_type == "simple_iteration" and x0 is not None:
        # Проста ітерація: φ(x) = x - λf(x), де λ = 1/f'(x0)
        lambda_val = 1 / df(x0)
        phi = lambda x: x - lambda_val * f(x)
        phi_str = f"φ(x) = x - {lambda_val:.6f} * f(x)"
        return phi, phi_str
    
    return None, "Невідомий тип ітераційної функції"

def check_iterative_convergence(phi, a, b):
    """Перевірка умови збіжності ітераційного методу"""
    # Перевірка умови |φ'(x)| < 1 на [a,b]
    x_test = np.linspace(a, b, 100)
    
    # Чисельне обчислення похідної φ'(x)
    h = 1e-8
    phi_prime_values = []
    for x in x_test:
        phi_prime = (phi(x + h) - phi(x)) / h
        phi_prime_values.append(abs(phi_prime))
    
    max_phi_prime = max(phi_prime_values)
    if max_phi_prime >= 1:
        return False, f"|φ'(x)| = {max_phi_prime:.6f} ≥ 1 - умова збіжності не виконується"
    
    return True, f"|φ'(x)| = {max_phi_prime:.6f} < 1 - умова збіжності виконується"

def bisection(a, b, eps=1e-10):
    convergence, message = check_bisection_convergence(a, b)
    if not convergence:
        return None, 0, message
    
    iterations = 0
    while (b - a) > 2 * eps:
        iterations += 1
        c = (a + b) / 2
        if f(a) * f(c) < 0:
            b = c
        else:
            a = c
    return (a + b) / 2, iterations, "Успішно зійшовся"

def chord(a, b, eps=1e-10):
    convergence, message = check_chord_convergence(a, b)
    if not convergence:
        return None, 0, message
    
    # Отримуємо ітераційну функцію
    phi, phi_str = get_iterative_function("chord", a, b)
    
    # Перевіряємо умову збіжності для ітераційної функції
    iter_convergence, iter_message = check_iterative_convergence(phi, a, b)
    
    iterations = 0
    x_prev = a
    while iterations < 10000:
        iterations += 1
        x_new = phi(x_prev)
        if abs(x_new - x_prev) < eps:
            return x_new, iterations, f"Успішно зійшовся. {iter_message}"
        x_prev = x_new
    return x_prev, iterations, f"Досягнуто максимум ітерацій. {iter_message}"

def newton(x0, eps=1e-10, interval=None):
    convergence, message = check_newton_convergence(x0, interval)
    if not convergence:
        return None, 0, message
    
    # Отримуємо ітераційну функцію
    phi, phi_str = get_iterative_function("newton")
    
    if interval:
        a, b = interval
        # Перевіряємо умову збіжності для ітераційної функції
        iter_convergence, iter_message = check_iterative_convergence(phi, a, b)
    else:
        iter_convergence, iter_message = True, "Умова збіжності не перевірялась"
    
    iterations = 0
    x_prev = x0
    while iterations < 10000:
        iterations += 1
        x_new = phi(x_prev)
        if abs(x_new - x_prev) < eps:
            return x_new, iterations, f"Успішно зійшовся. {iter_message}"
        x_prev = x_new
    return x_prev, iterations, f"Досягнуто максимум ітерацій. {iter_message}"

def simple_iteration(x0, eps=1e-10, interval=None):
    """Метод простої ітерації"""
    # Отримуємо ітераційну функцію
    phi, phi_str = get_iterative_function("simple_iteration", x0=x0)
    
    if interval:
        a, b = interval
        # Перевіряємо умову збіжності для ітераційної функції
        iter_convergence, iter_message = check_iterative_convergence(phi, a, b)
        if not iter_convergence:
            return None, 0, iter_message
    else:
        iter_convergence, iter_message = True, "Умова збіжності не перевірялась"
    
    iterations = 0
    x_prev = x0
    while iterations < 10000:
        iterations += 1
        x_new = phi(x_prev)
        if abs(x_new - x_prev) < eps:
            return x_new, iterations, f"Успішно зійшовся. {iter_message}"
        x_prev = x_new
    return x_prev, iterations, f"Досягнуто максимум ітерацій. {iter_message}"

def find_roots(start, end, step=0.1):
    roots = []
    x = start
    while x <= end:
        if f(x) * f(x + step) <= 0:
            roots.append((x, x + step))
        x += step
    return roots

# Основна програма
print("Лабораторна робота №3")
print("Рівняння: x³ - x² - 4x + 4 - 9sin(x) = 0")
print("Точність: ε = 1e-10\n")

intervals = find_roots(-3, 3)
print(f"Знайдені інтервали: {intervals}")

for i, (a, b) in enumerate(intervals):
    print(f"\n{'='*60}")
    print(f"Корінь {i+1} на [{a:.1f}, {b:.1f}]:")
    print(f"f({a:.1f}) = {f(a):.8f}, f({b:.1f}) = {f(b):.8f}")
    
    # Метод бісекції
    root_b, iter_b, msg_b = bisection(a, b)
    print(f"\nМетод бісекції:")
    print(f"  Результат: {root_b:.12f} ({iter_b} ітер.)")
    print(f"  Статус: {msg_b}")
    
    # Метод хорд
    root_c, iter_c, msg_c = chord(a, b)
    print(f"\nМетод хорд:")
    print(f"  Результат: {root_c:.12f} ({iter_c} ітер.)")
    print(f"  Статус: {msg_c}")
    
    # Метод Ньютона
    x0 = (a + b) / 2
    root_n, iter_n, msg_n = newton(x0, interval=(a, b))
    print(f"\nМетод Ньютона (x0 = {x0:.2f}):")
    print(f"  Результат: {root_n:.12f} ({iter_n} ітер.)")
    print(f"  Статус: {msg_n}")
    
    # Метод простої ітерації
    root_si, iter_si, msg_si = simple_iteration(x0, interval=(a, b))
    print(f"\nМетод простої ітерації (x0 = {x0:.2f}):")
    print(f"  Результат: {root_si:.12f} ({iter_si} ітер.)")
    print(f"  Статус: {msg_si}")

print(f"\n{'='*60}")
print(f"Дослідження для [0.3, 0.4]:")
a, b = 0.3, 0.4
epsilons = [10**-i for i in range(1, 12)]

print("ε\t\tБісекція\tХорд\t\tНьютон\tПроста ітерація")
for eps in epsilons:
    _, iter_b, _ = bisection(a, b, eps)
    _, iter_c, _ = chord(a, b, eps)
    _, iter_n, _ = newton(0.35, eps, (a, b))
    _, iter_si, _ = simple_iteration(0.35, eps, (a, b))
    print(f"{eps:.0e}\t\t{iter_b}\t\t{iter_c}\t\t{iter_n}\t\t{iter_si}")

# Побудова графіка
x = np.linspace(-3, 3, 1000)
y = [f(xi) for xi in x]

plt.figure(figsize=(12, 8))
plt.plot(x, y, 'b-', linewidth=2, label='f(x) = x³ - x² - 4x + 4 - 9sin(x)')
plt.axhline(y=0, color='k', linestyle='-', alpha=0.3)

# Додаємо корені на графік
for a, b in intervals:
    root_b, _, _ = bisection(a, b)
    plt.plot(root_b, f(root_b), 'ro', markersize=8, label=f'Корінь ≈ {root_b:.2f}')

plt.grid(True)
plt.title('Графік функції f(x) = x³ - x² - 4x + 4 - 9sin(x)', fontsize=14)
plt.xlabel('x', fontsize=12)
plt.ylabel('f(x)', fontsize=12)
plt.legend()
plt.savefig('graph.png', dpi=300, bbox_inches='tight')
print("\nГрафік збережено у graph.png")