hbase-fs-rest
=============

[hbase-fs](http://leeychee.github.io/hbase-fs) is a InputStream/OutputStream lib for reading & writing file 
on [HBase](http://hbase.apache.org). It's not very convenient for end-user. So, we build a HTTP-REST Service to 
simplify the operation.

Basically, we use HEAD, GET, PUT, POST, DELETE to manipulate files. The request like this:
> curl -X HEAD http://${host}:${port}/${version}/fs/${identifier}?${paras}

 OP     | PARAS          | DESC                              |
--------|----------------|-----------------------------------|
HEAD    |                | Test if the file exists, 404 or 200|
GET     |                | Get the file                      |
GET     | details=true   | Get json of the file details      |
PUT     | desc="blabla"  | add new file desc                 |
POST    | desc="blabla"  | add new file with desc            |
DELETE  |                | delete the file                   |

