from fastapi import APIRouter, Depends, HTTPException, Query, UploadFile, File, Form 
from sqlalchemy.orm import Session
from typing import List, Optional
from datetime import date
import json

from database import get_db
from schemas.photo import (
    PhotoCreate, PhotoResponse, PhotoUploadResponse, FileUploadResponse
)
from models.person import Person
from models.photo import Photo
from services.crud import (
    get_photos, get_photo_by_id, create_photo, 
     get_photos_by_person, get_photos_by_location,
)
from services.r2_service import r2_service

router = APIRouter(prefix="/photos", tags=["photos"])
### ========= GET ============###
@router.get("/", response_model=List[PhotoResponse])
def read_photos(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Get all photos with pagination"""
    return get_photos(db, skip=skip, limit=limit)

@router.get("/{photo_id}", response_model=PhotoResponse)
def read_photo(photo_id: int, db: Session = Depends(get_db)):
    """Get a specific photo by ID"""
    photo = get_photo_by_id(db, photo_id=photo_id)
    if photo is None:
        raise HTTPException(status_code=404, detail="Photo not found")
    return photo


@router.get("/{photo_id}/url")
async def get_photo_url(
    photo_id: int,
    db: Session = Depends(get_db)
):
    """Get photo URL from R2"""
    photo = get_photo_by_id(db, photo_id)
    if not photo:
        raise HTTPException(status_code=404, detail="Photo not found")
    
    return {
        "filename": photo.filename,
        "original_url": photo.img_url,
        "processed_url": photo.processed_url,
        "direct_url": r2_service.get_file_url(photo.filename)
    }


###======== CREATE ============###
@router.post("/", response_model=PhotoResponse)
async def create_photo(
    file: UploadFile = File(...),
    location_id: int = Form(...),
    date: date = Form(...),
    description: str = Form(...),
    persons_ids: str = Form("[]"),
    db: Session = Depends(get_db)
):
    """Simple and clean - no custom schemas needed"""
    upload_info = await r2_service.upload_file(file)


    
    person_ids_list = json.loads(persons_ids) if persons_ids else []
    db_photo = Photo(
        filename=upload_info["filename"],
        img_url=upload_info["url"],
        processed_url=upload_info["url"],
        location_id=location_id,
        date=date,
        description=description,
    )
    
    db.add(db_photo)
    db.commit()
    db.refresh(db_photo)
    
    for person_id in person_ids_list:
        person = db.query(Person).filter(Person.id == person_id).first()
        if person:
            db_photo.persons.append(person)
    
    if person_ids_list:
        db.commit()
        db.refresh(db_photo)
    
    return db_photo



##=========== SEARCH =============###

@router.get("/search/", response_model=List[PhotoResponse])
def search_photos_endpoint(
    q: str = Query(None, description="Search query for filename or description"),
    db: Session = Depends(get_db)
):
    """Search photos by filename or description"""
    if not q:
        raise HTTPException(status_code=400, detail="Search query is required")
    return search_photos(db, query=q)



##========= FILTER =============###
@router.get("/filter/date-range/", response_model=List[PhotoResponse])
def filter_photos_by_date_range(
    start_date: date = Query(..., description="Start date (YYYY-MM-DD)"),
    end_date: date = Query(..., description="End date (YYYY-MM-DD)"),
    db: Session = Depends(get_db)
):
    """Filter photos by date range"""
    return search_photos_by_date_range(db, start_date=start_date, end_date=end_date)

@router.get("/filter/person/{person_id}", response_model=List[PhotoResponse])
def get_photos_by_person_endpoint(person_id: int, db: Session = Depends(get_db)):
    """Get all photos containing a specific person"""
    return get_photos_by_person(db, person_id=person_id)

@router.get("/filter/location/{location_id}", response_model=List[PhotoResponse])
def get_photos_by_location_endpoint(location_id: int, db: Session = Depends(get_db)):
    """Get all photos from a specific location"""
    return get_photos_by_location(db, location_id=location_id)

@router.delete("/{photo_id}")
async def delete_photo(
    photo_id: int,
    db: Session = Depends(get_db)
):
    """Delete photo and associated file from R2"""
    success = delete_photo_with_file(db, photo_id)
    if not success:
        raise HTTPException(status_code=404, detail="Photo not found")
    return {"message": "Photo deleted successfully"}

