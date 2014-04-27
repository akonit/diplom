package relationship;

import java.util.Arrays;

import org.junit.Test;

import attribute.AttributeTypes;
import attribute.Attribute;
import attribute.KeyGroup;
import relationship.Relationship;
import entity.Entity;
import static org.junit.Assert.*;
import utils.RelationshipUtils;

public class RelationshipTest {

	@Test
	public void testRelationshipCreation() {
		Entity m1 = new Entity();
		m1.setName("m1");
		Entity m2 = new Entity();
		m2.setName("m2");
		
		Attribute a1 = new Attribute();
		a1.setAttributeType(AttributeTypes.CLOB);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		a1.setId(String.valueOf(System.currentTimeMillis()));
		m1.setAttributes(Arrays.asList(a1));
		
		Relationship relation = RelationshipUtils.assignRelationship(m1, m2, a1);
		assertNotNull(m2.getAttributes());
		//создать метод assertAttributes в базовом классе. и базовый класс тоже создать
		assertEquals(m2.getAttributes().get(0).getAttributeType(), a1.getAttributeType());
		assertEquals(m2.getAttributes().get(0).getDefinition(), a1.getDefinition());
		assertEquals(m2.getAttributes().get(0).getKeyGroup(), KeyGroup.FOREIGN_KEY);
		
		assertEquals(m2.getAttributes().get(0).getId(), relation.getToAttr().getId());
		assertEquals(a1.getId(), relation.getFromAttr().getId());
	}
	
	@Test
	public void testRelationshipDropping() {
		Entity m1 = new Entity();
		m1.setName("m1");
		Entity m2 = new Entity();
		m2.setName("m2");
		
		Attribute a1 = new Attribute();
		a1.setAttributeType(AttributeTypes.CLOB);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		a1.setId(String.valueOf(System.currentTimeMillis()));
		m1.setAttributes(Arrays.asList(a1));
		
		Relationship relation = RelationshipUtils.assignRelationship(m1, m2, a1);
		assertNotNull(relation);
		assertNotNull(m2.attributes);
		assertEquals(m2.attributes.size(), 1);
		
		//drop it
		RelationshipUtils.dropRelationship(m2, relation.toAttr);
		assertTrue(m2.attributes.empty);
	}
}
