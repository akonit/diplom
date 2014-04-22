package lowmodel.attribute;

import org.junit.Test;

import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.StringTypes
import utils.*

import static org.junit.Assert.*;

public class StringAttributeTest {

	/**
	 * Проверка группы типов атрибута 'String' - атрибут содержит группу
	 * и конкретный элемент группы. Можно получить список элементов в группе,
	 * можно выгрузить полную информацию о конкретном элементе.
	 */
	@Test
	public void testStringAttributeType() {
		def BasicAttribute battr = new BasicAttribute();
		battr.attrType = AttributeType.STRING;
		List<String> subTypes = battr.attrType.subTypes;
		AttributeUtils.signSubTypeToAttr(battr, StringTypes.CLOB.name);//имитация выбора типа на форме.

		//проверка получения списка типов атрибута в группе.
		for (StringTypes type : StringTypes.values()) {
			assertTrue(subTypes.contains(type.name));
		}
		
		//проверка получения полной информации по атрибуту
		assertEquals(battr.subAttributeType.name, StringTypes.CLOB.name);
		assertEquals(battr.activeSubAttributeType, StringTypes.CLOB.name);
		assertEquals(battr.subAttributeType.database, StringTypes.CLOB.database);
		assertEquals(battr.subAttributeType.modifyable, StringTypes.CLOB.modifyable);
	}
}
