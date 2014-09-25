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
import org.apache.hadoop.hbase.client.ResultScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * The HBase File System. <br/>
 *
 * HBase File System is a flat fs, it does not have a file tree like linux file
 * system. If you need one, You should manage it yourself.
 * 
 * HBaseFileSystem provide some API to see the file system's status.
 * 
 * It's not working now.
 * 
 * @author chunhui
 * @see HBaseFile
 */
class HBaseFileSystem {
    
    private final static Logger log = LoggerFactory.getLogger(HBaseFileSystem.class);
    private static HBaseFileSystem fs;
    
    private HBaseFileSystem() {}
    
    public synchronized static HBaseFileSystem instance() {
        if (fs == null) {
            fs = new HBaseFileSystem();
            log.debug("HBase File System has been init.");
        }
        return fs;
    }
    
    /**
     * 
     * read the next hbase file.
     * 
     * @return 
     */
    public HBaseFileScanner scan() {
        HBaseFileScanner hrs = null;
        try {
            ResultScanner rs = HBaseFileHelper.scan();
            hrs = new HBaseFileScanner(rs);
        } catch (IOException ex) {
            log.error("Fail to scan.", ex);
        }
        return hrs;
    }
    
    public long count() {
        return 0L;
    }
}

