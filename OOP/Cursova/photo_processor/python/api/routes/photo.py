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
from services.search import (
    filter_photos_combined
)

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
        "ame": photo.name,
        "original_url": photo.img_url,
        "processed_url": photo.processed_url,
        "direct_url": r2_service.get_file_url(photo.id)
    }


@router.get("/person/{person_id}}", response_model=List[PhotoResponse])
def get_person_photos( 
    person_id: int,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    photos = get_photos_by_person(  db,person_id=person_id, skip=skip, limit=limit)
    return photos

@router.get("/location/{person_id}}", response_model=List[PhotoResponse])
def get_person_photos( 
    person_id: int,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    photos = get_photos_by_location(  db,person_id=person_id, skip=skip, limit=limit)
    return photos

###======== CREATE ============###
@router.post("/", response_model=PhotoResponse)
async def create_photo(
    file: UploadFile = File(...),
    name: str = Form(...),  
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
        name=name,
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

@router.get("/filter/", response_model=List[PhotoResponse])
def search_photos_endpoint(
    query: Optional[str] = Query(None, description="Search in filename or description"),
    start_date: Optional[date] = Query(None, description="Start date (YYYY-MM-DD)"),
    end_date: Optional[date] = Query(None, description="End date (YYYY-MM-DD)"),
    year: Optional[int] = Query(None, description="Filter by year", ge=1900, le=2100),
    min_persons: Optional[int] = Query(None, description="Minimum number of persons in photo", ge=0),
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Unified search for photos with multiple filter options"""
    return filter_photos_combined(
        db=db,
        query=query,
        start_date=start_date,
        end_date=end_date,
        year=year,
        min_persons=min_persons,
        skip=skip,
        limit=limit
    )


