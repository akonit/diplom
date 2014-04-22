package connection;

import groovy.sql.Sql
import org.apache.log4j.Logger

public class ConnectionManager {
	
	static Logger log = Logger.getLogger(ConnectionManager.class.getName());

	public Sql openAndGetConnection(String url, String user, String password, Driver driver) {
		try {
			def sql = Sql.newInstance(url, user, password, driver.getName())
			log.info("openAndGetConnection: " + url + ", " + user + ", " + password + ",  "
				+ driver.name + " - ok");
			return sql
		} catch (Exception e) {
			log.error("openAndGetConnection: " + url + ", " + user + ", " + password + ",  "
				+ driver.name + " - fail, " + e);
			throw new RuntimeException("open connection - fail");
		}
	}
	
	public void closeConnection(Sql connection) {
		try {
			connection.close();
			log.info("closeConnection - ok");
		} catch (Exception e) {
		    log.error("closeConnection - fault, " + e);
		}
	}
}
