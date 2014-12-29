package entity

import org.junit.Test
import utils.UserDataUtils
import attribute.AttributeTypes
import attribute.Attribute
import relationship.Relationship
import entity.Entity
import entity.Index
import static org.junit.Assert.*
import utils.*

public class IndexTest {
	
	//проверяется каскадное удаление атрибутов и связей, завязанных на индекс.
	@Test
	public void testSaveDeleteIndex() {
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
			EntityUtils.getCurrent(m2.id), IndexUtils.getCurrent(i.id), false, 1, 0)
		
		Relationship r = RelationshipUtils.getCurrent(relation.id)
		assertEquals(m1.id, r.fromEntityId)
		assertEquals(m2.id, r.toEntityId)
		
		def row = adUtils.getConnection().firstRow("select * from relation_to_attr")
		assertEquals(relation.id, row.relation_id)
		
		row = adUtils.getConnection().firstRow("select * from app_index")
		assertEquals(i.id, row.id)
			
		def rows = adUtils.getConnection().rows("select * from app_attribute"
			+ " where table_id = ?", [m2.id])
		assertEquals(2, rows.size())
		
		//delete index
		IndexUtils.deleteIndex(i.id)
		
		r = RelationshipUtils.getCurrent(relation.id)
		assertTrue(r.isDeleted)
		
		Index index = IndexUtils.getCurrent(i.id)
		assertTrue(index.isDeleted)
		
		adUtils.getConnection().eachRow("select * from app_attribute"
			+ " where table_id = ?", [m2.id]) {
			attr ->
			Attribute a = AttributeUtils.getCurrent(attr.id)
		    assertTrue(a.isDeleted)
		}

		adUtils.exitApplication()
	}
}
