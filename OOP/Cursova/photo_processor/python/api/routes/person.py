from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from datetime import date

from database import get_db
from schemas.person import PersonCreate, PersonResponse
from services.crud import (
    get_persons, get_person_by_id, create_person,  get_person_by_name  , get_photos_by_person
)
from  services.search import search_persons, search_persons_by_age_range, search_persons_by_nationality, search_persons_by_date_range
router = APIRouter(prefix="/persons", tags=["persons"])

@router.get("/", response_model=List[PersonResponse])
def read_persons(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Get all persons with pagination"""
    return get_persons(db, skip=skip, limit=limit)

@router.get("/{person_id}", response_model=PersonResponse)
def read_person(person_id: int, db: Session = Depends(get_db)):
    """Get a specific person by ID"""
    person = get_person_by_id(db, person_id=person_id)
    if person is None:
        raise HTTPException(status_code=404, detail="Person not found")
    return person

@router.post("/", response_model=PersonResponse)
def create_new_person(person: PersonCreate, db: Session = Depends(get_db)):
    """Create a new person"""
    return create_person(db=db, person=person)

@router.get("/search/", response_model=List[PersonResponse])
def search_persons_endpoint(
    q: str = Query(..., description="Search query for name, description, or nationality"),
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Search persons by name, description, or nationality"""
    return search_persons(db, query=q, skip=skip, limit=limit)

@router.get("/filter/age-range/", response_model=List[PersonResponse])
def filter_persons_by_age(
    min_age: int = Query(0, ge=0, description="Minimum age"),
    max_age: int = Query(150, le=150, description="Maximum age"),
    db: Session = Depends(get_db)
):
    """Filter persons by age range"""
    return search_persons_by_age_range(db, min_age=min_age, max_age=max_age)

@router.get("/filter/nationality/", response_model=List[PersonResponse])
def filter_persons_by_nationality(
    nationality: str = Query(..., description="Nationality to filter by"),
    db: Session = Depends(get_db)
):
    """Filter persons by nationality"""
    return search_persons_by_nationality(db, nationality=nationality)

@router.get("/filter/birth-date-range/", response_model=List[PersonResponse])
def filter_persons_by_birth_date(
    start_date: date = Query(..., description="Start date (YYYY-MM-DD)"),
    end_date: date = Query(..., description="End date (YYYY-MM-DD)"),
    db: Session = Depends(get_db)
):
    """Filter persons by birth date range"""
    return search_persons_by_date_range(db, start_date=start_date, end_date=end_date)

@router.get("/name/{first_name}/{last_name}", response_model=PersonResponse)
def get_person_by_full_name(
    first_name: str,
    last_name: str,
    db: Session = Depends(get_db)
):
    """Get person by first and last name"""
    person = get_person_by_name(db, first_name=first_name, last_name=last_name)
    if person is None:
        raise HTTPException(status_code=404, detail="Person not found")
    return person

@router.get("/alive/", response_model=List[PersonResponse])
def get_living_persons(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Get persons who are alive (no death date)"""
    persons = get_persons(db, skip=skip, limit=limit)
    return [person for person in persons if person.death_date is None]

@router.get("/photos/{person_id}}", response_model=List[PersonResponse])
def get_person_photos( 
    person_id: int,
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Get persons who are alive (no death date)"""
    persons = get_photos_by_person( person_id , db, skip=skip, limit=limit)
    return [person for person in persons if person.death_date is None]