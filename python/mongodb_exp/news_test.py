import time, glob, pymongo


class NewsTest:

    def __init__(self, mongo_client):
        self.data = None
        self.client = mongo_client
        self.db = self.client['test']
        self.collection = None

    def install(self):
        # init collection
        self.collection = self.db["data"]

    def uninstall(self):
        self.collection.delete_many({})

    def case_insert(self):
        # write files to mongodb
        start = time.time()
        for file in glob.glob('dataset/news/*/*.txt', recursive=True):
            _, _, cate, file_name = file.split('/')
            text = open(file, errors='ignore').read()
            doc = {
                'key': file_name.split('.')[0],
                'value': text,
                'category': cate
            }
            self.collection.insert_one(doc)
        size = self.data.get_size()
        end = time.time()
        # show document count
        print("{} documents {} bytes are inserted, time used: {} ms".
              format(self.collection.count(), size, (end-start)*1000))

    def case_query(self):
        # query by category
        start = time.time()
        rs = self.collection.find({'category': 'business'})
        end = time.time()
        print("query data {} , time used: {} ms".format(rs.count(), (end-start)*1000))

    def case_build_index(self):
        self.db.profiles.create_index([('category', pymongo.ASCENDING)])
        print("built index on 'category'")
