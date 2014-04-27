package utils;

import attribute.AttributeTypes;
import attribute.Attribute;
import lowmodel.attribute.type.*

final class AttributeUtils {
	
	private AttributeUtils() {
		
	}

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
