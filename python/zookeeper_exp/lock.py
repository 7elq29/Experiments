from kazoo.client import KazooClient
import re


class Lock:

    def __init__(self, client: KazooClient, path):
        self.path = path
        self.client = client
        self.real_path = None

    def try_lock(self):
        if self.real_path is None:
            self.real_path = self.client.create(self.path+"/lock-", b"", None, True, True)
        return self._is_first(self.client.get_children(self.path), self.real_path)

    def unlock(self):
        self.client.delete(self.real_path)

    def _is_first(self, children, name):
        l = [self.path+"/"+ele for ele in children if re.match('lock-.*', ele)]
        l.sort()
        return True if l[0] == name else False
