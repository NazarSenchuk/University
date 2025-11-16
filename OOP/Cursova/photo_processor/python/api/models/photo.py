# models/photo.py
from sqlalchemy import Column, Integer, String, ForeignKey, Date, Index
from sqlalchemy.orm import relationship
from database import Base
from models.photo_person import photo_person
class Photo(Base):
    __tablename__ = "photos"

    id = Column(Integer, primary_key=True, index=True)
    name  = Column(String, index=True)
    location_id = Column(Integer, ForeignKey("locations.id"))
    img_url = Column(String, unique=True, index=True)
    processed_url = Column(String, unique=True, index=True)
    date = Column(Date, index=True)
    description = Column(String)

    location = relationship("Location", back_populates="photos")
    persons = relationship("Person", secondary=photo_person, back_populates="photos")
