package utils

import org.apache.log4j.Logger

import attribute.AttributeTypes
import attribute.Attribute
import lowmodel.attribute.type.*

final class AttributeUtils {
	
	static Logger log = Logger.getLogger(AttributeUtils.class.getName())
	
	private AttributeUtils() {
		
	}
	
	/**
	 * Запись в файл.
	 * @param entityId - идентификатор таблицы, которой принадлежит атрибут.
	 */
	public static void createAttribute(Attribute attr, long entityId) {
		try {
			attr.id = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_attribute"
					+ " (id, name, type, definition, commentary, is_primary, is_nullable, is_unique, table_id)"
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						attr.id,
						attr.name,
						attr.activeAttributeType,
						attr.definition,
						attr.commentary,
						attr.constraints.primary ? 1 : 0,
						attr.constraints.nullable ? 1 : 0,
						attr.constraints.unique ? 1 : 0,
						entityId
					])
			log.info("createAttribute ["+ attr.name + "] -> done, " + attr.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createAttribute [" + attr.name + "] -> failed", e)
			throw new RuntimeException("failed to create attribute")
		}
	}
	
	/**
	 * Удалит атрибут
	 */
	public static void deleteAttribute(long attrId) {
		try {
			UserDataUtils.connection.execute("delete from app_attribute where id = ?", [attrId])
			log.info("deleteAttribute [" + attrId + "] -> done")
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("deleteAttribute [" + attrId + "] -> failed", e)
			throw new RuntimeException("failed to delete attribute")
		}
	}

	/**
	 * Назначение атрибуту типа с указанным названием. Метод предположительно для view
	 * @param attr атрибут
	 * @param subTypeName имя назначаемого типа
	 */
	public static void signTypeToAttr(Attribute attr, String typeName) {
		attr.activeAttributeType = typeName
		attr.attributeType =  AttributeTypes.getByName(typeName)
	}
}
