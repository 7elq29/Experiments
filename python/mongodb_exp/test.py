import sys, time, threading

size = 0

f = open('dataset/pagecount/pagecounts-2017-09-views-ge-5-totals')


def monitor():
    start = time.time()
    buffer = 0
    while True:
        time.sleep(5)
        if buffer == size:
            break
        print("{} MB".format(size / 1024 / 1024))
        buffer = size
    end = time.time()
    print("Finish reading in {} s".format(end-start))

t = threading.Thread(target=monitor)
t.setDaemon(False)
t.start()

cfile = None
index = 0

while True:
    if not cfile:
        cfile = open('pagecount-2017-09-part'+str(index), 'w+')
        index += 1
    line = f.readline()
    if not line:
        cfile.close()
        break
    cfile.write(line)
    size += len(line)
    if (size/1024/1024) > 50:
        cfile.close()
        size = 0
        cfile = open('pagecount-2017-09-part'+str(index), 'w+')
        index += 1
