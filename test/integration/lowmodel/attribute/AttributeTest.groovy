package lowmodel.attribute

import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.StringTypes

import org.junit.After;
import org.junit.Test

import static org.junit.Assert.*;

//выделить как минимум assert в базовый класс
class AttributeTest {

	/**
	 * Объект, записанный в файл, совпадает с объектом, прочитанным из файла.
	 */
	@Test
	public void testSerialization() {
		def BasicAttribute battr = new BasicAttribute();
		battr.attrType = AttributeType.STRING;
		battr.subAttributeType = StringTypes.CLOB.name;
		battr.definition = "test attr";
		battr.keyGroup = KeyGroup.PRIMARY_KEY;
		battr.id = String.valueOf(System.currentTimeMillis());
		
		//в файл
		def out= new ObjectOutputStream(new FileOutputStream('save'))
		out.writeObject(battr)
		out.close()
		
		//из файла
		def inp= new ObjectInputStream(new FileInputStream('save'))
		def newBattr= inp.readObject()
		inp.close()
		
		assertEquals(battr.id, ((BasicAttribute) newBattr).id);
		assertEquals(battr.attrType, ((BasicAttribute) newBattr).attrType);
		assertEquals(battr.definition, ((BasicAttribute) newBattr).definition);
		assertEquals(battr.subAttributeType, ((BasicAttribute) newBattr).subAttributeType);
		assertEquals(battr.keyGroup, ((BasicAttribute) newBattr).keyGroup);
	}
	
	/**
	 * Проверка группы типов атрибута 'String' - атрибут содержит группу
	 * и конкретный элемент группы. Можно получить список элементво в группе,
	 * можно выгрузить полуню информацию о конкретном элементе.
	 */
	@Test
	public void testStringAttributeType() {
		def BasicAttribute battr = new BasicAttribute();
		battr.attrType = AttributeType.STRING;
		List<String> subTypes = battr.attrType.subTypes;

		//проверка получения списка типов атрибута в группе.
		for (StringTypes type : StringTypes.values()) {
			assertTrue(subTypes.contains(type.name));
		}
		
		//проверка получения полной информации по атрибуту
		battr.subAttributeType = StringTypes.CLOB.name;
		StringTypes st = battr.getFullSubAttributeData();
		
		assertEquals(st.name, StringTypes.CLOB.name);
		assertEquals(st.database, StringTypes.CLOB.database);
		assertEquals(st.modifyable, StringTypes.CLOB.modifyable);
	}
}
