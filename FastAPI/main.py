from fastapi import Depends, FastAPI, HTTPException, status

from datetime import timedelta
from typing import Annotated
from fastapi.security import OAuth2PasswordRequestForm

from .database import engine, Base

from .routers import stores, items

from .auth import service
from .auth.service import ACCESS_TOKEN_EXPIRE_MINUTES, authenticate_user, create_access_token, get_current_active_user
from .auth.schemas import Token, UserCreate, User


## Start FastApi App
# app = FastAPI(dependencies=[Depends(get_query_token)])
app = FastAPI()

## Generate database tables
Base.metadata.create_all(bind=engine)

app.include_router(stores.router)
# app.include_router(items.router)


@app.get("/")
def root():
    return {"message": "Hello FastAPI!"}

@app.post("/signup", response_model=User)
def create_user(user: UserCreate):
    db_user = service.get_user(username=user.username)
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    db_user = service.create_user(user=user)
    return User(username=db_user.username, email=db_user.email, disabled=db_user.disabled)

# Username: johndoe Password: secret
# grant_type=password&username=johndoe&password=secret
@app.post("/token")
def login_for_access_token(
    form_data: Annotated[OAuth2PasswordRequestForm, Depends()],
) -> Token:
    user = authenticate_user(form_data.username, form_data.password)
    if not user:
        raise HTTPException(
            status_code=status.HTTP_401_UNAUTHORIZED,
            detail="Incorrect username or password",
            headers={"WWW-Authenticate": "Bearer"},
        )
    access_token_expires = timedelta(minutes=ACCESS_TOKEN_EXPIRE_MINUTES)
    access_token = create_access_token(
        data={"sub": user.username}, expires_delta=access_token_expires
    )
    return Token(access_token=access_token, token_type="bearer")


@app.get("/users/me/", response_model=User)
def read_users_me(
    current_user: Annotated[User, Depends(get_current_active_user)],
):
    return current_user
