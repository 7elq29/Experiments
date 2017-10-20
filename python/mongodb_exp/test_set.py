class TestSet:

    def __init__(self, mongo_client):
        self.cases = []
        self.install = None
        self.uninstall = None
        self.cls = None
        self.client = mongo_client

    def register_install(self, operation):
        self.install = operation

    def register_uninstall(self, operation):
        self.uninstall = operation

    def run_test(self):
        if not self.cls:
            raise RuntimeError("No class is specified")
        getattr(self.cls, self.install)()
        while len(self.cases) > 0:
            getattr(self.cls, self.cases.pop(0))()
        getattr(self.cls, self.uninstall)()

    def register(self, case):
        if isinstance(case, str):
            self.cases.append(case)
        elif isinstance(case, list):
            self.cases.extend(case)

    def load_class(self, cls, cases):
        if not getattr(cls, 'install') or not getattr(cls, 'uninstall'):
            raise RuntimeError("Test class must have 'install' and 'uninstall' method")
        self.cls = cls(self.client)
        self.register_install('install')
        self.register_uninstall('uninstall')
        self.register(cases)



