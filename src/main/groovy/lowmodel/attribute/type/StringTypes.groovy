package lowmodel.attribute.type

import interfaces.SubAttributeTypes
import lowmodel.database.Databases

//подумать про указание размерности. varchar(20) например. мб какая валидация нужна
//дописать остальные атрибуты
//добавить возможность редактирования имен атрибутов. новое поле workingName?
public enum StringTypes implements SubAttributeTypes {

	CHAR ("CHAR", Databases.ORACLE, false),
	CHAR_CLAUSE ("CHAR()", Databases.ORACLE, true),
	NCHAR("NCHAR", Databases.ORACLE, false),
	NCHAR_CLAUSE("NCHAR()", Databases.ORACLE, true),
	VARCHAR("VARCHAR()", Databases.ORACLE, true),
	NVARCHAR("NVARCHAR()", Databases.ORACLE, true),
	/**
	 * бинарные данные.
	 */
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
	
	private StringTypes(String name, Databases database, boolean modifyable) {
		this.name = name;
		this.database = database;
		this.modifyable = modifyable;
	}
	
	//добавить фильтр по бд? вытащить в интерфейс попробовать
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<>();
		
		for (StringTypes type : StringTypes.values()) {
			names.add(type.name);
		}
		
		return names;
	}
	
	public static StringTypes getByName(String name) {
		for (StringTypes type : StringTypes.values()) {
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
