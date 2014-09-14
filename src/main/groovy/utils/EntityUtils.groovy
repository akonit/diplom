package utils;

import entity.Entity
import org.apache.log4j.Logger

public class EntityUtils {
	
	static Logger log = Logger.getLogger(EntityUtils.class.getName())

	/**
	 * Запись в файл.
	 */
	public static void createEntity(Entity entity) {
		try {
			entity.id = System.currentTimeMillis()
		    UserDataUtils.connection.execute("insert into app_table(id, name, commentary) values(?, ?, ?)",
				[entity.id, entity.name, entity.commentary])
		} catch (Exception e) {
		    UserDataUtils.connection.rollback()
			log.error("createEntity [" + entity.name + "] -> failed", e)
		}
	}
	
	//delete entity
}
