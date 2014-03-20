package lowmodel.attribute.type

import java.util.List;

import lowmodel.database.Databases;
import interfaces.SubAttributeTypes;

public enum DatetimeTypes implements SubAttributeTypes {
	DATE ("DATE", Databases.ORACLE, false),
	TIMESTAMP ("TIMESTAMP", Databases.ORACLE, false),
	TIMESTAMP_CLAUSE ("TIMESTAMP()", Databases.ORACLE, true);

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
	
	private DatetimeTypes(String name, Databases database, boolean modifyable) {
		this.name = name;
		this.database = database;
		this.modifyable = modifyable;
	}
	
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<>();
		
		for (DatetimeTypes type : DatetimeTypes.values()) {
			names.add(type.name);
		}
		
		return names;
	}
	
	public static DatetimeTypes getByName(String name) {
		for (DatetimeTypes type : DatetimeTypes.values()) {
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
