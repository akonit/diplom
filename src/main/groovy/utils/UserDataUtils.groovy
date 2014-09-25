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
	 * Стек операций, обратных к проведенным.
	 */
	private static Stack<OperationBlock> undoStack
	
	/**
	 * Стек операций, обратных к отмененным.
	 */
	private static Stack<OperationBlock> redoStack

	/**
	 * Создаст новый .db файл.
	 * @param name имя файла.
	 */
	public static void createNewFile(String name) {
		try {
			InputStream is = new FileInputStream("configuration/base.db")//ClassLoader.getSystemClassLoader().getSystemResourceAsStream("base.db")
		    Files.copy(is, Paths.get("saves/" + name + ".db"))
			is.close()
			undoStack = new Stack<>()
			redoStack = new Stack<>()
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
		undoStack = new Stack<>()
		redoStack = new Stack<>()
			
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
		if (undoStack.peek() != null) {
			OperationBlock ob = undoStack.pop();
			try {
				for (Operation op : ob.undo) {
			        connection.execute(op.command, op.params)
			        log.info("undo [" + op.command + "] -> ok")
				}
				redoStack.push(ob)
			} catch (Exception e) {
		        connection.rollback()
			    log.error("undo -> failed", e)
			    throw new RuntimeException("failed to undo operation")
			}
		} else {
		    log.info("undo -> nothing could be undone")
		}
	}
	
	/**
	 * Повторить последнее отмененное действие.
	 */
	public static void redo() {
		if (redoStack.peek() != null) {
			OperationBlock ob = redoStack.pop()
			try {
				for (Operation op : ob.redo) {
				    connection.execute(op.command, op.params)
			        log.info("redo [" + op.command + "] -> ok")
				}
				undoStack.push(ob)
			} catch (Exception e) {
		        connection.rollback()
			    log.error("redo -> failed", e)
			    throw new RuntimeException("failed to redo operation")
			}
		} else {
		    log.info("redo -> nothing could be repeated")
		}
	}
}
