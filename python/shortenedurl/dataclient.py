from pymongo import MongoClient
import pymongo


class DataClient:

    def __init__(self):
        self.client = MongoClient("localhost", 32773)
        MongoClient()
        self.collection = self.client['shortened_url']["url_map"]
        self.collection.create_index([('shortened_url', pymongo.ASCENDING)])

    def put(self, shortened_url, url):
        data = self.collection.find({'shortened_url': shortened_url})
        if data is not None and data.count() > 1:
            raise IOError("Duplicated url: {}".format(shortened_url))
        if data.count() == 1:
            _id = data.next()['_id']
            self.collection.update({
                '_id': _id
            }, {
                '$set': {
                    'url': url
                }
            })
        else:
            self.collection.insert_one({
                'shortened_url': shortened_url,
                'url': url
            })

    def get(self, shortened_url):
        return self.collection.find_one({'shortened_url': shortened_url})["url"]
