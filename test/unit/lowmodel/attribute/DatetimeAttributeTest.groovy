package lowmodel.attribute;

import org.junit.Test;

import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.DatetimeTypes
import utils.*

import static org.junit.Assert.*;

public class DatetimeAttributeTest {

	/**
	 * Проверка группы типов атрибута 'Datetime' - атрибут содержит группу
	 * и конкретный элемент группы. Можно получить список элементов в группе,
	 * можно выгрузить полную информацию о конкретном элементе.
	 */
	@Test
	public void testStringAttributeType() {
		def BasicAttribute battr = new BasicAttribute();
		battr.attrType = AttributeType.DATE_TIME;
		List<String> subTypes = battr.attrType.subTypes;
		AttributeUtils.signSubTypeToAttr(battr, DatetimeTypes.DATE.name);//имитация выбора типа на форме.

		//проверка получения списка типов атрибута в группе.
		for (DatetimeTypes type : DatetimeTypes.values()) {
			assertTrue(subTypes.contains(type.name));
		}
		
		//проверка получения полной информации по атрибуту
		assertEquals(battr.subAttributeType.name, DatetimeTypes.DATE.name);
		assertEquals(battr.activeSubAttributeType, DatetimeTypes.DATE.name);
		assertEquals(battr.subAttributeType.database, DatetimeTypes.DATE.database);
		assertEquals(battr.subAttributeType.modifyable, DatetimeTypes.DATE.modifyable);
	}
}
