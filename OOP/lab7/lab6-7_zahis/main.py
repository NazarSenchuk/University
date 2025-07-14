from user import UserManager
from property import PropertyManager
from datetime import datetime 
import re

def validate_date(date_str):
    try:
        datetime_obj = datetime.strptime(date_str, "%Y-%m-%d")
        if datetime_obj < datetime.now():
            raise ValueError("Дата не може бути в минулому.")
        return date_str
    except ValueError as e:
        raise ValueError("Неправильний формат дати (YYYY-MM-DD) або дата в минулому.")

def main():
    user_mgr = UserManager()
    prop_mgr = PropertyManager()

    # Завантаження даних
    user_mgr.load_users()
    prop_mgr.load_properties()

    while True:
        print("\n1. Додати користувача")
        print("2. Додати об'єкт оренди")
        print("3. Пошук об'єктів")
        print("4. Забронювати об'єкт")
        print("5. Скасувати бронювання")
        print("6. Додати відгук")
        print("7. Звіт для орендодавця")
        print("8. Вийти")
        choice = input("Виберіть опцію: ")

        try:
            match choice:
                case "1":
                    username = input("Введіть ім'я користувача: ")
                    user_type = input("Введіть тип (Орендодавець/Клієнт): ").lower()
                    user_mgr.add_user(username.strip(), user_type.strip())

                case "2":
                    landlord = input("Введіть ім'я орендодавця: ")
                    description = input("Опис об'єкта: ")
                    price = float(input("Ціна за добу: "))
                    if price <= 0:
                        raise ValueError("Ціна має бути додатньою.")
                    location = input("Локація: ")
                    capacity = int(input("Кількість місць: "))
                    if capacity <= 0:
                        raise ValueError("Кількість місць має бути додатньою.")
                    prop_mgr.add_property(landlord.strip(), description.strip(), price, location.strip(), capacity)

                case "3":
                    price_limit = float(input("Максимальна ціна (або 0 для всіх): ") or 0)
                    if price_limit < 0:
                        raise ValueError("Максимальна ціна не може бути від'ємною.")
                    capacity_min = int(input("Мінімальна кількість місць (або 0 для всіх): ") or 0)
                    if capacity_min < 0:
                        raise ValueError("Мінімальна кількість місць не може бути від'ємною.")
                    location = input("Локація (або натисніть Enter для всіх): ") or None
                    if location:
                        location = location.strip()
                    results = prop_mgr.search_property(price_limit, capacity_min, location)
                    for prop in results:
                        print(f"Опис: {prop.description}, Ціна: {prop.price}, Локація: {prop.location}, "
                            f"Місць: {prop.capacity}, Рейтинг: {prop.rating:.1f} ({prop.rating_count} відгуків)")

                case "4":
                    property_desc = input("Введіть опис об'єкта для бронювання: ")
                    client = input("Введіть ім'я клієнта: ")
                    
                    start_date = validate_date(input("Дата початку (наприклад: 2025-06-10): ").strip())
                    end_date = validate_date(input("Дата кінця (наприклад: 2025-06-15): ").strip())
                    if datetime.strptime(start_date, "%Y-%m-%d") >= datetime.strptime(end_date, "%Y-%m-%d"):
                        raise ValueError("Дата початку має бути раніше дати кінця.")
                    prop_mgr.book_property(property_desc.strip(), client.strip(), start_date, end_date)

                case "5":
                    client = input("Введіть ім'я клієнта: ")
                    property_desc = input("Введіть опис об'єкта для скасування: ")
                    prop_mgr.cancel_booking(client.strip(), property_desc.strip())

                case "6":
                    client = input("Введіть ім'я клієнта: ")
                    property_desc = input("Введіть опис об'єкта: ")
                    review = input("Введіть відгук: ")
                    rating = int(input("Введіть рейтинг (1-5): "))
                    if rating < 1 or rating > 5:
                        raise ValueError("Рейтинг має бути від 1 до 5.")
                    prop_mgr.add_review(client.strip(), property_desc.strip(), review.strip(), rating)

                case "7":
                    landlord = input("Введіть ім'я орендодавця: ")
                    report = prop_mgr.get_landlord_report(landlord.strip())
                    for line in report:
                        print(line)

                case "8":
                    print("Завершення програми!")
                    break

                case _:
                    print("Такого пункту немає.")

        except ValueError as e:
            print(f"Помилка: {e}")
        except Exception as e:
            print(f"Помилка: {e}")

if __name__ == "__main__":
    main()