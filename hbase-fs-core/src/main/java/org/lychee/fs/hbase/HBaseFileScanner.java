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
import java.util.ArrayList;
import java.util.List;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Scan the HBase File System. <br/>
 * You can not new it, just can get it from the HBaseFileSystem.<br/>
 * It's not thread safe!
 * 
 * @author chunhui
 * @see HBaseFileSystem
 */
public class HBaseFileScanner {

    private final static Logger log = LoggerFactory.getLogger(HBaseFileScanner.class);

    private final ResultScanner scanner;

    HBaseFileScanner(ResultScanner scanner) {
        this.scanner = scanner;
    }

    /**
     * scan the next *size* files. <br/>
     * 
     * @param size
     * @return 
     */
    public List<HBaseFile> next(int size) {
        List<HBaseFile> hbFiles = null;
        if (scanner == null) 
            return new ArrayList<HBaseFile>();
        try {
            Result[] rs;
            if ((rs = scanner.next(size)) != null) {
                hbFiles = to(rs);
            } else {
                scanner.close();
            }
        } catch (IOException ex) {
            scanner.close();
            log.error("Fail to read next Hbase file.", ex);
        }
        return hbFiles == null ? new ArrayList<HBaseFile>() : hbFiles;
    }

    /**
     * scan the next *1* files.
     * 
     * @return 
     */
    public HBaseFile next() {
        List<HBaseFile> hbFiles = next(1);
        return hbFiles.isEmpty() ? null : hbFiles.get(0);
    }

    /**
     * close the scanner.
     */
    public void close() {
        scanner.close();
    }

    private HBaseFile to(Result result) {
        HBaseFile hbFile = new HBaseFile(Bytes.toString(result.getRow()));
        HBaseFileHelper.readMeta(result, hbFile);
        return hbFile;
    }
    
    private List<HBaseFile> to(Result[] rs) {
        List<HBaseFile> hbFiles = new ArrayList<HBaseFile>();
        for (Result r : rs)
            hbFiles.add(to(r));
        return hbFiles;
    }
}
