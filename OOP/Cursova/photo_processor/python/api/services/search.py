from sqlalchemy import or_, and_
from sqlalchemy.orm import Session
from datetime import date
from models.person import Person
from models.location import Location
from models.photo import Photo


from typing import Optional





from sqlalchemy import and_, or_, func
from typing import Optional
from datetime import date

def filter_photos_combined(
    db: Session,
    query: Optional[str] = None,
    start_date: Optional[date] = None,
    end_date: Optional[date] = None,
    year: Optional[int] = None,
    skip: int = 0,
    limit: int = 100
):
    """
    Combined search for photos with multiple filter options
    """
    base_query = db.query(Photo)
    
    # Build filters dynamically
    filters = []
    
    # Text search filter
    if query:
        filters.append(
            or_(
                Photo.name.ilike(f"%{query}%"),
                Photo.description.ilike(f"%{query}%")
            )
        )
    
    # Date range filter
    if start_date and end_date:
        filters.append(Photo.date.between(start_date, end_date))
    elif start_date:
        filters.append(Photo.date >= start_date)
    elif end_date:
        filters.append(Photo.date <= end_date)
    
    # Year filter (takes precedence over date range for that year)
    if year:
        filters.append(Photo.date.between(f"{year}-01-01", f"{year}-12-31"))
    
    # Apply pagination and return
    return base_query.offset(skip).limit(limit).all()


def filter_locations_combined(
    db: Session,
    query: Optional[str] = None,

    existing: Optional[int] = None,
    skip: int = 0,
    limit: int = 100
):

    base_query = db.query(Location)
    
    
    filters = []
    if query:
        filters.append(
            or_(
                Location.name.ilike(f"%{query}%"),
            )
        )
  
    filters.append(Location.existing == existing)
    
    if filters:
        base_query = base_query.filter(and_(*filters))

    return base_query.offset(skip).limit(limit).all()

from sqlalchemy import and_, or_
from typing import Optional
from datetime import date


def filter_persons_combined(
    db: Session,
    query: Optional[str] = None,
    min_age: Optional[int] = None,
    max_age: Optional[int] = None,
    nationality: Optional[str] = None,
    start_date: Optional[date] = None,
    end_date: Optional[date] = None,
    alive_only: Optional[bool] = None,
    skip: int = 0,
    limit: int = 100
):
    # Start with base query
    base_query = db.query(Person)
    
    # Build filters dynamically
    filters = []
    
    # Text search filter
    if query:
        filters.append(
            or_(
                Person.first_name.ilike(f"%{query}%"),
                Person.last_name.ilike(f"%{query}%"),
                Person.description.ilike(f"%{query}%"),
                Person.nationality.ilike(f"%{query}%")
            )
        )
    
    # Age range filter
    if min_age is not None:
        filters.append(Person.age >= min_age)
    if max_age is not None:
        filters.append(Person.age <= max_age)
    
    # Nationality filter (exact match or partial)
    if nationality:
        filters.append(Person.nationality.ilike(f"%{nationality}%"))
    
    # Date range filter
    if start_date:
        filters.append(Person.birth_date >= start_date)
    if end_date:
        filters.append(Person.birth_date <= end_date)
    
    # Alive filter
    if alive_only is not None:
        if alive_only:
            # alive_only=1: show only alive people
            filters.append(Person.death_date.is_(None))
        else:
            # alive_only=0: show only dead people
            filters.append(Person.death_date.is_not(None))
    
    # Apply all filters
    if filters:
        base_query = base_query.filter(and_(*filters))
    
    # Apply pagination and return
    return base_query.offset(skip).limit(limit).all()