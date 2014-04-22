package connection;

public enum Driver {

	MY_SQL ("com.mysql.jdbc.Driver"),
	ORACLE ("oracle.jdbc.pool.OracleDataSource");
	
	/**
	 * Название драйвера.
	 */
	private String name;
	
	private Driver(String name) {
		this.name = name
	}
	
	public String getName() {
		return name;
	}
}
