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
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author chunhui
 */
public class HBaseFileTest {
    private static final Logger log = LoggerFactory.getLogger(HBaseFileTest.class);
    
	private final TempFileManager tfg = new TempFileManager();
    private final String uploadPath = tfg.getTempDir();
    private final String outPath = uploadPath + File.separator + "download" + File.separator;
    
    public HBaseFileTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() throws IOException {
		tfg.generateFiles(20);
        FileUtils.deleteDirectory(new File(outPath));
        FileUtils.forceMkdir(new File(outPath));
    }
    
    @After
    public void tearDown() throws IOException {
		tfg.clear();
    }

    @Test
    public void testHBaseFile() throws IOException {
        final File uploadFolder = new File(uploadPath);
        if (!uploadFolder.isDirectory()) {
            fail("please enter a correct dir to upload!");
        }
        Collection<File> uploadFiles = FileUtils.listFiles(uploadFolder, null, false);
        if (uploadFiles == null || uploadFiles.isEmpty()) {
            fail("please enter a correct dir which has files.");
        }
        for (File uploadFile : uploadFiles) {
            if (uploadFile.isFile())
                testAHBaseFile(uploadFile);
        }
    }
    
    private void testAHBaseFile(File localFile) throws IOException {
        String identifier = HBaseFileUtils.upload(localFile);
        File downloadFile = new File(outPath + localFile.getName());
        HBaseFileUtils.download(identifier, downloadFile);
		String localMd5, downloadMd5;
		try (InputStream lis = new FileInputStream(localFile)) {
			localMd5 = DigestUtils.md5Hex(lis);
		}
		try (InputStream dis = new FileInputStream(downloadFile)) {
			downloadMd5 = DigestUtils.md5Hex(dis);
		}
		assertEquals(localMd5, downloadMd5);
		log.debug("Success to upload & download file: " + localFile.getName());
    }
    
    private void testDel(HBaseFile hbFile) throws IOException {
        if (hbFile.exists())
            hbFile.delete();
    }
    
}
