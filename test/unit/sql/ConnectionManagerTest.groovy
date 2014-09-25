package sql;

import groovy.sql.Sql;

import org.junit.Ignore;
import org.junit.Test;

import sql.ConnectionManager;
import sql.Database;
import static org.junit.Assert.*;

public class ConnectionManagerTest {

	ConnectionManager cm = new ConnectionManager();
	
	/**
	 * Валидные данные - от компа к компу они разные, поэтому аннотация Ignore.
	 * Можно его убрать и подставить свои параметры подключения к бд для
	 * запуска этого теста. 
	 */
	@Ignore
	@Test
	public void testOkOpenConnection() {
		boolean isException = false;
		String url = "jdbc:mysql://localhost/PRODUCTS";
		String user = "root";
		String password = "password";
		try {
			Sql sql = cm.openAndGetConnection(url, user, password, Database.MY_SQL);
			cm.closeConnection(sql);
		} catch (Exception e) {
			isException = true;
		}
		
		assertFalse(isException);
	}
}
