import json
from datetime import datetime
from user import UserManager

class Property:
    def __init__(self, landlord, description, price, location, capacity):
        if price <= 0:
            raise ValueError("Ціна має бути додатньою.")
        if capacity <= 0:
            raise ValueError("Кількість місць має бути додатньою.")
        self.landlord = landlord
        self.description = description
        self.price = price
        self.location = location
        self.capacity = capacity
        self.bookings = []
        self.rating = 0
        self.rating_count = 0

    def is_available(self, start_date, end_date):
        try:
            start = datetime.strptime(start_date, "%Y-%m-%d")
            end = datetime.strptime(end_date, "%Y-%m-%d")
            if start >= end:
                raise ValueError("Дата початку має бути раніше дати кінця.")
            for booking in self.bookings:
                booked_start = datetime.strptime(booking["start_date"], "%Y-%m-%d")
                booked_end = datetime.strptime(booking["end_date"], "%Y-%m-%d")
                if not (end <= booked_start or start >= booked_end):
                    return False
            return True
        except ValueError as e:
            raise ValueError(f"Неправильний формат дати або {str(e)}")

    def add_booking(self, client, start_date, end_date):
        self.bookings.append({"client": client, "start_date": start_date, "end_date": end_date})

    def cancel_booking(self, client):
        for booking in self.bookings:
            if booking["client"] == client:
                self.bookings.remove(booking)
                return True
        return False

    def update_rating(self, new_rating):
        if 1 <= new_rating <= 5:
            self.rating = (self.rating * self.rating_count + new_rating) / (self.rating_count + 1)
            self.rating_count += 1
        else:
            raise ValueError("Рейтинг має бути від 1 до 5.")

    def to_dict(self):
        return {
            "landlord": self.landlord,
            "description": self.description,
            "price": self.price,
            "location": self.location,
            "capacity": self.capacity,
            "bookings": self.bookings,
            "rating": self.rating,
            "rating_count": self.rating_count
        }

class PropertyManager:
    def __init__(self):
        self.properties = []

    def add_property(self, landlord, description, price, location, capacity):
        user_mgr = UserManager()
        user_mgr.load_users()
        if not user_mgr.find_user(landlord) or user_mgr.find_user(landlord).user_type != "орендодавець":
            raise ValueError("Орендодавець не зареєстрований .")
        if not description.strip():
            raise ValueError("Опис об'єкта не може бути порожнім.")
        if any(prop.description == description for prop in self.properties):
            raise ValueError("Об'єкт із таким описом уже існує.")
        property = Property(landlord, description, price, location, capacity)
        self.properties.append(property)
        print(f"Об'єкт {description} доданий.")
        self.save_properties()

    def search_property(self, price_limit=None, capacity_min=None, location=None):
        results = self.properties
        if price_limit is not None and price_limit < 0:
            raise ValueError("Максимальна ціна не може бути від'ємною.")
        if capacity_min is not None and capacity_min < 0:
            raise ValueError("Мінімальна кількість місць не може бути від'ємною.")
        
        if price_limit:
            filtered_results = []
            for p in results:
                if p.price <= price_limit:
                    filtered_results.append(p)
            results = filtered_results

       
        if capacity_min:
            filtered_results = []
            for p in results:
                if p.capacity >= capacity_min:
                    filtered_results.append(p)
            results = filtered_results

        if location:
            filtered_results = []
            for p in results:
                if location.lower() in p.location.lower():
                    filtered_results.append(p)
            results = filtered_results
        
        return sorted(results, key=lambda x: x.rating, reverse=True)
    def book_property(self, property_desc, client, start_date, end_date):
        user_mgr = UserManager()
        user_mgr.load_users()
        if not user_mgr.find_user(client) or user_mgr.find_user(client).user_type != "клієнт":
            raise ValueError("Клієнт не зареєстрований або не є client.")
        for prop in self.properties:
            if prop.description == property_desc and prop.is_available(start_date, end_date):
                prop.add_booking(client, start_date, end_date)
                print(f"Бронювання {property_desc} для {client} успішне.")
                self.save_properties()
                return
        raise ValueError("Об'єкт недоступний або вже заброньований.")

    def cancel_booking(self, client, property_desc):
        for prop in self.properties:
            if prop.description == property_desc and prop.cancel_booking(client):
                print(f"Бронювання {property_desc} для {client} скасовано.")
                self.save_properties()
                return
        raise ValueError("Бронювання не знайдено.")

    def add_review(self, client, property_desc, review, rating):
        user_mgr = UserManager()
        user_mgr.load_users()
        client_user = user_mgr.find_user(client)
        if not client_user:
            raise ValueError("Клієнт не зареєстрований.")
        if not review.strip():
            raise ValueError("Відгук не може бути порожнім.")
        for prop in self.properties:
            if prop.description == property_desc:
                client_user.add_review(property_desc, review, rating)
                prop.update_rating(rating)
                user_mgr.save_users()
                self.save_properties()
                print(f"Відгук для {property_desc} додано.")
                return
        raise ValueError("Об'єкт не знайдено.")

    def get_landlord_report(self, landlord):
        report = []
        for prop in self.properties:
            if prop.landlord == landlord:
                report.append(f"Об'єкт: {prop.description}, Бронювання: {len(prop.bookings)}, Рейтинг: {prop.rating:.1f}")
        return report

    def save_properties(self):
        data = [prop.to_dict() for prop in self.properties]
        with open("properties.json", "w", encoding="utf-8") as f:
            f.write(json.dumps(data, ensure_ascii=False, indent=4))

    def load_properties(self):
        try:
            with open("properties.json", "r", encoding="utf-8") as f:
                data = json.loads(f.read())
                self.properties = [Property(item["landlord"], item["description"], item["price"],
                                          item["location"], item["capacity"]) for item in data]
                for prop, prop_data in zip(self.properties, data):
                    prop.bookings = prop_data.get("bookings", [])
                    prop.rating = prop_data.get("rating", 0)
                    prop.rating_count = prop_data.get("rating_count", 0)
        except FileNotFoundError:
            self.properties = []