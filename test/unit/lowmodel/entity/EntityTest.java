package lowmodel.entity;

import java.util.Arrays;

import lowmodel.entity.BasicEntity;
import lowmodel.attribute.BasicAttribute;
import lowmodel.attribute.KeyGroup;
import lowmodel.attribute.type.AttributeTypes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityTest {

	@Test
	public void testEntityCreation() throws Exception {
		BasicEntity entity = new BasicEntity();
		entity.setName("newEntity");
		
		BasicAttribute battr1 = new BasicAttribute();
		battr1.setAttributeType(AttributeTypes.VARCHAR);
		battr1.setDefinition("attr1");
		battr1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		battr1.setId(String.valueOf(System.currentTimeMillis()));

		BasicAttribute battr2 = new BasicAttribute();
		battr2.setAttributeType(AttributeTypes.TIMESTAMP);
		battr2.setDefinition("attr2");
		battr2.setKeyGroup(KeyGroup.FOREIGN_KEY);
		battr2.setId(String.valueOf(System.currentTimeMillis()));
		
		entity.setAttributes(Arrays.asList(battr1, battr2));
		
		assertEquals(entity.getAttributes().size(), 2);
	}
}
