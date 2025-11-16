from pydantic import BaseModel
from typing import Optional, List

class LocationBase(BaseModel):
    name: str
    country: Optional[str] = None
    coordinates: Optional[str] = None
    description: str
    existing: Optional[bool] = True

class LocationCreate(LocationBase):
    pass

class LocationUpdate(BaseModel):
    name: Optional[str] = None
    country: Optional[str] = None
    coordinates: Optional[str] = None
    description: Optional[str] = None

class LocationResponse(LocationBase):
    id: int
    
    class Config:
        from_attributes = True