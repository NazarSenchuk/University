# models/location.py
from sqlalchemy import Column, Integer, String
from sqlalchemy.orm import relationship
from database import Base

class Location(Base):
    __tablename__ = "locations"

    id = Column(Integer, primary_key=True, index=True)
    name = Column(String, unique=True, index=True)
    country = Column(String)
    coordinates = Column(String)
    country = Column(String)
    photos = relationship("Photo", back_populates="location")
