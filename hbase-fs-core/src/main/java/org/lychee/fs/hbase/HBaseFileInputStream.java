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
import java.io.InputStream;

/**
 *
 * the InputStream impl of a HBase file. <br/>
 * 
 * Read a hbase file.like a common inputstream.
 * 
 * @author chunhui
 * @see HBaseFile
 * @see HBaseFileOutputStream
 */
public class HBaseFileInputStream extends InputStream {
    
    private final HBaseFile hbFile;
    private int shard = 1;
    
    private byte[] cache;
    private int cursor;
    
    public HBaseFileInputStream(HBaseFile hbFile) {
        this.hbFile = hbFile;
    }

    @Override
    public int read() throws IOException {
        if (cache == null) {
            cache = readCacheFromHBase();
            cursor = 0;
            // read completed.
            if (cache == null || cache.length == 0) return -1;
        }
        byte b = -1;
        if (cursor < cache.length) {
            b = cache[cursor++];
        }
        if (cursor >= cache.length) {
            cache = null;
        }
        return b & 0xff;
    }
    
//    @Override
//    public int available() throws IOException {
//        return (int) hbFile.getSize();
//    }
    
    private byte[] readCacheFromHBase() throws IOException {
        return HBaseFileHelper.readShard(hbFile, shard++);
    }
    
}
