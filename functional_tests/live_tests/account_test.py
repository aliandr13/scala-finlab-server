import pytest
from hamcrest import assert_that, is_, not_none

my_test_acc = {
    "name": "my_test_acc",
    "description": "some description",
    "type": 1,
    "currency": 1,
    "balance": 0
}


def test_pet_endpoint_info(finlab_client):
    response = finlab_client.get_account_endpoint()
    json_response = response.json()
    assert_that(json_response['name'], is_("accounts endpoint"))


def test_create_account(finlab_client):
    acc_to_save = {
        "name": "create_new_acc",
        "description": "some description",
        "type": 1,
        "currency": 1,
        "balance": 10.5
    }

    saved_acc = None

    try:
        response = finlab_client.create_account(acc_to_save)
        saved_acc = response.json()
        assert_that(response.status_code, is_(200))
        assert_that(saved_acc['name'], is_(acc_to_save['name']))
        assert_that(saved_acc['description'], is_(acc_to_save['description']))
        assert_that(saved_acc['type'], is_(acc_to_save['type']))
        assert_that(saved_acc['currency'], is_(acc_to_save['currency']))
        assert_that(saved_acc['balance'], is_(acc_to_save['balance']))
    finally:
        if saved_acc:
            finlab_client.delete_account(saved_acc[id])
