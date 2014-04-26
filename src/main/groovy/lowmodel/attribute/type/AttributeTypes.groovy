package lowmodel.attribute.type;

import java.util.List;

import lowmodel.database.Databases;

/**
 * Конкретизированные типы атрибутов. Используются при генерации схемы БД (в отличие от групп
 * атрибутов).
 */
public enum AttributeTypes {
	
	//string attributes
	CHAR ("CHAR", Databases.ORACLE, false),
	CHAR_CLAUSE ("CHAR()", Databases.ORACLE, true),
	NCHAR("NCHAR", Databases.ORACLE, false),
	NCHAR_CLAUSE("NCHAR()", Databases.ORACLE, true),
	VARCHAR("VARCHAR()", Databases.ORACLE, true),
	NVARCHAR("NVARCHAR()", Databases.ORACLE, true),
	
	//database attributes
	DATE ("DATE", Databases.ORACLE, false),
	TIMESTAMP ("TIMESTAMP", Databases.ORACLE, false),
	TIMESTAMP_CLAUSE ("TIMESTAMP()", Databases.ORACLE, true),
	
	// binary attributes
	CLOB("CLOB", Databases.ORACLE, false);
	
	/**
	 * Название группы атрибута для представления.
	 */
	private String name;
	
	/**
	 * БД, в которой есть такой тип атрибута. мб сделать список, мб захардкодить повторения
	 */
	private Databases database;
	
	/**
	 * Можно указать размер атрибута (длину слова например).
	 */
	private boolean modifyable;
	
	
	private AttributeTypes(String name, Databases database, boolean modifyable) {
		this.name = name;
		this.database = database;
		this.modifyable = modifyable;
	}
	
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<>();
		
		for (AttributeTypes type : AttributeTypes.values()) {
			names.add(type.name);
		}
		
		return names;
	}
	
	public static AttributeTypes getByName(String name) {
		for (AttributeTypes type : AttributeTypes.values()) {
			if(type.name.equals(name)) {
				return type;
			}
		}
		
		return null;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Databases getDatabase() {
		return database;
	}

	@Override
	public boolean isModifyable() {
		return modifyable;
	}
}
