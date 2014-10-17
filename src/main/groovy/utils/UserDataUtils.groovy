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
			new File("saves").mkdir();
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
	
	/**
	 * Оставит в бд только актуальные версии объектов.
	 */
	public static void cleanUp() {
		try {
			//clean up undone
			cleanUpUndone()
			
			//clean up deleted
			connection.execute("delete from app_table where is_deleted = ?", [1])
		    connection.execute("delete from app_attribute where is_deleted = ?", [1])
		    connection.execute("delete from app_relation where is_deleted = ?", [1])
		    connection.execute("delete from app_index where is_deleted = ?", [1])
		    connection.execute("delete from relation_to_attr where is_deleted = ?", [1])
		    connection.execute("delete from app_index_attribute where is_deleted = ?", [1])
			
			//clean up old versions
			connection.eachRow("select distinct(id) from app_table") {
				def row = connection.firstRow("select * from app_table where id = ? order by time desc",
						[it.id])
				connection.execute("delete from app_table where id = ? and time != ?", [it.id, row.time])
			}
			connection.eachRow("select distinct(id) from app_attribute") {
				def row = connection.firstRow("select * from app_attribute where id = ? order by time desc",
						[it.id])
				connection.execute("delete from app_attribute where id = ? and time != ?", [it.id, row.time])
			}
			connection.eachRow("select distinct(id) from app_relation") {
				def row = connection.firstRow("select * from app_relation where id = ? order by time desc",
						[it.id])
				connection.execute("delete from app_relation where id = ? and time != ?", [it.id, row.time])
			}
			connection.eachRow("select distinct(id) from app_index") {
				def row = connection.firstRow("select * from app_index where id = ? order by time desc",
						[it.id])
				connection.execute("delete from app_index where id = ? and time != ?", [it.id, row.time])
			}
			connection.eachRow("select distinct(id) from relation_to_attr") {
				def row = connection.firstRow("select * from relation_to_attr where id = ? order by time desc",
						[it.id])
				connection.execute("delete from relation_to_attr where id = ? and time != ?", [it.id, row.time])
			}
			connection.eachRow("select distinct(id) from app_index_attribute") {
				def row = connection.firstRow("select * from app_index_attribute where id = ? order by time desc",
						[it.id])
				connection.execute("delete from app_index_attribute where id = ? and time != ?", [it.id, row.time])
			}
			
			log.info("cleanUp -> done")
		} catch (Exception e) {
		    log.error("cleanUp -> error", e)
		    throw new RuntimeException(e)
		}
	}
	
	public static void save() {
		connection.commit()
	}
	
	public static Sql getConnection() {
		return connection
	}
	
	/**
	 * Отменить последнее действие.
	 * Получаем время последней транзакции в статусе 'DONE' и отмечаем отмененными все записи в бд,
	 * датируемые этим временем.
	 */
	public static void undo() {
		try {
			def row = connection.firstRow("select time from app_table "
					+ " union select time from app_attribute "
					+ " union select time from app_relation "
					+ " union select time from app_index "
					+ " union select time from relation_to_attr "
					+ " union select time from app_index_attribute "
					+ " where status = ? order by time desc", [Status.DONE.name])
			if (row == null) {
				log.error("undo -> nothing to be undone here")
				return
			}

			long time = row.time;
			updateStatus(time, Status.UNDONE)
			log.info("undo -> done")
		} catch (Exception e) {
			log.error("undo -> error", e)
			throw new RuntimeException(e)
		}
	}
	
	/**
	 * Повторить последнее отмененное действие.
	 * Получаем время последней транзакции в статусе 'DONE' и отмечаем отмененными все записи в бд,
	 * датируемые этим временем.
	 */
	public static void redo() {
		try {
			def row = connection.firstRow("select time from app_table "
					+ " union select time from app_attribute "
					+ " union select time from app_relation "
					+ " union select time from app_index "
					+ " union select time from relation_to_attr "
					+ " union select time from app_index_attribute "
					+ " where status = ? order by time desc", [Status.UNDONE.name])
			if (row == null) {
				log.error("redo -> nothing to be undone here")
				return
			}

			long time = row.time;
			updateStatus(time, Status.DONE)
			log.info("redo -> done")
		} catch (Exception e) {
			log.error("redo -> error", e)
			throw new RuntimeException(e)
		}		
	}
	
	/**
	 * Обновит статус во всех записях базы данных с указанным временем транзакции.
	 */
	private static void updateStatus(long time, Status status) {
		connection.execute("update app_table set status = ? where time = ?", [status.name, time])
		connection.execute("update app_attribute set status = ? where time = ?", [status.name, time])
		connection.execute("update app_relation set status = ? where time = ?", [status.name, time])
		connection.execute("update app_index set status = ? where time = ?", [status.name, time])
		connection.execute("update relation_to_attr set status = ? where time = ?", [status.name, time])
		connection.execute("update app_index_attribute set status = ? where time = ?", [status.name, time])
	}
	
	/**
	 * Удалит все записи в статусе 'UNDONE'.
	 */
	public static void cleanUpUndone() {
		connection.execute("delete from app_table where status = ?", [Status.UNDONE.name])
		connection.execute("delete from app_attribute where status = ?", [Status.UNDONE.name])
		connection.execute("delete from app_relation where status = ?", [Status.UNDONE.name])
		connection.execute("delete from app_index where status = ?", [Status.UNDONE.name])
		connection.execute("delete from relation_to_attr where status = ?", [Status.UNDONE.name])
		connection.execute("delete from app_index_attribute where status = ?", [Status.UNDONE.name])
	}
}
