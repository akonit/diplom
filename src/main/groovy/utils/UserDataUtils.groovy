package utils;

import groovy.sql.Sql
import org.apache.log4j.Logger;
import sql.*;
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Функции по работе с SqlLite-хранилищем данных.
 */
public class UserDataUtils {
	
	static Logger log = Logger.getLogger(UserDataUtils.class.getName())
	
	private Sql connection

	/**
	 * Создаст новый .db файл.
	 * @param name имя файла.
	 */
	public void createNewFile(String name) {
		try {
			InputStream is = this.getClass().getResourceAsStream("/base.db")
		    Files.copy(is, Paths.get("saves/" + name + ".db"))
			is.close()
		} catch (Exception e) {
		    log.error("createNewFile [" + name + "] -> error", e)
		}

		String url = "jdbc:sqlite:saves/" + name + ".db"
		connection = ConnectionManager.openAndGetConnection(url, Database.SQLITE)
		log.info("createNewFile [" + name + "] -> done")
	}
	
	/**
	 * Вызывать перед завершением работы приложения.
	 */
	public void exitApplication() {
		ConnectionManager.closeConnection(connection)
	}
}
