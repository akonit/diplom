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
	
	private static Sql connection

	/**
	 * Создаст новый .db файл.
	 * @param name имя файла.
	 */
	public static void createNewFile(String name) {
		try {
			InputStream is = new FileInputStream("configuration/base.db")//ClassLoader.getSystemClassLoader().getSystemResourceAsStream("base.db")
		    Files.copy(is, Paths.get("saves/" + name + ".db"))
			is.close()
		} catch (Exception e) {
		    log.error("createNewFile [" + name + "] -> error", e)
		}

		openFile(name)
	}
	
	/**
	 * Откроет существующий .db файл.
	 */
	public static void openFile(String name) {
		String url = "jdbc:sqlite:saves/" + name + ".db"
		connection = ConnectionManager.openAndGetConnection(url, Database.SQLITE)
		connection.execute("PRAGMA foreign_keys = ON")
		connection.getConnection().setAutoCommit(false)
			
		log.info("openFile [" + name + "] -> done")
	}
	
	/**
	 * Вызывать перед завершением работы приложения.
	 */
	public static void exitApplication() {
		ConnectionManager.closeConnection(connection)
	}
	
	public static void save() {
		connection.commit()
	}
	
	public static Sql getConnection() {
		return connection
	}
	
	/**
	 * Отменить последнее действие.
	 */
	public static void undo() {
		
	}
	
	/**
	 * Повторить последнее отмененное действие.
	 */
	public static void redo() {
		
	}
}
