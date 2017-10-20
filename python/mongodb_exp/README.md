# Purpose

The purpose of this project is to do some functional performance of MongoDB 
and measure the performance.

# How to use

1. Install mongoDB > version 3.5

2. Config connection in mongo_test.py

`self.client = MongoClient("localhost", 27017)`

3. Run mongo_test.py


# Report

*Data Sets*
1. Wiki page count data: 77.4 MB, 2,952,892 records.
2. BBC news data: 5 categories, 2,225 files in total.

*Insertion*

To insert the page count data, first the file will be split into chunks, each of them is 
  1 MB large, and the cursor of each chunk is saved in a thread safe queue. 
  Then *n* threads will be created and each of them will do the following operations,
1. grab a chunk from the queue.
2. for each line in the chunk, create a corresponding mongoDB document, put it into buffer.
3. when buffer size exceed *m* documents, write the buffer to mongoDB.

With *n* equals to 8 and *m* equals to 50,000, the whole process (read file and write to mongo) takes about 180 seconds to finish.
 