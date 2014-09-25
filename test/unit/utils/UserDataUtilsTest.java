package utils;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class UserDataUtilsTest {

	@Test
	public void testCreateNewFile() throws Exception {
		String name = "myNewDb" + System.currentTimeMillis();
		UserDataUtils.createNewFile(name);
		File newFile = new File("saves/" + name + ".db");
		if(!newFile.exists()) {
			fail();
		}
		UserDataUtils.exitApplication();
	}
}
