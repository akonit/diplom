package sql.converter;

import org.apache.log4j.Logger;

import entity.Entity
import relationship.Relationship
import sql.Database

public class ConverterCommander {

	static Logger log = Logger.getLogger(ConverterCommander.class.getName());
	
	private final Map<Database, SqlConverter> converters;
	
	public ConverterCommander () {
		converters = new HashMap();
		converters.put(Database.MY_SQL, new MySqlConverter());
	}
	
	/**
	 * Построение SQL-кода наката по диаграмме.
	 * @param database база данных.
	 * @param entities таблицы.
	 * @param relations связи между таблицами.
	 * @return SQL-код наката
	 */
	public String convertToSql(Database database, List<Entity> entities, 
		List<Relationship> relations) {
		SqlConverter converter = converters.get(database);
		if (converter == null) {
			log.error("no such converter for " + database);
			throw new Exception("no such converter for " + database);
		}
		
		return converter.convertToSql(entities, relations);
	}
}
