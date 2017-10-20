import time, threading, queue, pymongo


class PageCountTest:

    FILE_NAME = 'dataset/pagecount/pagecount-2017-09-part0'
    PROCESS_NUM = 8
    BUFFER_SIZE = 50000

    def __init__(self, mongo_client):
        self.data = None
        self.client = mongo_client
        self.db = self.client['test']
        self.collection = None
        self.size = 0
        self.lock = threading.Lock()
        self.q = queue.Queue()

    def install(self):
        # init collection
        self.collection = self.db["data"]
        self.collection.delete_many({})

    def uninstall(self):
        self.collection.delete_many({})

    def case_insert(self):
        # write files to mongodb
        start = time.time()
        for i in range(8):
            t = threading.Thread(target=self.worker)
            t.start()

        for chunk in self._get_chunks(PageCountTest.FILE_NAME):
            self.q.put((PageCountTest.FILE_NAME, chunk))

        t = threading.Thread(target=self.monitor)
        t.start()

        self.q.join()

        end = time.time()
        # show document count
        print("{} documents {} bytes are inserted, time used: {} ms".
              format(self.collection.count(), self.size, (end - start) * 1000))

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

    def _process(self, file, chunk):
        f = open(file)
        f.seek(chunk[0])
        s = 0
        docs = []
        for line in f.read(chunk[1]).splitlines():
            s += len(line)
            if s > 5000:
                self.lock.acquire()
                self.size += s
                s = 0
                self.lock.release()
            # If there are more than BUFFER_SIZE documents in the buffer, insert into mongodb
            if len(docs) > PageCountTest.BUFFER_SIZE:
                self.collection.insert(docs)
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
        self.collection.insert(docs)
        self.lock.acquire()
        self.size += s
        self.lock.release()

    def worker(self):
        while 1:
            args = self.q.get()
            if args is None:
                print("task done")
                break
            self._process(*args)
            self.q.task_done()

    def monitor(self):
        start = time.time()
        while not self.q.empty():
            time.sleep(5)
            mb = self.size / 1024 / 1024
            print("{} MB".format(mb))
        end = time.time()
        print("finish  reading in {} s".format(end - start))




