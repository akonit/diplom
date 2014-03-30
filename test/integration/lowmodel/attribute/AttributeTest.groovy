package lowmodel.attribute

import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.StringTypes
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
		battr.attrType = AttributeType.STRING;
		battr.subAttributeType = StringTypes.CLOB;
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
	 * Изменяемые типы изменяются, неизменяемые -нет.
	 */
	@Test
	public void testSubAttrTypeModification() {
		//назначение нового имени изменяемому типу
		def BasicAttribute battr = new BasicAttribute();
		battr.attrType = AttributeType.STRING;
		AttributeUtils.signSubTypeToAttr(battr, StringTypes.NVARCHAR.name);
		
		assertEquals(battr.activeSubAttributeType, StringTypes.NVARCHAR.name);
		
		String myNewType = "NVARCHAR(128)";
		boolean isSuccess = battr.changeSubAttrType(myNewType);
		
		assertEquals(battr.activeSubAttributeType, myNewType);
		assertTrue(isSuccess);
		
		//назначение нового имени неизменяемому типу
		AttributeUtils.signSubTypeToAttr(battr, StringTypes.CHAR.name);
		assertEquals(battr.activeSubAttributeType, StringTypes.CHAR.name);
		
		myNewType = "CHAR!11";
		isSuccess = battr.changeSubAttrType(myNewType);
		
		assertEquals(battr.activeSubAttributeType, StringTypes.CHAR.name);
		assertFalse(isSuccess);		
	}
}
