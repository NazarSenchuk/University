import json
import re

class User:
    def __init__(self, username, user_type):
        if not re.match("^[a-zA-Zа-яА-Я]+$", username):
            raise ValueError("Ім'я може містити тільки літери.")
        if not username.strip():
            raise ValueError("Ім'я не може бути порожнім.")
        self.username = username
        self.user_type = user_type 
        self.reviews = [] 

    def add_review(self, property_desc, review, rating):
        if 1 <= rating <= 5:
            self.reviews.append({"property": property_desc, "review": review, "rating": rating})
        else:
            raise ValueError("Рейтинг має бути від 1 до 5.")

    def to_dict(self):
        return {"username": self.username, "user_type": self.user_type, "reviews": self.reviews}

class UserManager:
    def __init__(self):
        self.users = []

    def add_user(self, username, user_type):
        if user_type not in ["орендодавець", "клієнт"]:
            raise ValueError("Тип користувача має бути Орендодавець або Клієнт .")
        if any(user.username == username for user in self.users):
            raise ValueError("Користувач із таким ім'ям уже існує.")
        user = User(username, user_type)
        self.users.append(user)
        print(f"Користувач {username} ({user_type}) доданий.")
        self.save_users()

    def find_user(self, username):
        return next((user for user in self.users if user.username == username), None)

    def save_users(self):
        data = [user.to_dict() for user in self.users]
        with open("users.json", "w", encoding="utf-8") as f:
            f.write( json.dumps(data, ensure_ascii=False, indent=4))

    def load_users(self):
        try:
            with open("users.json", "r", encoding="utf-8") as f:
                data = json.loads(f.read())
                self.users = [User(item["username"], item["user_type"]) for item in data]
                for user, user_data in zip(self.users, data):
                    user.reviews = user_data.get("reviews", [])
        except FileNotFoundError:
            self.users = []
