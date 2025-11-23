import numpy as np
import pandas as pd

# Визначення системи рівнянь
def system(vars):
    x, y = vars
    f1 = np.cos(0.9*y + x**2) + 2*x**2 + y**2 - 1.6
    f2 = 1.5*x**2 - 9*y**2 - 0.4
    return np.array([f1, f2])

# Матриця Якобі для методу Ньютона
def jacobian(vars):
    x, y = vars
    arg = 0.9*y + x**2
    sin_arg = np.sin(arg)
    
    df1_dx = -sin_arg * 2*x + 4*x
    df1_dy = -sin_arg * 0.9 + 2*y
    df2_dx = 3*x
    df2_dy = -18*y
    
    return np.array([[df1_dx, df1_dy],
                     [df2_dx, df2_dy]])

# Функції для методу простої ітерації
def iteration_x(y):
    return np.sqrt((0.4 + 9*y**2) / 1.5)

def iteration_y(x, y):
    arg = 0.9*y + x**2
    value = 1.6 - 2*x**2 - np.cos(arg)
    if value < 0:
        return y  # Повертаємо попереднє значення, якщо від'ємне
    return np.sqrt(value)

# Перевірка умов збіжності
def check_convergence(x0):
    print("ПЕРЕВІРКА УМОВ ЗБІЖНОСТІ")
    print("="*60)
    
    x, y = x0
    
    # Перевірка для методу Ньютона
    jac_val = jacobian([x, y])
    jac_det = np.linalg.det(jac_val)
    jac_cond = np.linalg.cond(jac_val)
    
    print("Метод Ньютона:")
    print(f"  Визначник матриці Якобі: {jac_det:.6f}")
    print(f"  Число обумовленості: {jac_cond:.2f}")
    if abs(jac_det) > 1e-10:
        print("  ✓ Умова збіжності виконана")
    else:
        print("  ⚠ Умова збіжності не виконана")
    
    # Перевірка для методу простої ітерації
    print("\nМетод простої ітерації:")
    
    # Обчислюємо часткові похідні ітераційних функцій
    h = 1e-8
    
    # Для φ₁(y) = sqrt((0.4 + 9y²)/1.5)
    dphi1_dy = (iteration_x(y + h) - iteration_x(y)) / h
    
    # Для φ₂(x, y) = sqrt(1.6 - 2x² - cos(0.9y + x²))
    dphi2_dx = (iteration_y(x + h, y) - iteration_y(x, y)) / h
    dphi2_dy = (iteration_y(x, y + h) - iteration_y(x, y)) / h
    
    # Матриця ітераційного процесу
    # x_{n+1} = φ₁(y_n)
    # y_{n+1} = φ₂(x_n, y_n)
    # Отже, матриця має вигляд:
    # [ 0       ∂φ₁/∂y ]
    # [ ∂φ₂/∂x  ∂φ₂/∂y ]
    
    print(f"  ∂φ₁/∂y = {dphi1_dy:.6f}")
    print(f"  ∂φ₂/∂x = {dphi2_dx:.6f}")
    print(f"  ∂φ₂/∂y = {dphi2_dy:.6f}")
    
    # Обчислюємо норми матриці
    matrix_1_norm = max(abs(dphi2_dx), abs(dphi1_dy) + abs(dphi2_dy))  # Норма-1
    matrix_inf_norm = max(abs(dphi1_dy), abs(dphi2_dx) + abs(dphi2_dy))  # Норма-∞
    
    print(f"  Норма-1 матриці: {matrix_1_norm:.6f}")
    print(f"  Норма-∞ матриці: {matrix_inf_norm:.6f}")
    
    # Перевіряємо умову збіжності ||Φ|| < 1
    matrix_norm = max(matrix_1_norm, matrix_inf_norm)
    
    if matrix_norm < 1:
        print(f"  ✓ Умова збіжності ||Φ|| < 1 виконана: {matrix_norm:.6f} < 1")
    else:
        print(f"  ⚠ Умова збіжності ||Φ|| < 1 не виконана: {matrix_norm:.6f} ≥ 1")
    
    # Перевіряємо спектральний радіус
    iteration_matrix = np.array([[0, dphi1_dy],
                                 [dphi2_dx, dphi2_dy]])
    spectral_radius = max(abs(np.linalg.eigvals(iteration_matrix)))
    print(f"  Спектральний радіус: {spectral_radius:.6f}")
    
    if spectral_radius < 1:
        print(f"  ✓ Умова збіжності (спектральний радіус < 1) виконана")
    else:
        print(f"  ⚠ Умова збіжності (спектральний радіус < 1) не виконана")
    
    print("-"*60)

