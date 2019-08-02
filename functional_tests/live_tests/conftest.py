import pytest
from finlab_client import FinlabClient

example_user = {
    "userName": "example",
    "firstName": "John",
    "lastName": "Wick",
    "email": "wheresmycar@gmail.com",
    "password": "wickofyourwit",
    "phone": "215-789-0123",
    "role": "Admin"
}

@pytest.fixture(scope="session")
def finlab_client():
    return FinlabClient()