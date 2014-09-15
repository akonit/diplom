package utils

import entity.Entity
import org.apache.log4j.Logger

public final class EntityUtils {
	
	static Logger log = Logger.getLogger(EntityUtils.class.getName())
	
	private EntityUtils() {
		
	}

	/**
	 * Запись в файл.
	 */
	public static void createEntity(Entity entity) {
		try {
			entity.id = System.currentTimeMillis()
		    UserDataUtils.connection.execute("insert into app_table(id, name, commentary)" 
				+ " values(?, ?, ?)", [entity.id, entity.name, entity.commentary])
			log.info("createEntity [" + entity.name + "] -> done, " + entity.id)
		} catch (Exception e) {
		    UserDataUtils.connection.rollback()
			log.error("createEntity [" + entity.name + "] -> failed", e)
			throw new RuntimeException("failed to create entity")
		}
	}
	
	/**
	 * Удаление из файла. Заодно снесет все атрибуты, индексы etc
	 */
	public static void deleteEntity(long entityId) {
		try {
			UserDataUtils.connection.execute("delete from app_table where id = ?", [entityId])
			log.info("deleteEntity [" + entityId + "] -> done")
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("deleteEntity [" + entityId + "] -> failed", e)
			throw new RuntimeException("failed to delete entity")
		}
	}
}