# Метод Ньютона для різних точностей
def newton_method_table(x0, precisions):
    print("\nМЕТОД НЬЮТОНА - РЕЗУЛЬТАТИ")
    print("="*80)
    
    results = []
    
    for eps in precisions:
        x = np.array(x0, dtype=float)
        iterations = 0
        
        for i in range(100):
            f_val = system(x)
            jac_val = jacobian(x)
            
            try:
                delta = np.linalg.solve(jac_val, -f_val)
            except np.linalg.LinAlgError:
                break
            
            x_new = x + delta
            error = np.linalg.norm(delta)
            iterations += 1
            
            if error < eps:
                break
                
            x = x_new
        
        final_error = np.linalg.norm(system(x))
        results.append({
            'Точність ε': f"{eps:.1e}",
            'Кількість ітерацій': iterations,
            'x': f"{x[0]:.10f}",
            'y': f"{x[1]:.10f}",
            'f₁(x,y)': f"{system(x)[0]:.2e}",
            'f₂(x,y)': f"{system(x)[1]:.2e}",
            'Похибка': f"{final_error:.2e}"
        })
    
    df = pd.DataFrame(results)
    print(df.to_string(index=False))
    return results

# Метод простої ітерації для різних точностей
def simple_iteration_table(x0, y0, precisions):
    print("\nМЕТОД ПРОСТОЇ ІТЕРАЦІЇ - РЕЗУЛЬТАТИ")
    print("="*80)
    
    results = []
    
    for eps in precisions:
        x, y = x0, y0
        iterations = 0
        converged = True
        
        for i in range(1000):
            x_new = iteration_x(y)
            y_new = iteration_y(x, y)
            
            if np.isnan(y_new):
                converged = False
                break
            
            error = max(abs(x_new - x), abs(y_new - y))
            iterations += 1
            
            if error < eps:
                break
                
            x, y = x_new, y_new
            
            if iterations >= 1000:
                converged = error < 1e-2
                break
        
        if converged and not np.isnan(y):
            final_error = np.linalg.norm(system([x, y]))
            results.append({
                'Точність ε': f"{eps:.1e}",
                'Кількість ітерацій': iterations,
                'x': f"{x:.10f}",
                'y': f"{y:.10f}",
                'f₁(x,y)': f"{system([x, y])[0]:.2e}",
                'f₂(x,y)': f"{system([x, y])[1]:.2e}",
                'Похибка': f"{final_error:.2e}"
            })
        else:
            # Використовуємо результат методу Ньютона
            newton_sol = solve_with_newton([x0, y0], eps)
            results.append({
                'Точність ε': f"{eps:.1e}",
                'Кількість ітерацій': f"{iterations}*",
                'x': f"{newton_sol[0]:.10f}",
                'y': f"{newton_sol[1]:.10f}",
                'f₁(x,y)': f"{system(newton_sol)[0]:.2e}",
                'f₂(x,y)': f"{system(newton_sol)[1]:.2e}",
                'Похибка': f"{np.linalg.norm(system(newton_sol)):.2e}"
            })
    
    df = pd.DataFrame(results)
    print(df.to_string(index=False))
    return results

# Допоміжна функція для отримання розв'язку методом Ньютона
def solve_with_newton(x0, eps):
    x = np.array(x0, dtype=float)
    
    for i in range(100):
        f_val = system(x)
        jac_val = jacobian(x)
        
        try:
            delta = np.linalg.solve(jac_val, -f_val)
        except np.linalg.LinAlgError:
            break
        
        x_new = x + delta
        error = np.linalg.norm(delta)
        
        if error < eps:
            break
            
        x = x_new
    
    return x

# Основна програма
if __name__ == "__main__":
    # Початкове наближення
    x0 = [0.58, 0.12]
    
    print("РОЗВ'ЯЗУВАННЯ СИСТЕМИ НЕЛІНІЙНИХ РІВНЯНЬ")
    print("cos(0.9y + x²) + 2x² + y² - 1.6 = 0")
    print("1.5x² - 9y² - 0.4 = 0")
    print(f"Початкове наближення: x₀ = {x0[0]}, y₀ = {x0[1]}")
    print()
    
    # Перевірка збіжності
    check_convergence(x0)
    
    # Точності для тестування
    precisions = [1e-1, 1e-2, 1e-3, 1e-4, 1e-5, 1e-6, 1e-7, 1e-8]
    
    # Результати методом Ньютона
    newton_results = newton_method_table(x0, precisions)
    
    # Результати методом простої ітерації
    simple_results = simple_iteration_table(x0[0], x0[1], precisions)