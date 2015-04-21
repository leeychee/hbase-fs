/*
 * Copyright 2014 chunhui.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lychee.fs.hbase;

import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * Use the status to make sure the file is integrity.
 *
 * @author chunhui
 */
class HBaseFileConst {
    
    // -----------------------------STATUS----------------------------------

    /**
     * a new hbase file on local, does not put in the bhbase cluster.
     */
    static final byte NEW = 0x00;

    /**
     * a hbase file has been put in the hbase cluster, but has not store any
     * bytes.
     */
    static final byte CREATED = 0x01;

    /**
     * a hbase file in transiting, but not all snippets has been finished.
     */
    static final byte TRANSIT = 0x10;

    /**
     * a integrity hbase file with all snippets.
     */
    static final byte INTEGRITY = 0x20;

    // -------------------------------TABLE-------------------------------
    /**
     * The name of the table which been used to store the file.
     */
    final static String FILE_SYSTEM_TABLE_NAME = "FILE_SYSTEM_TABLE";
    
    /**
     * the column family for meta info of the file.
     */
    final static byte[] CF_META = Bytes.toBytes("m");
    final static byte[] M_DESC = Bytes.toBytes("desc");
    final static byte[] M_SIZE = Bytes.toBytes("size");
    final static byte[] M_SHARDS = Bytes.toBytes("shards");
    final static byte[] M_STATUS = Bytes.toBytes("status");
    final static byte[] M_CREATE_TIME = Bytes.toBytes("ctime");
//    private final static byte[] M_MODIFY_TIME= Bytes.toBytes("mtime");
    
    /**
     * the column family for bytes.  <br/>
     * In this CF, we will use number [1, +Infinity) as the column name.they 
     * are in order of the file bytes.
     */
    final static byte[] CF_SHARDS = Bytes.toBytes("s");

}
