from pydantic import BaseModel, HttpUrl
from datetime import date
from typing import Optional, List
from .person import PersonResponse
from .location import LocationResponse

class PhotoBase(BaseModel):
    location_id: int
    date: date
    description: str
    persons_ids: Optional[List[int]] = None

class PhotoCreate(PhotoBase):
    pass

class PhotoUpdate(BaseModel):
    location_id: Optional[int] = None
    date: Optional[date] = None
    description: Optional[str] = None
    persons_ids: Optional[List[int]] = None

class PhotoResponse(PhotoBase):
    id: int
    filename: str
    img_url: HttpUrl
    processed_url: Optional[HttpUrl] = None
    
    class Config:
        from_attributes = True

# For detailed responses with relationships
class PhotoWithRelations(PhotoResponse):
    location: Optional[LocationResponse] = None
    persons: List[PersonResponse] = []

class PhotoUploadResponse(BaseModel):
    photo: PhotoResponse
    upload_info: dict

class FileUploadResponse(BaseModel):
    filename: str
    original_filename: str
    url: str
    content_type: str
    size: int