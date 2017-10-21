from pymongo import MongoClient
from mongodb_exp.test_set import TestSet
from mongodb_exp.news_test import NewsTest
from mongodb_exp.pagecount_test import PageCountTest


class MongoTest:

    def __init__(self):
        self.client = MongoClient("localhost", 27017)
        self.db = self.client["test"]
        self.data = None

    def test(self):
        test_case = TestSet(self.client)
        test_case.load_class(PageCountTest,
                        ['case_insert', 'case_query', 'case_build_index', 'case_query'])
        test_case.run_test()


if __name__ == "__main__":
    test = MongoTest()
    test.test()

