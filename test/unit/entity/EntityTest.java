package entity;

import java.util.Arrays;

import org.junit.Test;

import attribute.AttributeTypes;
import attribute.Attribute;
import attribute.KeyGroup;
import entity.Entity;
import static org.junit.Assert.*;

public class EntityTest {

	@Test
	public void testEntityCreation() throws Exception {
		Entity entity = new Entity();
		entity.setName("newEntity");
		
		Attribute battr1 = new Attribute();
		battr1.setAttributeType(AttributeTypes.VARCHAR);
		battr1.setDefinition("attr1");
		battr1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		battr1.setId(String.valueOf(System.currentTimeMillis()));

		Attribute battr2 = new Attribute();
		battr2.setAttributeType(AttributeTypes.TIMESTAMP);
		battr2.setDefinition("attr2");
		battr2.setKeyGroup(KeyGroup.FOREIGN_KEY);
		battr2.setId(String.valueOf(System.currentTimeMillis()));
		
		entity.setAttributes(Arrays.asList(battr1, battr2));
		
		assertEquals(entity.getAttributes().size(), 2);
	}
}
