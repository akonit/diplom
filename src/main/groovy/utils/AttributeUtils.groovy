package utils;

import org.apache.log4j.Logger;

import attribute.AttributeTypes;
import attribute.Attribute;
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
			UserDataUtils.connection.execute("insert into app_attribute(id, name, type, definition, commentary, table_id) values(?, ?, ?, ?, ?, ?)",
				[attr.id, attr.name, attr.activeAttributeType, attr.definition, attr.commentary, entityId])
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createAttribute [" + attr.name + "] -> failed", e)
		}
	}
	
	//delete attribute

	/**
	 * Назначение атрибуту типа с указанным названием. Метод предположительно для view
	 * @param attr атрибут
	 * @param subTypeName имя назначаемого типа
	 */
	public static void signTypeToAttr(Attribute attr, String typeName) {
		attr.activeAttributeType = typeName;
		attr.attributeType =  AttributeTypes.getByName(typeName);
	}
}
