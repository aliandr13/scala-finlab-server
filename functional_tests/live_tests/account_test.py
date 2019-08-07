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


def test_get_account(finlab_client):
    acc_to_save = {
        "name": "create_new_acc to get",
        "userId": 0,
        "description": "some description 111",
        "accountTypeId": 1,
        "currencyId": 1,
        "balance": 10.5
    }

    saved_acc = None

    try:
        # create new account
        response = finlab_client.create_account(acc_to_save)
        saved_acc = response.json()
        assert_that(response.status_code, is_(200))
        assert_that((saved_acc['accountId']), not_none())

        # get account saved previously
        response = finlab_client.get_account_by_id(saved_acc['accountId'])
        get_acc = response.json()
        # check
        assert_that(get_acc['accountId'], is_(saved_acc['accountId']))
        assert_that(get_acc['name'], is_(acc_to_save['name']))
        assert_that(get_acc['userId'], is_(acc_to_save['userId']))
        assert_that(get_acc['description'], is_(acc_to_save['description']))
        assert_that(get_acc['accountTypeId'], is_(acc_to_save['accountTypeId']))
        assert_that(get_acc['currencyId'], is_(acc_to_save['currencyId']))
        assert_that(get_acc['balance'], is_(acc_to_save['balance']))
    finally:
        if saved_acc:
            # clean up
            finlab_client.delete_account(saved_acc['accountId'])


def test_create_account(finlab_client):
    acc_to_save = {
        "name": "create_new_acc",
        "userId": 0,
        "description": "some description",
        "accountTypeId": 1,
        "currencyId": 1,
        "balance": 10.5
    }

    saved_acc = None

    try:
        response = finlab_client.create_account(acc_to_save)
        saved_acc = response.json()
        assert_that(response.status_code, is_(200))
        assert_that(saved_acc['name'], is_(acc_to_save['name']))
        assert_that(saved_acc['userId'], is_(acc_to_save['userId']))
        assert_that(saved_acc['description'], is_(acc_to_save['description']))
        assert_that(saved_acc['accountTypeId'], is_(acc_to_save['accountTypeId']))
        assert_that(saved_acc['currencyId'], is_(acc_to_save['currencyId']))
        assert_that(saved_acc['balance'], is_(acc_to_save['balance']))
        assert_that((saved_acc['accountId']), not_none())
    finally:
        if saved_acc:
            finlab_client.delete_account(saved_acc['accountId'])


def test_update_account(finlab_client):
    acc_to_save = {
        "name": "create_new_acc to get",
        "userId": 0,
        "description": "some description 111",
        "accountTypeId": 1,
        "currencyId": 1,
        "balance": 10.5
    }

    saved_acc = None

    try:
        # create new account
        response = finlab_client.create_account(acc_to_save)
        saved_acc = response.json()
        assert_that(response.status_code, is_(200))
        assert_that((saved_acc['accountId']), not_none())

        # update some params
        saved_acc['description'] = "updated description"
        saved_acc['accountTypeId'] = 2
        saved_acc['currencyId'] = 3
        saved_acc['balance'] = 555.666

        # update account
        response = finlab_client.update_account(saved_acc)
        assert_that(response.status_code, is_(200))

        # get account saved previously
        response = finlab_client.get_account_by_id(saved_acc['accountId'])
        get_acc = response.json()
        # check
        assert_that(get_acc['accountId'], is_(saved_acc['accountId']))
        assert_that(get_acc['name'], is_(saved_acc['name']))
        assert_that(get_acc['userId'], is_(saved_acc['userId']))
        assert_that(get_acc['description'], is_("updated description"))
        assert_that(get_acc['accountTypeId'], is_(2))
        assert_that(get_acc['currencyId'], is_(3))
        assert_that(get_acc['balance'], is_(555.666))
    finally:
        if saved_acc:
            # clean up
            finlab_client.delete_account(saved_acc['accountId'])


def test_delete_account(finlab_client):
    acc_to_save = {
        "name": "create_new_acc to get",
        "userId": 0,
        "description": "some description 111",
        "accountTypeId": 1,
        "currencyId": 1,
        "balance": 10.5
    }

    saved_acc = None

    try:
        # create new account
        response = finlab_client.create_account(acc_to_save)
        saved_acc = response.json()
        assert_that(response.status_code, is_(200))
        accountId = saved_acc['accountId']
        assert_that(accountId, not_none())

        # get account saved previously
        del_response = finlab_client.delete_account(saved_acc['accountId'])
        assert_that(del_response.status_code, is_(204))

        saved_acc = None

        get_response = finlab_client.get_account_by_id(accountId)
        assert_that(get_response.status_code, is_(404))
    finally:
        if saved_acc:
            # clean up
            finlab_client.delete_account(saved_acc['accountId'])
