package lowmodel.attribute;

import java.util.Arrays;

import lowmodel.entity.BasicEntity;
import lowmodel.attribute.BasicAttribute;
import lowmodel.attribute.type.AttributeType;
import lowmodel.attribute.type.DatetimeTypes;
import lowmodel.attribute.type.StringTypes;

import org.junit.Test;

import static org.junit.Assert.*;

public class EntityTest {

	@Test
	public void testEntityCreation() throws Exception {
		BasicEntity entity = new BasicEntity();
		entity.setName("newEntity");
		
		BasicAttribute battr1 = new BasicAttribute();
		battr1.setAttrType(AttributeType.STRING);
		battr1.setSubAttributeType(StringTypes.VARCHAR);
		battr1.setDefinition("attr1");
		battr1.setKeyGroup(KeyGroup.PRIMARY_KEY);
		battr1.setId(String.valueOf(System.currentTimeMillis()));

		BasicAttribute battr2 = new BasicAttribute();
		battr2.setAttrType(AttributeType.DATE_TIME);
		battr2.setSubAttributeType(DatetimeTypes.TIMESTAMP);
		battr2.setDefinition("attr2");
		battr2.setKeyGroup(KeyGroup.FOREIGN_KEY);
		battr2.setId(String.valueOf(System.currentTimeMillis()));
		
		entity.setAttributes(Arrays.asList(battr1, battr2));
		
		assertEquals(entity.getAttributes().size(), 2);
	}
}
