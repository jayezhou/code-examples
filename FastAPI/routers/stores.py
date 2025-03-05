from fastapi import APIRouter, Depends, HTTPException
from typing import List
from sqlalchemy.orm import Session

from ..database import get_db
from ..auth.service import get_current_active_user
from ..domain.store import service, schemas

# router = APIRouter(tags=["stores"])

router = APIRouter(
    prefix="/stores",
    tags=["stores"],
    dependencies=[Depends(get_current_active_user)],
    responses={404: {"description": "Not found"}},
)


@router.get("/stores/", response_model=List[schemas.Store])
def read_stores(skip: int = 0, limit: int = 100, db: Session = Depends(get_db)):
    users = service.get_stores(db, skip=skip, limit=limit)
    return users


@router.get("/stores/{store_id}", response_model=schemas.Store)
def read_store(store_id: int, db: Session = Depends(get_db)):
    db_store = service.get_store(db, user_id=store_id)
    if db_store is None:
        raise HTTPException(status_code=404, detail="Store not found")
    return db_store


@router.post("/stores/{store_id}/items/", response_model=schemas.Item)
def create_item_for_store(
        store_id: int, item: schemas.ItemCreate, db: Session = Depends(get_db)
):
    return service.create_store_item(db=db, item=item, store_id=store_id)