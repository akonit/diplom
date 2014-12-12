package utils

import java.util.List;

import entity.Entity

import org.apache.log4j.Logger

public final class EntityUtils {
	
	static Logger log = Logger.getLogger(EntityUtils.class.getName())
	
	private EntityUtils() {
		
	}

	/**
	 * Создание таблицы.
	 */
	public static void createEntity(Entity entity) {
		try {
			entity.id = System.currentTimeMillis()
		    UserDataUtils.connection.execute("insert into app_table "
				+ "(id, time, status, name, commentary, is_deleted, "
				+ "x_coord, y_coord, height, width) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", [
						entity.id,
						System.currentTimeMillis(),
						Status.DONE.getName(),
						entity.name,
						entity.commentary,
						0,
						entity.xCoord,
						entity.yCoord,
						entity.height,
						entity.width
					])
			
			UserDataUtils.cleanUpUndone()			
			log.info("createEntity [" + entity.name + "] -> done, " + entity.id)
		} catch (Exception e) {
		    UserDataUtils.connection.rollback()
			log.error("createEntity [" + entity.name + "] -> failed", e)
			throw new RuntimeException("failed to create entity")
		}
	}
	
	/**
	 * Обновление таблицы
	 */
	public static void updateEntity(Entity entity) {
		try {
			UserDataUtils.connection.execute("insert into app_table "
				+ "(id, time, status, name, commentary, is_deleted, "
				+ "x_coord, y_coord, height, width) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", [
						entity.id,
						System.currentTimeMillis(),
						Status.DONE.getName(),
						entity.name,
						entity.commentary,
						0,
						entity.xCoord,
						entity.yCoord,
						entity.height,
						entity.width
					])
			
			entity = getCurrent(entity.id)
			
			UserDataUtils.cleanUpUndone()
			log.info("updateEntity [" + entity.name + "] -> done, " + entity.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("updateEntity [" + entity.name + "] -> failed", e)
			throw new RuntimeException("failed to update entity")
		}
	}
	
	/**
	 * Удаление таблицы. Проставит признак удаления всем зависимым сущностям.
	 */
	public static void deleteEntity(long entityId) {
		try {
			long time = System.currentTimeMillis()
			Entity entity = getCurrent(entityId)
			if (entity == null) {
				return
			}
		    UserDataUtils.connection.execute("insert into app_table "
				+ "(id, time, status, name, commentary, is_deleted, "
				+ "x_coord, y_coord, height, width) "
				+ "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", [
						entity.id,
						time,
						Status.DONE.getName(),
						entity.name,
						entity.commentary,
						1,
						entity.xCoord,
						entity.yCoord,
						entity.height,
						entity.width
					])
			UserDataUtils.connection.eachRow("select * from app_attribute where table_id = ? "
				+ " and is_deleted = 0", [entity.id]) { 
                AttributeUtils.deleteAttribute(it.id, time)
            }
			UserDataUtils.connection.eachRow("select * from app_relation where (table_from_id = ? "
				+ " or table_to_id = ?) and is_deleted = 0", [entity.id, entity.id]) { 
                RelationshipUtils.deleteRelationship(it.id, time)
            }
			UserDataUtils.connection.eachRow("select * from app_index where table_id = ? "
				+ " and is_deleted = 0", [entity.id]) { 
                IndexUtils.deleteIndex(it.id)
            }
			log.info("deleteEntity [" + entityId + "] -> done")
			
			UserDataUtils.cleanUpUndone()
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("deleteEntity [" + entityId + "] -> failed", e)
			throw new RuntimeException("failed to delete entity")
		}
	}
	
	public static Entity getCurrent(long id) {
		def row = UserDataUtils.connection.firstRow("select * from app_table where id = ? and status = ? order by time desc",
				[
					id,
					Status.DONE.name
				])
		if (row == null) {
			return row
		}
		
		Entity current = new Entity()
		current.id = row.id
		current.time = row.time
		current.name = row.name
		current.commentary = row.commentary
		current.isDeleted = row.is_deleted == 0 ? false : true
		current.status = Status.DONE
		current.xCoord = row.x_coord
		current.yCoord = row.y_coord
		current.height = row.height
		current.width = row.width
		
		return current
	}
	
	/**
	 * Получение списка всех актуальных неудаленных таблиц.
	 * @param connection
	 * @return
	 */
	public static List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_table") {
			Entity e = EntityUtils.getCurrent(it.id)
			if (!e.isDeleted) {
				entities.add(e)
			}
		}
		return entities
	}
}
