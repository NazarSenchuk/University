from sqlalchemy import Table, Column, Integer, ForeignKey
from database import Base

photo_person = Table(
    "photo_person",
    Base.metadata,
    Column("photo_id", Integer, ForeignKey("photos.id"), primary_key=True),
    Column("person_id", Integer, ForeignKey("persons.id"), primary_key=True),
)