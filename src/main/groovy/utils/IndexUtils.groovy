package utils

import org.apache.log4j.Logger

import attribute.Attribute
import entity.Entity
import entity.Index

//нужен ли идентификатор таблицы индексу?
class IndexUtils {
	
	static Logger log = Logger.getLogger(IndexUtils.class.getName())
	
	/**
	 * Создание индекса.
	 * @param entityId - идентификатор таблицы, которой принадлежат атрибуты, образующие индекс.
	 */
	public static void createIndex(Index index, long entityId) {
		try {
			Entity e = EntityUtils.getCurrent(entityId)
			index.id = System.currentTimeMillis()
			long time = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_index "
				+ " (id, time, status, name, commentary, table_id, table_time, is_deleted) "
				+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
					index.id, 
					time, 
					Status.DONE.getName(),
					index.name, 
					index.commentary, 
					entityId,
					e.time,
					0])
			
			if (index.attributes != null && !index.attributes.isEmpty()) {
				for (Attribute attr : index.attributes) {
					attr = AttributeUtils.getCurrent(attr.id)
					UserDataUtils.connection.execute("insert into app_index_attribute"
						+ " (id, time, status, index_id, attribute_id, is_deleted, attribute_time, index_time) "
						+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
							System.currentTimeMillis(),
							time,
							Status.DONE.getName(),
							index.id, 
							attr.id,
							0,
							attr.time,
							time])
				}
			}
			
			UserDataUtils.cleanUpUndone()
			log.info("createIndex [" + index.name + "] -> done, " + index.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createIndex [" + index.name + "] -> failed", e)
			throw new RuntimeException("failed to create index")
		}
	}
	
	/**
	 * Обновление индекса таблицы.
	 */
	public static void updateIndex(Index index) {
		try {
			long time = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_index "
				+ " (id, time, status, name, commentary, table_id, table_time, is_deleted) "
				+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
					index.id, 
					time, 
					Status.DONE.getName(),
					index.name, 
					index.commentary, 
					index.entityId,
					index.entityTime,
					0])
			
			if (index.attributes != null && !index.attributes.isEmpty()) {
				for (Attribute attr : index.attributes) {
					UserDataUtils.connection.execute("insert into app_index_attribute"
						+ " (id, time, status, index_id, attribute_id, is_deleted, attribute_time, index_time) "
						+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
							System.currentTimeMillis(),
							time,
							Status.DONE.getName(),
							index.id, 
							attr.id,
							0,
							attr.time,
							time])
				}
			}
			
			UserDataUtils.cleanUpUndone()
			log.info("updateIndex [" + index.name + "] -> done, " + index.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("updateIndex [" + index.name + "] -> failed", e)
			throw new RuntimeException("failed to update index")
		}
	}
	
	/**
	 * Удаление индекса. Также удалит связи между таблицами и порожденные ими атрибуты.
	 */
	public static void deleteIndex(long indexId) {
		try {
			Index current = getCurrent(indexId)
			if (current == null || current.isDeleted) {
				return null
			}
			
			long time = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_index "
				+ " (id, time, status, name, commentary, table_id, table_time, is_deleted) "
				+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
					current.id,
					time,
					Status.DONE.getName(),
					current.name,
					current.commentary,
					current.entityId,
					current.entityTime,
					1])
			UserDataUtils.connection.eachRow("select * from app_relation where index_id = ? "
				+ " and is_deleted = 0",
					[indexId]) { row ->
						RelationshipUtils.deleteRelationship(row.id, time)
					}
	        UserDataUtils.connection.eachRow("select distinct aia.id as id  "
					+ " from app_index_attribute aia, app_index ai where "
					+ " aia.index_id = ai.id and ai.id = ?", [current.id]) {
						def row = UserDataUtils.connection.firstRow("select attribute_id, attribute_time, is_deleted from app_index_attribute "
								+ " where id = ? and status = ? order by time desc",
								[
									it.id,
									Status.DONE.getName()
								])
						if (row != null && row.is_deleted == 0) {
							UserDataUtils.connection.execute("insert into app_index_attribute "
									+ " (id, status, time, attribute_id, index_id, is_deleted, "
									+ " attribute_time, index_time) values "
									+ "(?, ?, ?, ?, ?, ?, ?, ?)", [
										it.id,
										Status.DONE,
										time,
										row.attribute_id,
										current.id,
										1,
										row.attribute_time,
										time
									])
						}
					}
			
			UserDataUtils.cleanUpUndone()
		    log.info("deleteIndex [" + indexId + "] -> done")
	    } catch (Exception e) {
		    UserDataUtils.connection.rollback()
		    log.error("deleteIndex [" + indexId + "] -> failed", e)
		    throw new RuntimeException("failed to delete index")
	    }
	}
	
	public static Index getCurrent(long id) {
		def row = UserDataUtils.connection.firstRow("select * from app_index where id = ? and status = ? order by time desc",
				[
					id,
					Status.DONE.getName()
				])
		if (row == null) {
			return row
		}
		
		Index current = new Index()
		current.id = row.id
		current.time = row.time
		current.entityId = row.table_id
		current.entityTime = row.table_time
		current.name = row.name
		current.commentary = row.commentary
		current.isDeleted = row.is_deleted == 0 ? false : true
		
		return current
	}
}
