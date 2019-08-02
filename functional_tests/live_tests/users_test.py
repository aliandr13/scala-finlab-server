from hamcrest import assert_that, is_

userName = "jwick200"

user1 = {
    "userName": userName,
    "firstName": "John",
    "lastName": "Wick",
    "email": "wheresmycar@gmail.com",
    "password": "wickofyourwit",
    "phone": "215-789-0123",
    "role": "Admin"
}


def test_user_endpoint(finlab_client):
    response = finlab_client.get_user_endpoint()
    json_response = response.json()
    assert_that(json_response['name'], is_("user endpoint"))


def test_user_by_username(finlab_client):
    response = finlab_client.find_user_by_name(userName)
    resp_user = response.json()
    assert_that(resp_user['userName'], is_(userName))
