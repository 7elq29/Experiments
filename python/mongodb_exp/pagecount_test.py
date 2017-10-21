import time, threading, queue, pymongo, multiprocessing
from multiprocessing import Process, Pool
from pymongo import MongoClient


class PageCountTest:

    FILE_NAME = 'dataset/pagecount/pagecount-2017-09-part0'
    PROCESS_NUM = cpu_count = multiprocessing.cpu_count()
    BUFFER_SIZE = 10000

    size = 0
    running_process = 0

    def __init__(self, mongo_client):
        self.client = mongo_client
        self.db = self.client['test']
        self.data = None
        self.collection = None
        self.size = 0
        self.lock = threading.Lock()

    def install(self):
        # init collection
        self.collection = self.db["data"]
        m = self.db['monitor']
        m.delete_many({})
        m.insert_one({'running_process': 0, 'size': 0})
        self.collection.delete_many({})

    def uninstall(self):
        self.collection.delete_many({})

    def case_insert(self):
        # write files to mongodb
        start = time.time()

        q = [[] for _ in range(PageCountTest.PROCESS_NUM)]
        index = 0
        for chunk in self._get_chunks(PageCountTest.FILE_NAME):
            q[index].append(chunk)
            index = index + 1 if index != PageCountTest.PROCESS_NUM-1 else 0

        pool = Pool(processes=PageCountTest.PROCESS_NUM)
        pool.map_async(PageCountTest.worker, list(zip([PageCountTest.FILE_NAME]*PageCountTest.PROCESS_NUM, q)))

        if __debug__:
            t = threading.Thread(target=self.monitor)
            t.setDaemon(True)
            t.start()

        pool.close()
        pool.join()

        end = time.time()
        # show document count
        print("{} documents {} bytes are inserted, time used: {} ms ({} inserts / sec)".
              format(self.collection.count(), self.size, (end - start) * 1000, self.collection.count()/(end-start)))

    def case_query(self):
        # query by category
        start = time.time()
        rs = self.collection.find({'project': 'aa.m'})
        end = time.time()
        print("query data {} , time used: {} ms".format(rs.count(), (end - start) * 1000))

    def case_build_index(self):
        self.db.profiles.create_index([('project', pymongo.ASCENDING)])
        print("built index on 'project'")

    def _get_chunks(self, file, size=1024 * 1024):
        f = open(file, 'rb')
        while True:
            start = f.tell()
            f.seek(size, 1)
            s = f.readline()
            yield start, f.tell() - start
            if not s:
                break

    @staticmethod
    def _process(file, chunk, db):
        collection = db['data']
        monitor = db['monitor']
        f = open(file)
        f.seek(chunk[0])
        s = 0
        docs = []
        buffer_size = 1024 * 1024
        for line in f.read(chunk[1]).splitlines():
            s += len(line)
            if s > buffer_size:
                monitor.update_one({}, {
                    '$inc': {
                        'size': s
                    }
                })
                s = 0
            # If there are more than BUFFER_SIZE documents in the buffer, insert into mongodb
            if len(docs) > PageCountTest.BUFFER_SIZE:
                collection.insert(docs)
                docs = []
            info = line.split(' ')
            if len(info) < 3:
                continue
            project, page, count = info
            docs.append({
                'key': page,
                'value': count,
                'project': project
            })
        collection.insert(docs)
        monitor.update_one({}, {
            '$inc': {
                'size': s
            }
        })

    @staticmethod
    def worker(args):
        PageCountTest._worker(*args)

    @staticmethod
    def _worker(file, chunks):
        client = MongoClient("localhost", 27017)
        db = client['test']
        db['monitor'].update_one({}, {
            '$inc': {
                'running_process': 1
            }
        })
        PageCountTest.running_process += 1

        for chunk in chunks:
            PageCountTest._process(file, chunk, db)
        db['monitor'].update_one({}, {
            '$inc': {
                'running_process': -1
            }
        })
        PageCountTest.running_process -= 1

    def monitor(self):
        start = time.time()
        time.sleep(5)
        while self.db['monitor'].find_one()['running_process'] > 0:
            mb = self.db['monitor'].find_one()['size'] / 1024 / 1024
            print("{} MB".format(mb))
            time.sleep(5)
        end = time.time()
        print("finish  reading in {} s".format(end - start))
