/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lychee.fs.hbase.rest;

import java.io.IOException;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author chunhui
 */
public class MainTest {
	
	@BeforeClass
	public static void run() throws IOException {
		Main.main(new String[]{});
	}

	@Ignore
	@Test
	public void testNothing() {
		
	}
}
