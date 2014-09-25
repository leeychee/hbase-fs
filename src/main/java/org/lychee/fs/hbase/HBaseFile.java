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
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.lychee.fs.hbase.HBaseFileConst.*;

/**
 *
 * the descriptor of a HBase file. <br/>
 *
 * A Hbase file **must** have a identifier. I recommend to use the md5 of the
 * origin file. A description is optinal, it will help you to use the file in
 * the future.
 *
 * @author chunhui
 * @see HBaseFileInputStream
 * @see HBaseFileOutputStream
 */
public class HBaseFile {

    private static final Logger log = LoggerFactory.getLogger(HBaseFile.class);

    /**
     * A facotry for building HBase file.
     * 
     * @author chunhui
     * @see HBaseFile
     */
    public static class Factory {

        private Factory() {
        }

        /**
         *
         * Create a HBase file. <br/>
         * it's like the buildHBaseFile(String identifier, String desc), except
         * when the identifier does not exist in hbase cluster, it will not set
         * the description.
         *
         * @param identifier
         * @return
         * @see buildHBaseFile(String identifier, String desc)
         */
        public static HBaseFile buildHBaseFile(String identifier) {
            return buildHBaseFile(identifier, null);
        }

        /**
         *
         * Create a HBase file. <br/>
         * if the identifier exists in hbase cluster, then read the meta info;
         * else, return a new hbase file with the given identifier and desc.
         *
         * @param identifier the identifier of the hbase file
         * @param desc the description of the hbase file, eg. file name
         * @return
         */
        public static HBaseFile buildHBaseFile(String identifier, String desc) {
            if (StringUtils.isEmpty(identifier)) {
                throw new IllegalArgumentException("You need to provide a identifier.");
            }
            HBaseFile hbFile = new HBaseFile(identifier);
            if (StringUtils.isNotEmpty(desc)) {
                hbFile.setDesc(desc);
            }
            try {
                HBaseFileHelper.readMeta(hbFile);
            } catch (IOException ex) {
                log.error("Fail to read HBase file from hbase cluster.", ex);
            }
            return hbFile;
        }
    }

    /**
     * Use the Factory instead.
     * 
     * @param identifier 
     * @see Factory
     */
    HBaseFile(String identifier) {
        this.identifier = identifier;
        this.status = NEW;
        this.createTime = System.currentTimeMillis();
    }

    public void delete() throws IOException {
        if (exists()) {
            HBaseFileHelper.delete(this);
        }
    }

    public boolean exists() {
        return status == CREATED || status == TRANSIT || status == INTEGRITY;
    }
    
    public boolean integrity() {
        return status == INTEGRITY;
    }

    public String getIdentifier() {
        return identifier;
    }

    public long getSize() {
        return this.size;
    }

    public String getDesc() {
        return this.desc;
    }

    public long getCreateTime() {
        return createTime;
    }
    
    @Override
    public String toString() {
        return identifier + "\t" + new Date(createTime) + "\t" + desc + "\t" + size;
    }

// --------------------------- protected ----------------------------
    
    protected boolean isNew() {
        return status == NEW;
    }

    protected boolean isCreated() {
        return status == TRANSIT;
    }

    protected boolean isTransit() {
        return status == TRANSIT;
    }

    protected void setStatus(byte status) {
        this.status = status;
    }

    protected byte getStatus() {
        return this.status;
    }

    protected void setSize(long size) {
        this.size = size;
    }

    protected void setShards(int shards) {
        this.shards = shards;
    }

    protected int getShards() {
        return this.shards;
    }

    protected void setDesc(String desc) {
        this.desc = desc;
    }

    protected void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

// --------------------------- private ------------------------------
    private final String identifier;

    private String desc = "";

    private byte status;

    private long createTime = 0L;

    private long size = 0L;

    private int shards = 0;

}
