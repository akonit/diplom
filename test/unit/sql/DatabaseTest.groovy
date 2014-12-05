package sql

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertNotNull
import static org.junit.Assert.assertTrue

import java.util.List

import org.junit.Test

public class DatabaseTest {

	@Test
	public void testDatabaseEnum() {
		List<String> dbs = Database.getAllNames()
		assertNotNull(dbs)
		assertTrue(dbs.contains(Database.MY_SQL.name))
		
		Database mySql = Database.getByName(Database.MY_SQL.name)
		assertEquals(mySql, Database.MY_SQL)
	}
}
