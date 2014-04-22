package connection;

import groovy.sql.Sql;

import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectionManagerTest {

	ConnectionManager cm = new ConnectionManager();
		
	/**
	 * Ошибочные данные при подключении -> исключение.
	 */
	@Test
	public void testFailOpenConnection() {
		boolean isException = false;
		try {
			cm.openAndGetConnection("wrongUrl", "user", "123456", Driver.ORACLE);
		} catch (Exception e) {
			isException = true;
		}
		
		assertTrue(isException);
	}
	
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
			Sql sql = cm.openAndGetConnection(url, user, password, Driver.MY_SQL);
			cm.closeConnection(sql);
		} catch (Exception e) {
			isException = true;
		}
		
		assertFalse(isException);
	}
}
