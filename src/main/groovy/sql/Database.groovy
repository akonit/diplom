package sql;import java.util.List;

import attribute.AttributeTypes;



public enum Database {

	MY_SQL ("MySql", "com.mysql.jdbc.Driver"),
	ORACLE ("Oracle", "oracle.jdbc.pool.OracleDataSource");
	
	/**
	 * Название базы данных для отображения пользователю.
	 */
	private String name;
	
	/**
	 * Название драйвера.
	 */
	private String driver;
	
	private Database(String name, String driver) {
		this.name = name;
		this.driver = driver
	}
	
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<>();
		
		for (Database db : Database.values()) {
			names.add(db.name);
		}
		
		return names;
	}
	
	public static Database getByName(String name) {
		for (Database db : Database.values()) {
			if(db.name.equals(name)) {
				return db;
			}
		}
		
		return null;
	}
	
	public String getDriver() {
		return driver;
	}
	
	public String getName() {
		return name;
	}
}
