from pydantic import BaseModel
from datetime import date
from typing import Optional, List

class PersonBase(BaseModel):
    first_name: str
    last_name: str
    age: int
    birth_date: date
    death_date: Optional[date] = None
    nationality: str
    description: str

class PersonCreate(PersonBase):
    pass

class PersonUpdate(BaseModel):
    first_name: Optional[str] = None
    last_name: Optional[str] = None
    age: Optional[int] = None
    birth_date: Optional[date] = None
    death_date: Optional[date] = None
    nationality: Optional[str] = None
    description: Optional[str] = None

class PersonResponse(PersonBase):
    id: int
    
    class Config:
        from_attributes = True