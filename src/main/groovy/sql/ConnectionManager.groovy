package sql

import javax.sql.DataSource

import groovy.sql.Sql

import org.apache.log4j.Logger

public class ConnectionManager {
	
	static Logger log = Logger.getLogger(ConnectionManager.class.getName())

	public static Sql openAndGetConnection(String url, String user, String password, Database database) {
		try {
			def sql = new Sql(Sql.newInstance(url, user, password, database.getDriver()).getConnection())
			log.info("openAndGetConnection: " + url + ", " + user + ", " + password + ",  "
				+ database.driver + " - ok")
			return sql
		} catch (Exception e) {
			log.error("openAndGetConnection: " + url + ", " + user + ", " + password + ",  "
				+ database.driver + " - fail, " + e)
			throw new RuntimeException("open connection - fail")
		}
	}

	public static Sql openAndGetConnection(String url, Database database) {
		try {
			def sql = new Sql(Sql.newInstance(url, database.driver).getConnection())
			log.info("openAndGetConnection: " + url + ",  " + database.driver + " - ok")
			return sql
		} catch (Exception e) {
			log.error("openAndGetConnection: " + url + database.driver + " - fail, " + e)
			throw new RuntimeException("open connection - fail")
		}
	}
	
	public static void closeConnection(Sql connection) {
		try {
			connection.close()
			log.info("closeConnection - ok")
		} catch (Exception e) {
		    log.error("closeConnection - fault, " + e)
		}
	}
}
