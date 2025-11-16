from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from datetime import date

from database import get_db
from schemas.person import PersonCreate, PersonResponse
from services.crud import (
    get_persons, get_person_by_id, create_person,  get_person_by_name  , get_photos_by_person
)
from  services.search import filter_persons_combined 
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



@router.get("/filter/", response_model=List[PersonResponse])
def filter_persons(
    # Search query
    q: Optional[str] = Query(None, description="Search by name, description, or nationality"),
    
    # Age range filter
    min_age: Optional[int] = Query(None, ge=0, le=150, description="Minimum age"),
    max_age: Optional[int] = Query(None, ge=0, le=150, description="Maximum age"),
    
    # Nationality filter
    nationality: Optional[str] = Query(None, description="Filter by nationality"),
    
    # Date range filter
    start_date: Optional[date] = Query(None, description="Start date (YYYY-MM-DD)"),
    end_date: Optional[date] = Query(None, description="End date (YYYY-MM-DD)"),
    
    # Alive filter
    alive_only: Optional[bool] = Query(None, description="Filter by living persons only"),
    
    # Pagination
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Unified filter endpoint for persons with multiple filter options"""
    return filter_persons_combined(
        db=db,
        query=q,
        min_age=min_age,
        max_age=max_age,
        nationality=nationality,
        start_date=start_date,
        end_date=end_date,
        alive_only=alive_only,
        skip=skip,
        limit=limit
    )

