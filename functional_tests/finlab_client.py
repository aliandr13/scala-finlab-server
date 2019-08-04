import json
import requests

from urllib.parse import urljoin
from urllib.parse import urlparse
from urllib.parse import parse_qs
from urllib.parse import urlsplit


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

    def find_user_by_name(self, userName):
        return self.make_request("/users/{0}".format(userName))

    def get_user_endpoint(self):
        return self.make_request("/users")

    def create_user(self, user):
        return self.make_request("/users", user, 'POST')

    def update_user(self, user):
        return self.make_request("/users", user, 'PUT')

    def delete_user(self, user):
        return self.make_request("/users", user, 'DELETE')
