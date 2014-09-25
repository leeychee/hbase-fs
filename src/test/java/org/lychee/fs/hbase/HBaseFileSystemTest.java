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
public class HBaseFileSystemTest {
    
    private static final Logger log = LoggerFactory.getLogger(HBaseFileSystemTest.class);
    
    private static HBaseFileSystem hbfs;
    
    public HBaseFileSystemTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        hbfs = HBaseFileSystem.instance();
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of instance method, of class HBaseFileSystem.
     */
    @Test
    public void testInstance() {
        System.out.println("instance");
        HBaseFileSystem fs1 = HBaseFileSystem.instance();
        HBaseFileSystem fs2 = HBaseFileSystem.instance();
        assertTrue(fs1 == fs2);
    }

    /**
     * Test of next method, of class HBaseFileSystem.
     */
    @Test
    public void testNext() {
        HBaseFileScanner scanner = hbfs.scan();
        HBaseFile hbFile;
        while ((hbFile = scanner.next()) != null) {
            log.info(hbFile.toString());
        }
    }

    /**
     * Test of count method, of class HBaseFileSystem.
     */
    @Test
    public void testCount() {
        System.out.println("count");
        // TODO has not been impl.
    }
    
}
