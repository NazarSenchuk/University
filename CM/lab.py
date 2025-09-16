import math

group_num = 25
student_num = 11
variant = 19

x1 = float(input("Введіть x1:"))
x2 = float(input("Введіть x2:"))
x3 = float(input("Введіть x3:"))

def function(x1, x2, x3):
    part1 = 10 * x1**2
    part2 = 8 * x2**2
    part3 = 6 * x3**2
    part4 = -7 * x1 * x2
    part5 = -5 * x1
    #Арксинус
    arg_asin = x1 - x2
    if abs(arg_asin) > 1:
        arg_asin = math.copysign(1, arg_asin)
    part6 = 20 * math.asin(arg_asin)
    F = part1 +part2+part3+part4+part5+part6
    return F

def number_count(x):
   
    # Перетворюємо число в рядок
    str_x = str(x)
    
    # Цифр після коми
    if '.' in str_x:
        n = len(str_x.split('.')[1])
    else:
        n = 0
    
    # Перша цифра
    for char in str_x:
        if char not in '0.':
            a_m = int(char)
            break
    else:
        a_m = 0  # якщо всі цифри нулі
    
    return n, a_m

#Підставляємо в функцію
func_value = function(x1,x2,x3)

# Часткові похідні
h = 1e-6  

x1_pohidna = function(x1 + h, x2, x3)
dF_dx1 = (x1_pohidna- func_value) / h

x2_pohidna = function(x1, x2 + h, x3)
dF_dx2 = (x2_pohidna - func_value) / h

x3_pohidna = function(x1, x2, x3 + h)
dF_dx3 = (x3_pohidna- func_value) / h


n1, a_m_x1 = number_count(x1)
n2, a_m_x2 = number_count(x2)
n3, a_m_x3 = number_count(x3)

print(f"x1 = {x1}: n={n1}, a_m={a_m_x1}")
print(f"x2 = {x2}: n={n2}, a_m={a_m_x2}") 
print(f"x3 = {x3}: n={n3}, a_m={a_m_x3}")

# Граничні відносні похибки
delta_x1_gr = 1 / (2 * a_m_x1 * 10**(n1-1)) if n1 > 0 else 0
delta_x2_gr = 1 / (2 * a_m_x2 * 10**(n2-1)) if n2 > 0 else 0
delta_x3_gr = 1 / (2 * a_m_x3 * 10**(n3-1)) if n3 > 0 else 0

print(f"\nГраничні відносні похибки:")
print(f"δx1_gr = {delta_x1_gr}")
print(f"δx2_gr = {delta_x2_gr}")
print(f"δx3_gr = {delta_x3_gr}")

# Абсолютні похибки: 
dx1_A = delta_x1_gr * abs(x1)
dx2_A = delta_x2_gr * abs(x2)
dx3_A = delta_x3_gr * abs(x3)

# Обчислення граничної абсолютної похибки результату F
dF_A = abs(dF_dx1) * dx1_A + abs(dF_dx2) * dx2_A + abs(dF_dx3) * dx3_A

#граничної відносної похибки F
delta_F_A = dF_A / abs(func_value)

# Б: Фіксовані абсолютні похибки вхідних даних
dx_B = variant * 10**-3  # Δx1,2,3 = N * 10^{-3} = 19 * 0.001

# Обчислення граничної абсолютної похибки результату F
dF_B = abs(dF_dx1) * dx_B + abs(dF_dx2) * dx_B + abs(dF_dx3) * dx_B

#Обчислення граничної відносної похибки результату F
delta_F_B = dF_B / abs(func_value)

print("Міністерство освіти і науки України")
print("Національний університет «Львівська політехніка»")
print("Інститут комп’ютерних наук та інформаційних технологій")
print("Кафедра автоматизованих систем управління")
print("\nЛабораторна робота №1")
print("«Абсолютна та відносна похибка»")
print(f"Виконав: студент групи {group_num}, номер студентського {student_num}, варіант {variant}")
print("\n1. Вхідні дані:")
print(f"   x1 = {x1} (задано вчителем)")
print(f"   x2 = {x2}")
print(f"   x3 = {x3}")

print("\n2. Значення функції F:")
print(f"   F = {func_value}")

print("\n3. Умова А (похибки через граничні відносні похибки вхідних даних):")
print(f"   Для x1: n={n1}, a_m={a_m_x1}, δx1_gr = 1/(2*{a_m_x1}*10^{n1-1}) = {delta_x1_gr:.6f}")
print(f"   Для x2: n={n2}, a_m={a_m_x2}, δx2_gr = 1/(2*{a_m_x2}*10^{n2-1}) = {delta_x2_gr:.6f}")
print(f"   Для x3: n={n3}, a_m={a_m_x3}, δx3_gr = 1/(2*{a_m_x3}*10^{n3-1}) = {delta_x3_gr:.6f}")
print(f"   Абсолютна похибка x1 (Δx1): {delta_x1_gr} * |{x1}| = {dx1_A:.8f}")
print(f"   Абсолютна похибка x2 (Δx2): {delta_x2_gr} * |{x2}| = {dx2_A:.8f}")
print(f"   Абсолютна похибка x3 (Δx3): {delta_x3_gr} * |{x3}| = {dx3_A:.8f}")
print(f"   Абсолютна похибка результату F (ΔF): {dF_A:.8f}")
print(f"   Відносна похибка результату F (δF): {delta_F_A:.8f} ({delta_F_A * 100:.6f}%)")

print("\n4. Умова Б (фіксовані абсолютні похибки вхідних даних Δx1=Δx2=Δx3=N*10^-3):")
print(f"   Абсолютна похибка x1, x2, x3 (Δx): {dx_B:.6f}")
print(f"   Абсолютна похибка результату F (ΔF): {dF_B:.8f}")
print(f"   Відносна похибка результату F (δF): {delta_F_B:.8f} ({delta_F_B * 100:.6f}%)")

print("\n5. Похідні та проміжні розрахунки (для зручності перевірки):")
print(f"   ∂F/∂x1 ≈ {dF_dx1}")
print(f"   ∂F/∂x2 ≈ {dF_dx2}")
print(f"   ∂F/∂x3 ≈ {dF_dx3}")
