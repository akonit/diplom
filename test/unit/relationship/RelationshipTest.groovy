package relationship;

import java.util.Arrays;

import org.junit.Test;

import attribute.AttributeTypes;
import attribute.Attribute;
import relationship.Relationship;
import entity.Entity;
import entity.Index
import static org.junit.Assert.*;
import utils.*;
import utils.UserDataUtils

public class RelationshipTest {

	@Test
	public void testRelationshipCreation() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity m1 = new Entity();
		m1.setName("m1");
		EntityUtils.createEntity(m1);
		Index i = new Index();
		i.name = "testIndex";
		m1.indexes.add(i);
		Entity m2 = new Entity();
		m2.setName("m2");
		EntityUtils.createEntity(m2);
		
		Attribute a1 = new Attribute();
		a1.setAttributeType(AttributeTypes.CLOB);
		a1.setActiveAttributeType(AttributeTypes.CLOB.name);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.name = "a1"
		a1.setId(System.currentTimeMillis());
		AttributeUtils.createAttribute(a1, m1.getId())
		m1.setAttributes(Arrays.asList(a1));
		i.addAttribute(a1)
		IndexUtils.createIndex(i, m1.getId())
		
		Relationship relation = RelationshipUtils.assignRelationship(m1, m2, i, false);
		assertNotNull(m2.getAttributes());
		//создать метод assertAttributes в базовом классе. и базовый класс тоже создать
		assertEquals(m2.getAttributes().get(0).getAttributeType(), a1.getAttributeType());
		assertEquals(m2.getAttributes().get(0).getDefinition(), a1.getDefinition());
		assertTrue(m2.getAttributes().get(0).getConstraints().isForeign());
		
		assertTrue(relation.getToAttr().contains(m2.getAttributes().get(0)));
		
		def row = adUtils.getConnection().firstRow("select * from app_relation")
		assertEquals(m1.getId(), row.table_from_id)
		assertEquals(m2.getId(), row.table_to_id)
		adUtils.exitApplication();
	}
	
	@Test
	public void testRelationshipDropping() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity m1 = new Entity();
		m1.setName("m1");
		EntityUtils.createEntity(m1);
		Index i = new Index();
		i.name = "testIndex";
		m1.indexes.add(i);
		Entity m2 = new Entity();
		m2.setName("m2");
		EntityUtils.createEntity(m2);
		
		Attribute a1 = new Attribute();
		a1.setAttributeType(AttributeTypes.CLOB);
		a1.setActiveAttributeType(AttributeTypes.CLOB.name);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.name = "a1"
		a1.setId(System.currentTimeMillis());
		AttributeUtils.createAttribute(a1, m1.getId())
		i.addAttribute(a1)
		Attribute a2 = new Attribute();
		a2.setAttributeType(AttributeTypes.CLOB);
		a2.setActiveAttributeType(AttributeTypes.CLOB.name);
		a2.setDefinition("test attr2" + System.currentTimeMillis());
		a2.name = "a2"
		a2.setId(System.currentTimeMillis());
		AttributeUtils.createAttribute(a2, m1.getId())
		i.addAttribute(a2)
		IndexUtils.createIndex(i, m1.getId())
		
		m1.setAttributes(Arrays.asList(a1, a2));
		
		Relationship relation = RelationshipUtils.assignRelationship(m1, m2, i, false);
		assertNotNull(relation);
		assertNotNull(m2.attributes);
		assertEquals(m2.attributes.size(), 2);
		
		//drop it
		RelationshipUtils.dropRelationship(m2, relation);
		assertTrue(m2.attributes.empty);
	}
}
