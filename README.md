hbase-fs
========

## Simple File System on HBase

### Description
Sometimes, when we use [HBase](http://hbase.apache.org/), we need to save files, especially very small files, such as images. HBase's API does not very friendly to do this. So, I try to build to simple file system on HBase, to simplify read & write file on HBase.

`hbase-fs` is a flat File System, not like the linux's file system, it does not have directory structure. All file has a identifier, which I recommend to use MD5, for reading & writing on the hbase-fs.

### How to use?
hbase-fs just like normal local file system. It has three main class:

- HBaseFile
- HBaseFileInputStream
- HBaseFileOutputStream

With special `InputStream` and `OutputStream` implementation, you can easily 
read and write file on HBase.

#### HBaseFile
I recommend to use md5 or SHA-1 of the file as the identifier, so you can easily
find out wether the content of a file has been stored in the hbase cluster.

#### Read
1. `HBaseFile.Factory.buildHBaseFile(identifier)` get a HBaseFile instance;
-  `new HBaseFileInputStream(hbFile)` get InputStream;
-  read from **the InputStream**

```java
// On JDK 7+
HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(md5);
try (InputStream is = new HBaseFileInputStream(hbf)) {
    // do something with the inputstream
}
```

#### Write
1. like Read's step 1;
-  `new HBaseFileOutputStream(hbFile)` get OutputStream;
-  write to **the OutputStream**
-  remember to **close**

```java
// On JDK 7+
HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(md5, desc);
try (OutputStream is = new HBaseFileOutputStream(hbf)) {
    // do something with the inputstream
}
```

Now, it's just a **Prototype**. 


## 基于 HBase 的文件系统

### 概述
有些时候,由于业务和环境的限制,我们需要使用 [HBase](http://hbase.apache.org/) 来存储文件,尤其是小文件,就像图片一类.然后. HBase 原生的 API 对于存取文件把并不是很友好.所以,我们尝试在 HBase 之上,构建一个简易的文件系统来简化我们的使用.

`hbase-fs` 是一个扁平状的文件系统, 没有类似于 linux 文件系统的那种目录结构. 我们目前也不打算构建这样的内容. 所有的文件通过唯一的标识符来描述, 所有的读写操作都依赖于这个操作符. 我们建议, 可以使用文件的 MD5 码, 作为文件的唯一标识符, 这样天然就具备了判断文件是否已经存入的功能. 

### 如何使用?
类似于 Java 里处理的通用办法, 我们使用特殊的InputStream, OutputStream 来实现对 hbase-fs 中文件读写. 主要有以下三个类. 

- HBaseFile
- HBaseFileInputStream
- HBaseFileOutputStream
 
#### HBaseFile
这是一个文件描述类, 用来描述存储在 hbae-fs 中的文件. 其中最主要的内容就是标识符(identifier), 他唯一确定了 hbase-fs 中一个文件. 我们强烈建议你使用文件的 MD5 值作为这个标识符. 通过使用 MD5, 文件系统天生就具有了识别重复文件的能力, 即使文件的名称已经改变. 

#### 读取文件
1. `HBaseFile.Factory.buildHBaseFile(identifier)` 获取一个 HBaseFile 的实例; 
-  `new HBaseFileInputStream(hbFile)` 获得 InputStream; 
-  从`InputStream`中读取数据;

```java
// On JDK 7+
HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(md5);
try (InputStream is = new HBaseFileInputStream(hbf)) {
    // do something with the inputstream
}
```

#### 写入文件
1. `HBaseFile.Factory.buildHBaseFile(identifier, desc)` 获取一个新的 HBasFile 的实例;
-  `new HBaseFileOutputStream(hbFile)` 获取 OutputStream;
-  将数据写入`OutputStream`
-  **注意** 将流关闭
  
```java
// On JDK 7+, 该语法会自动调用close(), 将流关闭.
HBaseFile hbf = HBaseFile.Factory.buildHBaseFile(md5, desc);
try (OutputStream is = new HBaseFileOutputStream(hbf)) {
    // do something with the inputstream
}
```


### 引用
[HBase](http://hbase.apache.org)
