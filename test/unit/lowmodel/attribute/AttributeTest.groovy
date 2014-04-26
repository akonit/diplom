package lowmodel.attribute

import lowmodel.attribute.type.AttributeTypes
import utils.*

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
		battr.attributeType = AttributeTypes.CLOB;
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
		assertEquals(battr.definition, ((BasicAttribute) newBattr).definition);
		assertEquals(battr.attributeType, ((BasicAttribute) newBattr).attributeType);
		assertEquals(battr.keyGroup, ((BasicAttribute) newBattr).keyGroup);
	}
	
	/**
	 * Изменяемые типы изменяются, неизменяемые -нет.
	 */
	@Test
	public void testSubAttrTypeModification() {
		//назначение нового имени изменяемому типу
		def BasicAttribute battr = new BasicAttribute();
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.NVARCHAR.name);
		
		assertEquals(battr.activeAttributeType, AttributeTypes.NVARCHAR.name);
		
		String myNewType = "NVARCHAR(128)";
		boolean isSuccess = battr.changeSubAttrType(myNewType);
		
		assertEquals(battr.activeAttributeType, myNewType);
		assertTrue(isSuccess);
		
		//назначение нового имени неизменяемому типу
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.CHAR.name);
		assertEquals(battr.activeAttributeType, AttributeTypes.CHAR.name);
		
		myNewType = "CHAR!11";
		isSuccess = battr.changeSubAttrType(myNewType);
		
		assertEquals(battr.activeAttributeType, AttributeTypes.CHAR.name);
		assertFalse(isSuccess);		
	}
	
	/**
	 * Проверка 'Datetime' атрибутов.
	 */
	@Test
	public void testDatetimeAttributeType() {
		def BasicAttribute battr = new BasicAttribute();
		List<String> types = AttributeTypes.getAllNames();
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.DATE.name);//имитация выбора типа на форме.
		
		assertTrue(types.contains(battr.attributeType.name));

		//проверка получения полной информации по атрибуту
		assertEquals(battr.attributeType.name, AttributeTypes.DATE.name);
		assertEquals(battr.activeAttributeType, AttributeTypes.DATE.name);
		assertEquals(battr.attributeType.database, AttributeTypes.DATE.database);
		assertEquals(battr.attributeType.modifyable, AttributeTypes.DATE.modifyable);
	}
	
	
	/**
	 * Проверка 'String' атрибутов.
	 */
	@Test
	public void testStringAttributeType() {
		def BasicAttribute battr = new BasicAttribute();
		List<String> types = AttributeTypes.getAllNames();
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.CLOB.name);//имитация выбора типа на форме.

		assertTrue(types.contains(battr.attributeType.name));
		
		//проверка получения полной информации по атрибуту
		assertEquals(battr.attributeType.name, AttributeTypes.CLOB.name);
		assertEquals(battr.activeAttributeType, AttributeTypes.CLOB.name);
		assertEquals(battr.attributeType.database, AttributeTypes.CLOB.database);
		assertEquals(battr.attributeType.modifyable, AttributeTypes.CLOB.modifyable);
	}
}
