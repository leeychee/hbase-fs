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

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author chunhui
 */
public class TempFileManager {

	private static final Logger log = LoggerFactory.getLogger(TempFileManager.class);

	private final String tmpDir;

	private final Random random = new Random();

	public TempFileManager() {
		this.tmpDir = FileUtils.getTempDirectoryPath() + File.separator +
				"hfs_test_" + RandomStringUtils.randomNumeric(5);
	}

	String getTempDir() {
		return tmpDir;
	}

	void clear() throws IOException {
		FileUtils.forceDelete(new File(tmpDir));
	}

	String generateFile() throws IOException {
		return generateFile(random.nextInt(10) + 10);
	}

	String generateFile(int sizeInMB) throws IOException {
		String filename = "upload_" + RandomStringUtils.randomNumeric(8);
		String filepath = tmpDir + File.separator + filename;
		File file = new File(filepath);
		FileUtils.touch(file);
		try (OutputStream fos = new BufferedOutputStream(new FileOutputStream(file))) {
			byte[] randomBytes = new byte[1024];
			int count = sizeInMB * 8;
			for (int i = 0; i < count; i++) {
				random.nextBytes(randomBytes);
				fos.write(randomBytes);
			}
		} catch (FileNotFoundException fnf) {
			log.error("Fail to find file, shoud never come to this place.", fnf);
		}
		return filepath;
	}

	void generateFiles(int fileCount) throws IOException {
		for (int i = 0; i < fileCount; i++) {
			generateFile();
		}
	}

	void generateFiles(int fileCount, int sizeInMB) throws IOException {
		for (int i = 0; i < fileCount; i++) {
			generateFile(sizeInMB);
		}
	}
}
