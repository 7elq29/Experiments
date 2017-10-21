# Purpose

The purpose of this project is to do some functional performance of MongoDB 
and measure the performance.

# How to use

1. Install mongoDB > version 3.5

2. unzip datatset/news/Archive.zip and dataset/pagecount/pagecount-2017-09-part0.zip

3. Config connection in mongo_test.py

`self.client = MongoClient("localhost", 27017)`

4. Run mongo_test.py


# Report

*Data Sets*
1. Wiki page count data: 77.4 MB, 2,952,892 records.
2. BBC news data: 5 categories, 2,225 files in total.

*Insertion*

To insert the page count data, first the file is split into chunks, each of which is 
  1 MB large. 
  Then *n* subprocesses are created and assigned several chunks. Each of them will do the following operations,
1. for each line in the chunk, create a corresponding mongoDB document, put it into buffer.
2. when buffer size exceed *m* documents, write the buffer to mongoDB.

With *n* equals to 4 and *m* equals to 50,000, the whole process (read file and write to mongo) takes
 about 75 seconds to finish. (40000 inserts / sec)
 