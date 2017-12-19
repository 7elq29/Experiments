import requests
from flask import Flask
from flask_restful import Resource, Api
from shortenedurl.dataclient import DataClient
from shortenedurl import http_request

app = Flask(__name__)
api = Api(app)


class ShortenedURL(Resource):

    def __init__(self):
        self.client = DataClient()
        self.hash = 1

    def get(self, url):
        u = self.client.get(url)
        return http_request.request(u, mock=True)

    def post(self, url):
        try:
            shortened_url = self._get_hash(self.hash)
            self.client.put(shortened_url, url)
            self.hash += 1
            return {'status': 'success', 'url': shortened_url}
        except IOError:
            return {'status': 'fail'}

    def _get_hash(self, inc):
        h = inc
        rs = ""
        while h > 0:
            num = h % 36
            rs = self._get_char(num) + rs
            h = int(h / 36)
        return rs

    def _get_char(self, num):
        return chr(ord('a') + num) if num<26 else chr(ord('0') + num - 26)


api.add_resource(ShortenedURL, '/<string:url>')

if __name__ == '__main__':
    app.run(debug=True)







