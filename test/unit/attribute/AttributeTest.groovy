package attribute

import utils.*

import org.junit.Test
import org.junit.Ignore

import attribute.AttributeTypes
import attribute.Attribute
import entity.Entity
import static org.junit.Assert.*

//выделить как минимум assert в базовый класс
class AttributeTest {

	@Test
	public void testSaveDeleteAttribute() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		
		def Attribute attr = new Attribute()
		attr.attributeType = AttributeTypes.CLOB
		attr.activeAttributeType = AttributeTypes.CLOB
		attr.definition = "test attr"
		attr.id = System.currentTimeMillis()
		attr.name = "test attr name"
		
		AttributeUtils.createAttribute(attr, entity.id)
		def row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertEquals(attr.name, row.name)
		
		adUtils.save()
		
		AttributeUtils.deleteAttribute(attr.id)
		Attribute deleted = AttributeUtils.getCurrent(attr.id)
		assertTrue(deleted.isDeleted)
		
		adUtils.exitApplication()
	}

	// создали таблицу - не смогли создать атрибут - ничего не сохранилось
	@Test
	public void testFailedToSaveAttributeToFile() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		
		Attribute attr = new Attribute()
		attr.attributeType = AttributeTypes.CLOB
		attr.definition = "test attr"
		attr.id = System.currentTimeMillis()
		attr.name = null//constraint - name is not null
		
		boolean isException = false
		try {
		    AttributeUtils.createAttribute(attr, entity.id)
		} catch (Exception e) {
		    isException = true
		}
		
		assertTrue(isException)
		Attribute a = AttributeUtils.getCurrent(attr.id)
		assertNull(a)
		
		Entity e = EntityUtils.getCurrent(entity.id)
		assertNull(e)
		
		adUtils.exitApplication()
	}

	// создали таблицу - закоммитили - не смогли создать атрибут - таблица сохранилась
	@Test
	public void testFailedToSaveAttributeWithEntityCommitToFile() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		adUtils.save()
		
		Attribute attr = new Attribute()
		attr.attributeType = AttributeTypes.CLOB
		attr.definition = "test attr"
		attr.id = System.currentTimeMillis()
		attr.name = null//constraint - name is not null
		
		boolean isException = false
		try {
		    AttributeUtils.createAttribute(attr, entity.getId())
		} catch (Exception e) {
		    isException = true
		}
		
		assertTrue(isException)
		Attribute a = AttributeUtils.getCurrent(attr.id)
		assertNull(a)
		
		Entity e = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, e.name)
		
		adUtils.exitApplication()
	}
	
	/**
	 * Изменяемые типы изменяются, неизменяемые - нет.
	 */
	@Test
	public void testSubAttrTypeModification() {
		//назначение нового имени изменяемому типу
		def Attribute attr = new Attribute()
		AttributeUtils.signTypeToAttr(attr, AttributeTypes.NVARCHAR.name)
		
		assertEquals(attr.activeAttributeType, AttributeTypes.NVARCHAR.name)
		
		String myNewType = "NVARCHAR(128)"
		boolean isSuccess = attr.changeSubAttrType(myNewType)
		
		assertEquals(attr.activeAttributeType, myNewType)
		assertTrue(isSuccess);
		
		//назначение нового имени неизменяемому типу
		AttributeUtils.signTypeToAttr(attr, AttributeTypes.CHAR.name)
		assertEquals(attr.activeAttributeType, AttributeTypes.CHAR.name)
		
		myNewType = "CHAR!11"
		isSuccess = attr.changeSubAttrType(myNewType)
		
		assertEquals(attr.activeAttributeType, AttributeTypes.CHAR.name)
		assertFalse(isSuccess)
	}
	
	/**
	 * Проверка 'Datetime' атрибутов.
	 */
	@Test
	public void testDatetimeAttributeType() {
		def Attribute attr = new Attribute()
		List<String> types = AttributeTypes.getAllNames()
		AttributeUtils.signTypeToAttr(attr, AttributeTypes.DATE.name)//имитация выбора типа на форме.
		
		assertTrue(types.contains(attr.attributeType.name));

		//проверка получения полной информации по атрибуту
		assertEquals(attr.attributeType.name, AttributeTypes.DATE.name)
		assertEquals(attr.activeAttributeType, AttributeTypes.DATE.name)
		assertEquals(attr.attributeType.databases, AttributeTypes.DATE.databases)
		assertEquals(attr.attributeType.modifyable, AttributeTypes.DATE.modifyable)
	}
	
	
	/**
	 * Проверка 'String' атрибутов.
	 */
	@Test
	public void testStringAttributeType() {
		def Attribute attr = new Attribute()
		List<String> types = AttributeTypes.getAllNames()
		AttributeUtils.signTypeToAttr(attr, AttributeTypes.CLOB.name)//имитация выбора типа на форме.

		assertTrue(types.contains(attr.attributeType.name))
		
		//проверка получения полной информации по атрибуту
		assertEquals(attr.attributeType.name, AttributeTypes.CLOB.name)
		assertEquals(attr.activeAttributeType, AttributeTypes.CLOB.name)
		assertEquals(attr.attributeType.databases, AttributeTypes.CLOB.databases)
		assertEquals(attr.attributeType.modifyable, AttributeTypes.CLOB.modifyable)
	}
}
