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
		entity.xCoord = 10
		entity.yCoord = 50
		entity.height = 100
		entity.width = 150
		
		
		EntityUtils.createEntity(entity)
		Entity fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
		assertEquals(entity.xCoord, fromFile.xCoord)
		assertEquals(entity.yCoord, fromFile.yCoord)
		assertEquals(entity.height, fromFile.height)
		assertEquals(entity.width, fromFile.width)
		
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
		entity.xCoord = 10
		entity.yCoord = 50
		
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
		assertEquals(entity.name, fromFile.name)
		assertEquals(false, fromFile.isDeleted)
		
		def Attribute attr = new Attribute();
		attr.attributeType = AttributeTypes.CLOB;
		attr.activeAttributeType = AttributeTypes.CLOB;
		attr.definition = "test attr";
		attr.id = System.currentTimeMillis();
		attr.name = "test attr name"
		
		AttributeUtils.createAttribute(attr, entity.id)
		Attribute fileAttr = AttributeUtils.getCurrent(attr.id)
		assertEquals(attr.name, fileAttr.name)
		
		adUtils.save()//commit
		
		EntityUtils.deleteEntity(entity.id)
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
		entity.name = "newEntity"
		
		// create
		EntityUtils.createEntity(entity)
		def fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)

		// undo create
		UserDataUtils.undo()
		fromFile = UserDataUtils.getConnection().firstRow("select * from app_table order by time desc")
		assertEquals(Status.UNDONE.name, fromFile.status)
		fromFile = EntityUtils.getCurrent(entity.id)
		assertNull(fromFile)
		
		//redo create
		UserDataUtils.redo()
		fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
	}
	
	@Test
	public void testUndoRedoDelete() throws Exception {
		String name = "myNewDb" + System.currentTimeMillis()
		UserDataUtils.createNewFile(name);
		
		Entity entity = new Entity()
		entity.name = "newEntity"
		
		// create
		EntityUtils.createEntity(entity)
		def fromFile = EntityUtils.getCurrent(entity.id)
		assertEquals(entity.name, fromFile.name)
		
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
