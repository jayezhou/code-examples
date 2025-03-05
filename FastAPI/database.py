import os

from sqlalchemy import create_engine
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import sessionmaker

###
# Database Configuration
###

SQLALCHEMY_DATABASE_URL = "mysql+pymysql://root:root@localhost:3306/fastapi?charset=utf8mb4"

engine = create_engine(
    os.getenv("DB_URL", SQLALCHEMY_DATABASE_URL)
)
SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)

Base = declarative_base()

def get_db():
    ''' Method for configure database '''
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()