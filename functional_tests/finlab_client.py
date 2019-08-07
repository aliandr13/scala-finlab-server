import json
import requests

from urllib.parse import urljoin
from urllib.parse import urlparse
from urllib.parse import parse_qs
from urllib.parse import urlsplit

# VERBS
PUT = 'PUT'
POST = 'POST'
DELETE = 'DELETE'

# endpoints
USERS = "/users"
USERS_PARAM = "/users/{0}"
ACCOUNTS = "/accounts"
ACCOUNTS_PARAM = "/accounts/{0}"


class FinlabClient(object):

    def __init__(self, url='http://localhost:9090'):
        self.index_url = url
        self.headers = {
            'Accept': 'application/json, text/plain',
            'Content-Type': 'application/json'
        }

        self.authHeader = None
        self.session = requests.Session()

    def url(self, path):
        return urljoin(self.index_url, path)

    def make_request(self, path, ob=None, method='GET', headers={}, **kw):
        hs = dict(dict(self.headers, **headers), **{"Authorization": self.authHeader})
        u = self.url(path)
        response = None
        if ob is None:
            response = self.session.request(method, u, headers=hs, **kw)
        else:
            response = self.session.request(method, u, headers=hs, data=json.dumps(ob), **kw)
        if "Authorization" in response.headers:
            self.authHeader = response.headers['Authorization']
        return response

    ##### users endpoints

    def get_user_endpoint(self):
        return self.make_request(USERS)

    def find_user_by_name(self, userName):
        return self.make_request(USERS_PARAM.format(userName))

    def create_user(self, user):
        return self.make_request(USERS, user, POST)

    def update_user(self, user):
        return self.make_request(USERS, user, PUT)

    def delete_user(self, user):
        return self.make_request(USERS, user, DELETE)

    ##### users endpoints

    ##### accounts endpoints

    def get_account_endpoint(self):
        return self.make_request(ACCOUNTS)

    def get_account_by_id(self, id):
        return self.make_request(ACCOUNTS_PARAM.format(id))

    def create_account(self, acc):
        return self.make_request(ACCOUNTS, acc, POST)

    def update_account(self, acc):
        return self.make_request(ACCOUNTS, acc, PUT)

    def delete_account(self, acc_id):
        return self.make_request(ACCOUNTS_PARAM.format(acc_id), None,  DELETE)

    ##### accounts endpoints
