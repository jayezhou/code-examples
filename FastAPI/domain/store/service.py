from sqlalchemy.orm import Session

from . import models, schemas


def get_store(db: Session, store_id: int):
    return db.query(models.Store).filter(models.Store.id == store_id).first()


def get_stores(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Store).offset(skip).limit(limit).all()


def create_store(db: Session, store: schemas.Store):
    db_store = models.Store(title=store.title)
    db.add(db_store)
    db.commit()
    db.refresh(db_store)
    return db_store


def get_items(db: Session, skip: int = 0, limit: int = 100):
    return db.query(models.Item).offset(skip).limit(limit).all()


def create_store_item(db: Session, item: schemas.ItemCreate, store_id: int):
    db_item = models.Item(**item.dict(), owner_id=store_id)
    db.add(db_item)
    db.commit()
    db.refresh(db_item)
    return db_item
