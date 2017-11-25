from kazoo.client import KazooClient
from zookeeper_exp.lock import Lock

zk1 = KazooClient(hosts='127.0.0.1:2182')
zk2 = KazooClient(hosts='127.0.0.1:2182')
zk1.start()
zk2.start()

path = "/zookeeper_exp/node"

## prepare data
zk1.ensure_path(path)
zk1.set(path, b'1')

## lock
lock1 = Lock(zk1, path)
if lock1.try_lock():
    zk1.set(path, b'2')
    print("zk1 set")
else:
    print("zk1 cannot lock "+path)


lock2 = Lock(zk2, path)
if lock2.try_lock():
    print(zk2.get(path)[0])
else:
    print("prevent reading from zk2")
lock1.unlock()

if lock2.try_lock():
    print(zk2.get(path)[0])
else:
    print("prevent reading from zk2")

lock2.unlock()