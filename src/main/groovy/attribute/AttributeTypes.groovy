package attribute

import java.util.List
import sql.Database
import static sql.Database.*

/**
 * Конкретизированные типы атрибутов. Используются при генерации схемы БД (в отличие от групп
 * атрибутов).
 */
public enum AttributeTypes {
	
	//string attributes
	CHAR ("CHAR", Arrays.asList(ORACLE, MY_SQL), false),
	CHAR_CLAUSE ("CHAR()", Arrays.asList(ORACLE, MY_SQL), true),
	NCHAR("NCHAR", Arrays.asList(ORACLE, MY_SQL), false),
	NCHAR_CLAUSE("NCHAR()", Arrays.asList(ORACLE, MY_SQL), true),
	VARCHAR("VARCHAR()", Arrays.asList(ORACLE, MY_SQL), true),
	NVARCHAR("NVARCHAR()", Arrays.asList(ORACLE, MY_SQL), true),
	
	//bit string attributes
	BIT("BIT()", Arrays.asList(ORACLE, MY_SQL), true),
	BIT_VARYING("BIT VARYING()", Arrays.asList(ORACLE, MY_SQL), true),
	
	//database attributes
	DATE ("DATE", Arrays.asList(ORACLE, MY_SQL), false),
	TIME ("TIME", Arrays.asList(ORACLE, MY_SQL), false),
	TIMESTAMP ("TIMESTAMP", Arrays.asList(ORACLE, MY_SQL), false),
	TIMESTAMP_CLAUSE ("TIMESTAMP()", Arrays.asList(ORACLE, MY_SQL), true),
	INTERVAL ("INTERVAL", Arrays.asList(ORACLE, MY_SQL), false),
	
	// binary attributes
	CLOB("CLOB", Arrays.asList(ORACLE, MY_SQL), false),
	
	// numbers
	INTEGER("INTEGER", Arrays.asList(ORACLE, MY_SQL), false),
	SMALLINT("SMALLINT", Arrays.asList(ORACLE, MY_SQL), false),
	DECIMAL("DECIMAL()", Arrays.asList(ORACLE, MY_SQL), true),
	NUMERIC("NUMERIC()", Arrays.asList(ORACLE, MY_SQL), true),
	FLOAT("FLOAT()", Arrays.asList(ORACLE, MY_SQL), true),
	REAL("REAL()", Arrays.asList(ORACLE, MY_SQL), true),
	DOUBLE_PRECISION("DOUBLE_PRECISION", Arrays.asList(ORACLE, MY_SQL), true);
	
	/**
	 * Название атрибута для отображения пользователю.
	 */
	private String name
	
	/**
	 * Список БД, в которых есть такой тип атрибута.
	 */
	//проверить на адекватность!
	private List<Database> databases
	
	/**
	 * Можно указать размер атрибута (длину слова например).
	 */
	private boolean modifyable
	
	
	private AttributeTypes(String name, List<Database> databases, boolean modifyable) {
		this.name = name
		this.databases = databases
		this.modifyable = modifyable
	}
	
	public static List<String> getAllNames() {
		List<String> names = new ArrayList<>()
		
		for (AttributeTypes type : AttributeTypes.values()) {
			names.add(type.name)
		}
		
		return names
	}
	
	public static AttributeTypes getByName(String name) {
		for (AttributeTypes type : AttributeTypes.values()) {
			if(type.name.equals(name)) {
				return type
			}
		}
		
		return null
	}
}
