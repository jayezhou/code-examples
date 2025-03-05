from typing import List, Optional

from pydantic import BaseModel


class User(BaseModel):
    username: str
    email: str | None = None
    disabled: bool | None = None


class UserCreate(User):
    password: str


class Token(BaseModel):
    access_token: str
    token_type: str


class TokenData(BaseModel):
    username: str | None = None