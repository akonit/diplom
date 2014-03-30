package lowmodel.attribute;

import java.util.Arrays;

import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.StringTypes;
import lowmodel.entity.BasicEntity;
import lowmodel.relationship.BasicRelationship;

import org.junit.Test;
import static org.junit.Assert.*;

import utils.RelationshipUtils;

public class RelationshipTest {

	@Test
	public void testRelationshipCreation() {
		BasicEntity m1 = new BasicEntity();
		m1.setName("m1");
		BasicEntity m2 = new BasicEntity();
		m2.setName("m2");
		
		BasicAttribute a1 = new BasicAttribute();
		a1.setAttrType(AttributeType.STRING);
		a1.setSubAttributeType(StringTypes.CLOB);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		a1.setId(String.valueOf(System.currentTimeMillis()));
		m1.setAttributes(Arrays.asList(a1));
		
		BasicRelationship relation = RelationshipUtils.assignRelationship(m1, m2, a1);
		assertNotNull(m2.getAttributes());
		//создать метод assertAttributes в базовом классе. и базовый класс тоже создать
		assertEquals(m2.getAttributes().get(0).getAttrType(), a1.getAttrType());
		assertEquals(m2.getAttributes().get(0).getSubAttributeType(), a1.getSubAttributeType());
		assertEquals(m2.getAttributes().get(0).getDefinition(), a1.getDefinition());
		assertEquals(m2.getAttributes().get(0).getKeyGroup(), KeyGroup.FOREIGN_KEY);
		
		assertEquals(m2.getAttributes().get(0).getId(), relation.getToAttr().getId());
		assertEquals(a1.getId(), relation.getFromAttr().getId());
	}
	
	@Test
	public void testRelationshipDropping() {
		BasicEntity m1 = new BasicEntity();
		m1.setName("m1");
		BasicEntity m2 = new BasicEntity();
		m2.setName("m2");
		
		BasicAttribute a1 = new BasicAttribute();
		a1.setAttrType(AttributeType.STRING);
		a1.setSubAttributeType(StringTypes.CLOB);
		a1.setDefinition("test attr" + System.currentTimeMillis());
		a1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		a1.setId(String.valueOf(System.currentTimeMillis()));
		m1.setAttributes(Arrays.asList(a1));
		
		BasicRelationship relation = RelationshipUtils.assignRelationship(m1, m2, a1);
		assertNotNull(relation);
		assertNotNull(m2.attributes);
		assertEquals(m2.attributes.size(), 1);
		
		//drop it
		RelationshipUtils.dropRelationship(m2, relation.toAttr);
		assertTrue(m2.attributes.empty);
	}
}
