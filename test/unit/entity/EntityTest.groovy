package entity

import java.util.Arrays

import org.junit.Test

import utils.*
import attribute.AttributeTypes
import attribute.Attribute
import entity.Entity
import static org.junit.Assert.*

public class EntityTest {

	@Test
	public void testEntityCreation() throws Exception {
		Entity entity = new Entity()
		entity.setName("newEntity")
		
		Attribute attr1 = new Attribute()
		attr1.setAttributeType(AttributeTypes.VARCHAR)
		attr1.setDefinition("attr1")
		attr1.setId(System.currentTimeMillis())

		Attribute attr2 = new Attribute()
		attr2.setAttributeType(AttributeTypes.TIMESTAMP)
		attr2.setDefinition("attr2")
		attr2.setId(System.currentTimeMillis())
		
		entity.setAttributes(Arrays.asList(attr1, attr2))
		
		assertEquals(entity.getAttributes().size(), 2)
	}
	
	@Test
	public void testSaveEntityToFile() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity entity = new Entity()
		entity.setName("newEntity")
		entity.setCommentary("test")
		
		EntityUtils.createEntity(entity)
		def row = adUtils.getConnection().firstRow("select * from app_table")
		assertEquals(entity.getName(), row.name)
		
		adUtils.exitApplication()
	}
	
	//test on delete cascade 
	@Test
	public void testSaveDelete() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name);
		
		Entity entity = new Entity()
		entity.setName("newEntity")
		entity.setCommentary("test")
		
		EntityUtils.createEntity(entity)
		def row = adUtils.getConnection().firstRow("select * from app_table")
		assertEquals(entity.getName(), row.name)
		
		def Attribute attr = new Attribute();
		attr.attributeType = AttributeTypes.CLOB;
		attr.activeAttributeType = AttributeTypes.CLOB;
		attr.definition = "test attr";
		attr.id = System.currentTimeMillis();
		attr.name = "test attr name"
		
		AttributeUtils.createAttribute(attr, entity.getId())
		row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertEquals(attr.getName(), row.name)
		
		adUtils.save()//commit
		
		EntityUtils.deleteEntity(entity.getId())
		row = adUtils.getConnection().firstRow("select * from app_table")
		assertNull(row)
		row = adUtils.getConnection().firstRow("select * from app_attribute")
		assertNull(row)
		
		adUtils.exitApplication()
	}
}
