hbase-fs
========

# A Simple file system build on HBase. 

hbase-fs just like normal local file system. It has three main class:

- HBaseFile
- HBaseFileInputStream
- HBaseFileOutputStream

With special `InputStream` and `OutputStream` implementation, you can easily 
read and write file on HBase.

### Read
1. `HBaseFile.Factory.buildHBaseFile(identifier)` get a HBaseFile instance;
-  `new HBaseFileInputStream(hbFile)` get InputStream;
-  read from **the InputStream**

### Write
1. like Read's step 1;
-  `new HBaseFileOutputStream(hbFile)` get OutputStream;
-  write to **the OutputStream**
-  remember to **close**

Now, it's just a **Prototype**. 

