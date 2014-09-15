package utils

import org.apache.log4j.Logger

import attribute.Attribute
import entity.Index

//нужен ли идентификатор таблицы индексу?
//кажется, удаление индекса из оперативной памяти будет редкостной кривостью. имхо,
//лучше ограничиться sqlite и проапдейтить потом состояние workspace, когда такая фича появится
class IndexUtils {
	
	static Logger log = Logger.getLogger(IndexUtils.class.getName())
	
	/**
	 * Запись в файл.
	 * @param entityId - идентификатор таблицы, которой принадлежит атрибут.
	 */
	public static void createIndex(Index index, long entityId) {
		try {
			index.id = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_index(id, name, commentary, table_id)"
				+ " values(?, ?, ?, ?)", [index.id, index.name, index.commentary, entityId])
			
			if (index.attributes != null && !index.attributes.isEmpty()) {
				for (Attribute attr : index.attributes) {
					UserDataUtils.connection.execute("insert into app_index_attribute"
						+ " (index_id, attribute_id) values(?, ?)", [index.id, attr.id])
				}
			}
			log.info("createIndex [" + index.name + "] -> done, " + index.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createIndex [" + index.name + "] -> failed", e)
			throw new RuntimeException("failed to create index")
		}
	}
	
	/**
	 * Удаление из файла. Также удалит связи между таблицами и порожденные ими атрибуты.
	 */
	public static void deleteIndex(long indexId) {
		try {
			UserDataUtils.connection.eachRow('select * from app_relation where index_id = ?',
					[indexId]) { row ->
						RelationshipUtils.deleteRelationship(row.id)
					}
	        UserDataUtils.connection.execute("delete from app_index where id = ?", [indexId])
		    log.info("deleteIndex [" + indexId + "] -> done")
	    } catch (Exception e) {
		    UserDataUtils.connection.rollback()
		    log.error("deleteIndex [" + indexId + "] -> failed", e)
		    throw new RuntimeException("failed to delete index")
	    }
	}
}
