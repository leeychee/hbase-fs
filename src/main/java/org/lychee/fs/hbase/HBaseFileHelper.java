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

import java.io.IOException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;

import static org.lychee.fs.hbase.HBaseFileConst.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Just for internal use. It will be replaced with another implemention.
 *
 * TODO need to imporve the performance. TODO need to be thread safe.
 *
 * @author chunhui
 */
class HBaseFileHelper {

    private static final Logger log = LoggerFactory.getLogger(HBaseFileHelper.class);

    private static HTableInterface fsTable;

    static {
        try {
            Configuration conf = HBaseConfiguration.create();
            HConnection connection = HConnectionManager.createConnection(conf);
            fsTable = connection.getTable(FILE_SYSTEM_TABLE_NAME);
        } catch (IOException ex) {
            String msg = "Fail to connect to the hbase cluster. Please recheck.";
            log.error(msg, ex);
            throw new RuntimeException(msg, ex);
        }
    }

    public static void saveOrUpdateMeta(HBaseFile hbFile) throws IOException {
        byte[] idBytes = Bytes.toBytes(hbFile.getIdentifier());
        Put put = new Put(idBytes);
        put.add(new KeyValue(idBytes, CF_META, M_DESC, Bytes.toBytes(hbFile.getDesc())));
        put.add(new KeyValue(idBytes, CF_META, M_SIZE, Bytes.toBytes(hbFile.getSize())));
        put.add(new KeyValue(idBytes, CF_META, M_SHARDS, Bytes.toBytes(hbFile.getShards())));
        put.add(new KeyValue(idBytes, CF_META, M_STATUS, new byte[]{hbFile.getStatus()}));
        put.add(new KeyValue(idBytes, CF_META, M_CREATE_TIME, Bytes.toBytes(hbFile.getCreateTime())));
        fsTable.put(put);
    }

    public static void readMeta(HBaseFile hbFile) throws IOException {
        Get get = new Get(Bytes.toBytes(hbFile.getIdentifier()));
        get.addFamily(CF_META);
        Result result = fsTable.get(get);
        if (result != null && !result.isEmpty()) {
            readMeta(result, hbFile);
        } else {
            hbFile.setStatus(NEW);
        }
    }

    public static void readMeta(Result result, HBaseFile hbFile) {
        hbFile.setDesc(Bytes.toString(result.getValue(CF_META, M_DESC)));
        hbFile.setSize(Bytes.toLong(result.getValue(CF_META, M_SIZE)));
        hbFile.setShards(Bytes.toInt(result.getValue(CF_META, M_SHARDS)));
        hbFile.setStatus(result.getValue(CF_META, M_STATUS)[0]);
        hbFile.setCreateTime(Bytes.toLong(result.getValue(CF_META, M_CREATE_TIME)));
    }

    public static void addShard(HBaseFile hbFile, byte[] shard) throws IOException {
        if (hbFile.isTransit() && shard != null && shard.length > 0) {
            byte[] idBytes = Bytes.toBytes(hbFile.getIdentifier());
            Put put = new Put(idBytes);
            byte[] shardNo = Bytes.toBytes(hbFile.getShards());
            put.add(CF_SHARDS, shardNo, shard);
            put.add(CF_META, M_SHARDS, shardNo);
            put.add(CF_META, M_SIZE, Bytes.toBytes(hbFile.getSize()));
            fsTable.put(put);
        }
    }

    public static byte[] readShard(HBaseFile hbFile, int shard) throws IOException {
        if (!hbFile.integrity()) {
            return null;
        }
        byte[] shards = null;
        byte[] idBytes = Bytes.toBytes(hbFile.getIdentifier());
        Get get = new Get(idBytes);
        get.addColumn(CF_SHARDS, Bytes.toBytes(shard));
        Result result = fsTable.get(get);
        if (result != null && !result.isEmpty()) {
            shards = result.getValue(CF_SHARDS, Bytes.toBytes(shard));
        }
        return shards;
    }

    public static void delete(HBaseFile hbFile) throws IOException {
        Delete del = new Delete(Bytes.toBytes(hbFile.getIdentifier()));
        fsTable.delete(del);
    }

    public static ResultScanner scan() throws IOException {
        return fsTable.getScanner(CF_META);
    }

}
