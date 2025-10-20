# models/person.py
from sqlalchemy import Column, Integer, String, Date, Index
from sqlalchemy.orm import relationship
from database import Base
from models.photo_person import photo_person  
class Person(Base):
    __tablename__ = "persons"

    id = Column(Integer, primary_key=True, index=True)
    age = Column(Integer)
    birth_date = Column(Date)
    death_date = Column(Date, nullable=True)
    nationality = Column(String)
    description = Column(String)
    first_name = Column(String, index=True)
    last_name = Column(String, index=True)
    gender = Column(String)
    photos = relationship("Photo", secondary=photo_person, back_populates="persons")
