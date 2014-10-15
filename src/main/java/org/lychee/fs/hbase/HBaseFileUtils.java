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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * A utils for user to use the Hbase file system.
 * 
 * @author chunhui
 */
public class HBaseFileUtils {
 
    private static final Logger log = LoggerFactory.getLogger(HBaseFileUtils.class);
    /**
     * 
     * upload local file to the hbase file system.
     * 
     * @param localFile
     * @return the identifier of the file in the hbase file system.
     * @throws IOException 
     */
    public static String upload(File localFile) throws IOException {
        String md5 = md5Hex(localFile);
        try (InputStream is = new FileInputStream(localFile)) {
            HBaseFile hbFile = HBaseFile.Factory.buildHBaseFile(md5, localFile.getName());
            if (hbFile.integrity()) {
                // do nothing. the file is already store in hbase cluster.
                // TODO handle upload the file on the same time.
                // TODO update description.
                log.debug("The local file which md5 is " + md5 + ", has already been uploaded.");
            } else {
                // When the hbase file is not integrity, delete it first,
                hbFile.delete();
                // then reupload.
                hbFile = HBaseFile.Factory.buildHBaseFile(md5, localFile.getName());
                try (OutputStream ops = new HBaseFileOutputStream(hbFile)) {
                    IOUtils.copy(is, ops);
                }
            }
        }
        return md5;
    }
    
    /**
     * 
     * download the file in the hbase file system to the local file.
     * the file must dose exist and integrity.
     * 
     * @param identifier
     * @param localFile
     * @throws IOException 
     */
    public static void download(String identifier, File localFile) throws IOException {
        HBaseFile hbFile = HBaseFile.Factory.buildHBaseFile(identifier);
        if (!hbFile.integrity()) {
            throw new IOException("Fail to read the file in the hbase file system.");
        }
        try (InputStream is = new HBaseFileInputStream(hbFile)) {
            FileUtils.deleteQuietly(localFile);
            try (OutputStream os = new FileOutputStream(localFile)) {
                IOUtils.copy(is, os);
            }
        }
    }
    
    private static String md5Hex(File file) throws IOException {
        String md5;
        try (InputStream is = new FileInputStream(file)) {
            md5 = DigestUtils.md5Hex(is);
        }
        return md5;
    }
}
