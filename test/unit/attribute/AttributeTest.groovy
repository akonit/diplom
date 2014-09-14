package attribute

import utils.*

import org.junit.Test

import attribute.AttributeTypes;
import attribute.Attribute;
import entity.Entity
import static org.junit.Assert.*;

//выделить как минимум assert в базовый класс
class AttributeTest {

	@Test
	public void testSaveAttributeToFile() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity entity = new Entity();
		entity.setName("newEntity");
		entity.setCommentary("test");
		
		EntityUtils.createEntity(entity);
		
		def Attribute battr = new Attribute();
		battr.attributeType = AttributeTypes.CLOB;
		battr.activeAttributeType = AttributeTypes.CLOB;
		battr.definition = "test attr";
		battr.id = System.currentTimeMillis();
		battr.name = "test attr name"
		
		AttributeUtils.createAttribute(battr, entity.getId())
		def row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertEquals(battr.getName(), row.name)
		
		adUtils.exitApplication();
	}

	// создали таблицу - не смогли создать атрибут - ничего не сохранилось
	@Test
	public void testFailedToSaveAttributeToFile() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity entity = new Entity();
		entity.setName("newEntity");
		entity.setCommentary("test");
		
		EntityUtils.createEntity(entity);
		
		def Attribute battr = new Attribute();
		battr.attributeType = AttributeTypes.CLOB;
		battr.definition = "test attr";
		battr.id = System.currentTimeMillis();
		battr.name = null//constraint - name is not null
		
		boolean isException = false
		try {
		    AttributeUtils.createAttribute(battr, entity.getId())
		} catch (Exception e) {
		    isException = true
		}
		
		assertTrue(isException)
		def row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertNull(row)
		
		row = adUtils.getConnection().firstRow("select * from app_table")
		assertNull(row)
		
		adUtils.exitApplication();
	}

	// создали таблицу - закоммитили - не смогли создать атрибут - таблица сохранилась
	@Test
	public void testFailedToSaveAttributeWithEntityCommitToFile() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity entity = new Entity();
		entity.setName("newEntity");
		entity.setCommentary("test");
		
		EntityUtils.createEntity(entity);
		adUtils.save()
		
		def Attribute battr = new Attribute();
		battr.attributeType = AttributeTypes.CLOB;
		battr.definition = "test attr";
		battr.id = System.currentTimeMillis();
		battr.name = null//constraint - name is not null
		
		boolean isException = false
		try {
		    AttributeUtils.createAttribute(battr, entity.getId())
		} catch (Exception e) {
		    isException = true
		}
		
		assertTrue(isException)
		def row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertNull(row)
		
		row = adUtils.getConnection().firstRow("select * from app_table")
		assertEquals(entity.getName(), row.name)
		
		adUtils.exitApplication();
	}
	
	/**
	 * Изменяемые типы изменяются, неизменяемые -нет.
	 */
	@Test
	public void testSubAttrTypeModification() {
		//назначение нового имени изменяемому типу
		def Attribute battr = new Attribute();
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
		def Attribute battr = new Attribute();
		List<String> types = AttributeTypes.getAllNames();
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.DATE.name);//имитация выбора типа на форме.
		
		assertTrue(types.contains(battr.attributeType.name));

		//проверка получения полной информации по атрибуту
		assertEquals(battr.attributeType.name, AttributeTypes.DATE.name);
		assertEquals(battr.activeAttributeType, AttributeTypes.DATE.name);
		assertEquals(battr.attributeType.databases, AttributeTypes.DATE.databases);
		assertEquals(battr.attributeType.modifyable, AttributeTypes.DATE.modifyable);
	}
	
	
	/**
	 * Проверка 'String' атрибутов.
	 */
	@Test
	public void testStringAttributeType() {
		def Attribute battr = new Attribute();
		List<String> types = AttributeTypes.getAllNames();
		AttributeUtils.signTypeToAttr(battr, AttributeTypes.CLOB.name);//имитация выбора типа на форме.

		assertTrue(types.contains(battr.attributeType.name));
		
		//проверка получения полной информации по атрибуту
		assertEquals(battr.attributeType.name, AttributeTypes.CLOB.name);
		assertEquals(battr.activeAttributeType, AttributeTypes.CLOB.name);
		assertEquals(battr.attributeType.databases, AttributeTypes.CLOB.databases);
		assertEquals(battr.attributeType.modifyable, AttributeTypes.CLOB.modifyable);
	}
}
