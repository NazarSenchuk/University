from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional

from database import get_db
from schemas.location import LocationCreate, LocationResponse
from services.crud import (
    get_locations, get_location_by_id, create_location, 
get_photos_by_location , get_location_by_name
)
from  services.search import search_locations 
router = APIRouter(prefix="/locations", tags=["locations"])

@router.get("/", response_model=List[LocationResponse])
def read_locations(
    skip: int = 0,
    limit: int = 100,
    db: Session = Depends(get_db)
):
    """Get all locations with pagination"""
    return get_locations(db, skip=skip, limit=limit)

@router.get("/{location_id}", response_model=LocationResponse)
def read_location(location_id: int, db: Session = Depends(get_db)):
    """Get a specific location by ID"""
    location = get_location_by_id(db, location_id=location_id)
    if location is None:
        raise HTTPException(status_code=404, detail="Location not found")
    return location

@router.post("/", response_model=LocationResponse)
def create_new_location(location: LocationCreate, db: Session = Depends(get_db)):
    """Create a new location"""
    return create_location(db=db, location=location)

@router.get("/search/", response_model=List[LocationResponse])
def search_locations_endpoint(
    q: str = Query(..., description="Search query for location name, country, or description"),
    db: Session = Depends(get_db)
):
    """Search locations by name, country, or description"""
    return search_locations(db, query=q)

