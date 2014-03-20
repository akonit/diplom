package utils;

import interfaces.SubAttributeTypes;
import lowmodel.attribute.BasicAttribute
import lowmodel.attribute.type.*

final class AttrTypeSignification {

	/**
	 * Назначение атрибуту типа с указанным названием. Метод для view
	 * @param attr атрибут
	 * @param subTypeName имя назначаемого типа
	 */
	public static void signSubTypeToAttr(BasicAttribute attr, String subTypeName) {
		attr.activeSubAttributeType = subTypeName;
		switch (attr.attrType) {
			case AttributeType.STRING:
				attr.subAttributeType =  StringTypes.getByName(subTypeName);
				break;

			case AttributeType.DATE_TIME:
				attr.subAttributeType =  DatetimeTypes.getByName(subTypeName);
				break;

			default:
				break;
		}
	}
}
