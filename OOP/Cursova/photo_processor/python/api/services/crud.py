import sqlalchemy
from sqlalchemy.orm import Session
from models.person import Person
from schemas.person import PersonCreate


def get_persons(db: Session, skip: int = 0, limit: int = 100):
    return db.query(Person).offset(skip).limit(limit).all()

def get_person_by_id(db: Session, person_id: int):
    return db.query(Person).filter(Person.id == person_id).first()

def create_person(db: Session, person: PersonCreate):
    db_person = Person(**person.dict())
    db.add(db_person)
    db.commit()
    db.refresh(db_person)
    return db_person

def get_person_by_name(db: Session, first_name: str, last_name: str):
    return db.query(Person).filter(
        Person.first_name == first_name,
        Person.last_name == last_name
    ).first()

from models.location import Location
from schemas.location import LocationCreate

def get_locations(db: Session, skip: int = 0, limit: int = 100):
    return db.query(Location).offset(skip).limit(limit).all()

def get_location_by_id(db: Session, location_id: int):
    return db.query(Location).filter(Location.id == location_id).first()

def create_location(db: Session, location: LocationCreate):
    db_location = Location(**location.dict())
    db.add(db_location)
    db.commit()
    db.refresh(db_location)
    return db_location

def get_location_by_name(db: Session, name: str):
    return db.query(Location).filter(Location.name == name).first()


from models.photo import Photo
from models.person import Person
from schemas.photo import PhotoCreate

def get_photos(db: Session, skip: int = 0, limit: int = 100):
    return db.query(Photo).offset(skip).limit(limit).all()

def get_photo_by_id(db: Session, photo_id: int):
    return db.query(Photo).filter(Photo.id == photo_id).first()

def create_photo(db: Session, photo: PhotoCreate):
    db_photo = Photo(**photo.dict())
    db.add(db_photo)
    db.commit()
    db.refresh(db_photo)
    return db_photo

def get_photos_by_person(db: Session, person_id: int, skip: int = 0, limit: int = 100):
    """Get all photos associated with a specific person"""
    return db.query(Photo).join(Photo.persons).filter(Person.id == person_id).offset(skip).limit(limit).all()

def get_photos_by_location(db: Session, location_id: int, skip: int = 0, limit: int = 100):
    """Get all photos from a specific location"""
    return db.query(Photo).filter(Photo.location_id == location_id).offset(skip).limit(limit).all()