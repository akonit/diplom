package entity

import java.util.Arrays

import org.junit.Test
import org.junit.Ignore

import utils.*
import attribute.AttributeTypes
import attribute.Attribute
import entity.Entity
import static org.junit.Assert.*

public class EntityTest {
	
	@Test
	public void testCreateEntity() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		Entity fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
		
		adUtils.exitApplication()
	}
	
	@Test
	public void testUpdateEntity() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		Entity fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
		
		entity.name = "newName"
		EntityUtils.updateEntity(entity)
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
		
		adUtils.exitApplication()
	}
	
	@Test
	public void testDelete() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name);
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		entity.commentary = "test"
		
		EntityUtils.createEntity(entity)
		Entity fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.getName(), fromFile.getName())
		assertEquals(false, fromFile.isDeleted)
		
		def Attribute attr = new Attribute();
		attr.attributeType = AttributeTypes.CLOB;
		attr.activeAttributeType = AttributeTypes.CLOB;
		attr.definition = "test attr";
		attr.id = System.currentTimeMillis();
		attr.name = "test attr name"
		
		AttributeUtils.createAttribute(attr, entity.getId())
		Attribute fileAttr = AttributeUtils.getCurrent(attr.id)
		assertEquals(attr.getName(), fileAttr.name)
		
		adUtils.save()//commit
		
		EntityUtils.deleteEntity(entity.getId())
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(true, fromFile.isDeleted)
		fileAttr = AttributeUtils.getCurrent(attr.id)
		assertTrue(fileAttr.isDeleted)
		
		adUtils.exitApplication()
	}
	
	@Test
	public void testUndoRedoCreation() throws Exception {
		String name = "myNewDb" + System.currentTimeMillis()
		UserDataUtils.createNewFile(name);
		
		Entity entity = new Entity()
		entity.setName("newEntity")
		
		// create
		EntityUtils.createEntity(entity)
		def fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.getName(), fromFile.name)

		// undo create
		UserDataUtils.undo()
		fromFile = UserDataUtils.getConnection().firstRow("select * from app_table order by time desc")
		assertEquals(Status.UNDONE.name, fromFile.status)
		fromFile = EntityUtils.getCurrent(entity.id)
		assertNull(fromFile)
		
		//redo create
		UserDataUtils.redo()
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.getName(), fromFile.name)
	}
	
	@Test
	public void testUndoRedoDelete() throws Exception {
		String name = "myNewDb" + System.currentTimeMillis()
		UserDataUtils.createNewFile(name);
		
		Entity entity = new Entity()
		entity.setName("newEntity")
		
		// create
		EntityUtils.createEntity(entity)
		def fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.getName(), fromFile.name)
		
		//delete
		EntityUtils.deleteEntity(entity.id)
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(true, fromFile.isDeleted)

		// undo delete
		UserDataUtils.undo()
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(false, fromFile.isDeleted)
		
		//redo delete
		UserDataUtils.redo()
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(true, fromFile.isDeleted)
	}
}
