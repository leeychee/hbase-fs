/*
 * Copyright 2015 chunhui.
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
import java.io.IOException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author chunhui
 */
public class TempFileManagerTest {

	private static final Logger log = LoggerFactory.getLogger(TempFileManagerTest.class);

	private final TempFileManager tmpFileGen = new TempFileManager();

	@BeforeClass
	public static void beforeClass() {

	}

	@Test
	public void testGenerateFile() throws IOException {
		String filename;
		for (int i = 0; i < 20; i++) {
			filename = tmpFileGen.generateFile();
			assertNotNull("random filename should not be null.", filename);
			File file = new File(filename);
			assertTrue("File should exist.", file.exists());
			log.debug(filename);
		}
		tmpFileGen.clear();
	}
}
