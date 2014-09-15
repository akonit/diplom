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
	//удаление из оперативной памяти здесь не происходит
	@Test
	public void testSaveDeleteIndex() {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		Entity m1 = new Entity()
		m1.setName("m1")
		EntityUtils.createEntity(m1)
		Index i = new Index()
		i.name = "testIndex"
		m1.indexes.add(i)
		Entity m2 = new Entity()
		m2.setName("m2")
		EntityUtils.createEntity(m2)
		
		Attribute a1 = new Attribute()
		a1.setAttributeType(AttributeTypes.CLOB)
		a1.setActiveAttributeType(AttributeTypes.CLOB.name)
		a1.setDefinition("test attr" + System.currentTimeMillis())
		a1.name = "a1"
		a1.setId(System.currentTimeMillis())
		AttributeUtils.createAttribute(a1, m1.getId())
		i.addAttribute(a1)
		Attribute a2 = new Attribute()
		a2.setAttributeType(AttributeTypes.CLOB)
		a2.setActiveAttributeType(AttributeTypes.CLOB.name)
		a2.setDefinition("test attr2" + System.currentTimeMillis())
		a2.name = "a2"
		a2.setId(System.currentTimeMillis())
		AttributeUtils.createAttribute(a2, m1.getId())
		i.addAttribute(a2)
		IndexUtils.createIndex(i, m1.getId())
		
		m1.setAttributes(Arrays.asList(a1, a2))
		
		Relationship relation = RelationshipUtils.assignRelationship(m1, m2, i, false)
		assertNotNull(relation)
		assertNotNull(m2.attributes)
		assertEquals(m2.attributes.size(), 2)
		
		def row = adUtils.getConnection().firstRow("select * from app_relation")
		assertEquals(m1.getId(), row.table_from_id)
		assertEquals(m2.getId(), row.table_to_id)
		
		row = adUtils.getConnection().firstRow("select * from relation_to_attr")
		assertEquals(relation.id, row.relation_id)
		
		row = adUtils.getConnection().firstRow("select * from app_index")
		assertEquals(i.id, row.id)
			
		def rows = adUtils.getConnection().rows("select * from app_attribute"
			+ " where table_id = ?", [m2.id])
		assertEquals(2, rows.size())
		
		//delete index
		IndexUtils.deleteIndex(i.id)
		
		row = adUtils.getConnection().firstRow("select * from app_relation")
		assertNull(row)
		
		row = adUtils.getConnection().firstRow("select * from relation_to_attr")
		assertNull(row)
		
		row = adUtils.getConnection().firstRow("select * from app_index")
		assertNull(row)
		
		rows = adUtils.getConnection().rows("select * from app_attribute"
			+ " where table_id = ?", [m2.id])
		assertTrue(rows.empty)
		adUtils.exitApplication()
	}
}
