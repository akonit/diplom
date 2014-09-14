package utils

import org.apache.log4j.Logger;

import attribute.Attribute;
import entity.Index

//нужен ли идентификатор таблицы индексу?
class IndexUtils {
	
	static Logger log = Logger.getLogger(IndexUtils.class.getName())
	
	/**
	 * Запись в файл.
	 * @param entityId - идентификатор таблицы, которой принадлежит атрибут.
	 */
	public static void createIndex(Index index, long entityId) {
		try {
			index.id = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_index(id, name, commentary, table_id) values(?, ?, ?, ?)",
				[index.id, index.name, index.commentary, entityId])
			
			if (index.attributes != null && !index.attributes.isEmpty()) {
				for (Attribute attr : index.attributes) {
					UserDataUtils.connection.execute("insert into app_index_attribute(index_id, attribute_id) values(?, ?)",
						[index.id, attr.id])
				}
			}
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createIndex [" + index.name + "] -> failed", e)
		}
	}
	
	//delete index
}
