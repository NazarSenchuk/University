from sqlalchemy import or_, and_
from sqlalchemy.orm import Session
from datetime import date
from models.person import Person
from models.location import Location
from models.photo import Photo


def search_photos(db: Session, query: str):
    return db.query(Photo).filter(
        or_(
            Photo.filename.ilike(f"%{query}%"),
            Photo.description.ilike(f"%{query}%")
        )
    ).all()

def search_photos_by_date_range(db: Session, start_date: date, end_date: date):
    return db.query(Photo).filter(
        and_(
            Photo.date >= start_date,
            Photo.date <= end_date
        )
    ).all()

def search_photos_by_year(db: Session, year: int):
    return db.query(Photo).filter(
        Photo.date.between(f"{year}-01-01", f"{year}-12-31")
    ).all()

def search_photos_with_persons(db: Session, min_persons: int = 1):
    # Photos that have at least min_persons people
    return db.query(Photo).join(Photo.persons).group_by(Photo.id).having(
        db.func.count(Person.id) >= min_persons
    ).all()


def search_locations(db: Session, query: str):
    return db.query(Location).filter(
        or_(
            Location.name.ilike(f"%{query}%"),
            Location.country.ilike(f"%{query}%"),
            Location.description.ilike(f"%{query}%")
        )
    ).all()


def search_persons(db: Session, query: str, skip: int = 0, limit: int = 100):
    return db.query(Person).filter(
        or_(
            Person.first_name.ilike(f"%{query}%"),
            Person.last_name.ilike(f"%{query}%"),
            Person.description.ilike(f"%{query}%"),
            Person.nationality.ilike(f"%{query}%")
        )
    ).offset(skip).limit(limit).all()

def search_persons_by_age_range(db: Session, min_age: int, max_age: int):
    return db.query(Person).filter(
        and_(
            Person.age >= min_age,
            Person.age <= max_age
        )
    ).all()

def search_persons_by_date_range(db: Session, start_date: date, end_date: date):
    return db.query(Person).filter(
        and_(
            Person.birth_date >= start_date,
            Person.birth_date <= end_date
        )
    ).all()

def search_persons_by_nationality(db: Session, nationality: str):
    return db.query(Person).filter(Person.nationality.ilike(f"%{nationality}%")).all()