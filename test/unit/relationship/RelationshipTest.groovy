package relationship

import java.util.Arrays

import org.junit.Test
import org.junit.Ignore

import attribute.AttributeTypes
import attribute.Attribute
import relationship.Relationship
import entity.Entity
import entity.Index
import static org.junit.Assert.*
import utils.*

public class RelationshipTest {

	@Test
	public void testRelationshipCreation() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity m1 = new Entity()
		m1.name = "m1"
		EntityUtils.createEntity(m1)
		Index i = new Index()
		i.name = "testIndex"
		m1.indexes.add(i)
		Entity m2 = new Entity()
		m2.name = "m2"
		EntityUtils.createEntity(m2)
		
		Attribute a1 = new Attribute()
		a1.attributeType = AttributeTypes.CLOB
		a1.activeAttributeType = AttributeTypes.CLOB.name
		a1.definition = "test attr" + System.currentTimeMillis()
		a1.name = "a1"
		a1.constraints.primary = true
		a1.id = System.currentTimeMillis()
		AttributeUtils.createAttribute(a1, m1.id)
		m1.attributes = Arrays.asList(a1)
		i.addAttribute(a1)
		IndexUtils.createIndex(i, m1.id)
		
		Relationship relation = RelationshipUtils.createRelationship(EntityUtils.getCurrent(m1.id), 
			EntityUtils.getCurrent(m2.id), IndexUtils.getCurrent(i.id), false,
			4, 3);
		
		Relationship r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		adUtils.exitApplication()
	}
	
	@Test
	public void testRelationshipDropping() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity m1 = new Entity()
		m1.name = "m1"
		EntityUtils.createEntity(m1)
		Index i = new Index()
		i.name = "testIndex"
		m1.indexes.add(i)
		Entity m2 = new Entity()
		m2.name = "m2"
		EntityUtils.createEntity(m2)
		
		Attribute a1 = new Attribute()
		a1.attributeType = AttributeTypes.CLOB
		a1.activeAttributeType = AttributeTypes.CLOB.name
		a1.definition = "test attr" + System.currentTimeMillis()
		a1.name = "a1"
		a1.constraints.primary = true
		a1.id = System.currentTimeMillis()
		AttributeUtils.createAttribute(a1, m1.id)
		i.addAttribute(a1)
		Attribute a2 = new Attribute()
		a2.attributeType = AttributeTypes.CLOB
		a2.activeAttributeType = AttributeTypes.CLOB.name
		a2.definition = "test attr2" + System.currentTimeMillis()
		a2.name = "a2"
		a2.constraints.primary = true
		a2.id = System.currentTimeMillis()
		AttributeUtils.createAttribute(a2, m1.id)
		i.addAttribute(a2)
		IndexUtils.createIndex(i, m1.id)
		
		m1.attributes = Arrays.asList(a1, a2)
		
		Relationship relation = RelationshipUtils.createRelationship(EntityUtils.getCurrent(m1.id), 
			EntityUtils.getCurrent(m2.id), IndexUtils.getCurrent(i.id), false,
			1, 0)
		
		Relationship r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		
		def row = adUtils.getConnection().firstRow("select * from relation_to_attr")
		assertEquals(relation.id, row.relation_id)
			
		def rows = adUtils.getConnection().rows("select * from app_attribute"
			+ " where table_id = ?", [m2.id])
		assertEquals(2, rows.size())
		
		//drop it
		RelationshipUtils.deleteRelationship(relation.id)
		
		r = RelationshipUtils.getCurrent(relation.id)
		assertTrue(r.isDeleted)
		
		row = adUtils.getConnection().firstRow("select * from relation_to_attr order by time desc")
		assertEquals(1, row.is_deleted)
		
		rows = adUtils.getConnection().rows("select * from app_attribute"
			+ " where table_id = ?", [m2.id])
		assertEquals(4, rows.size)
		adUtils.exitApplication()
	}
	
	@Test
	public void testUndoRedo() throws Exception {
		//create data
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity m1 = new Entity()
		m1.name = "m1"
		//first transaction
		EntityUtils.createEntity(m1)
		Index i = new Index()
		i.name = "testIndex"
		m1.indexes.add(i)
		Entity m2 = new Entity()
		m2.name = "m2"
		//second transaction
		EntityUtils.createEntity(m2)
		
		Attribute a1 = new Attribute()
		a1.attributeType = AttributeTypes.CLOB
		a1.activeAttributeType = AttributeTypes.CLOB.name
		a1.definition = "test attr" + System.currentTimeMillis()
		a1.name = "a1"
		a1.constraints.primary = true
		a1.id = System.currentTimeMillis()
		AttributeUtils.createAttribute(a1, m1.id)
		m1.attributes = Arrays.asList(a1)
		i.addAttribute(a1)
		//third transaction
		IndexUtils.createIndex(i, m1.id)
		
		//fourth transaction
		Relationship relation = RelationshipUtils.createRelationship(EntityUtils.getCurrent(m1.id), 
			EntityUtils.getCurrent(m2.id), IndexUtils.getCurrent(i.id), false,
			2, null)
		
		Relationship r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		
		//undo
		UserDataUtils.undo()
		r = RelationshipUtils.getCurrent(relation.id)
		assertNull(r)
		
		def rows = UserDataUtils.getConnection().rows("select * from app_attribute "
			+ " where status = ? and table_id = ?", Status.DONE.name, m2.id)
		assertEquals(0, rows.size)//undo created with relation attributes - one transaction
		
		// redo
		UserDataUtils.redo()
		r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		
		rows = UserDataUtils.getConnection().rows("select * from app_attribute "
			+ " where status = ? and table_id = ?", Status.DONE.name, m2.id)
		assertEquals(1, rows.size)//redo created with relation attributes - one transaction
	}
	
	@Test
	public void testCleanUp() throws Exception {
		//create data
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity m1 = new Entity()
		m1.name = "m1"
		//first transaction
		EntityUtils.createEntity(m1)
		Index i = new Index()
		i.name = "testIndex"
		m1.indexes.add(i)
		Entity m2 = new Entity()
		m2.name = "m2"
		//second transaction
		EntityUtils.createEntity(m2)
		
		Attribute a1 = new Attribute()
		a1.attributeType = AttributeTypes.CLOB
		a1.activeAttributeType = AttributeTypes.CLOB.name
		a1.definition = "test attr" + System.currentTimeMillis()
		a1.name = "a1"
		a1.constraints.primary = true
		a1.id = System.currentTimeMillis()
		AttributeUtils.createAttribute(a1, m1.id)
		m1.attributes = Arrays.asList(a1)
		i.addAttribute(a1)
		//third transaction
		IndexUtils.createIndex(i, m1.id)
		
		//fourth transaction
		Relationship relation = RelationshipUtils.createRelationship(EntityUtils.getCurrent(m1.id), 
			EntityUtils.getCurrent(m2.id), IndexUtils.getCurrent(i.id), false,
			3, 0)
		
		Relationship r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		
		//update
		m1.name = "m1_updated"
		EntityUtils.updateEntity(m1)
		
		m2.name = "m2_updated"
		EntityUtils.updateEntity(m2)
		
		// check current state
		def rows = UserDataUtils.getConnection().rows("select * from app_table")
		assertEquals(4, rows.size())
		
		//clean up
		UserDataUtils.cleanUp()
		rows = UserDataUtils.getConnection().rows("select * from app_table")
		assertEquals(2, rows.size())//get rid of pre-update versions
		assertEquals(m1.name, EntityUtils.getCurrent(m1.id).name)
		assertEquals(m2.name, EntityUtils.getCurrent(m2.id).name)
	}
}
