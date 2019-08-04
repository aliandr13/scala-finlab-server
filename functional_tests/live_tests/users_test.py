from hamcrest import assert_that, is_, not_none

userName = "jwick200"

user1 = {
    "userName": userName,
    "firstName": "John",
    "lastName": "Wick",
    "email": "wheresmycar@gmail.com",
    "password": "wickofyourwit",
    "phone": "215-789-0123"
}


def test_user_endpoint(finlab_client):
    response = finlab_client.get_user_endpoint()
    json_response = response.json()
    assert_that(json_response['name'], is_("user endpoint"))


def test_user_by_username(finlab_client):
    response = finlab_client.find_user_by_name(userName)
    resp_user = response.json()
    assert_that(resp_user['userName'], is_(userName))


def test_create_user(finlab_client):
    response = finlab_client.create_user(user1)
    new_user = response.json()
    assert_that(new_user['name'], is_("user create endpoint"))
    # assert_that(new_user['id'], not_none())
    # assert_that(new_user['userName'], is_(user1['userName']))


def test_update_user(finlab_client):
    response = finlab_client.update_user(user1)
    update_user = response.json()
    assert_that(update_user['name'], is_("user update endpoint"))


def test_delete_user(finlab_client):
    response = finlab_client.delete_user(user1)
    delete_user = response.json()
    assert_that(delete_user['name'], is_("user delete endpoint"))
