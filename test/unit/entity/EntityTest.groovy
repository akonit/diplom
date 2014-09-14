package entity;

import java.util.Arrays;

import org.junit.Test;

import utils.EntityUtils;
import utils.UserDataUtils;
import attribute.AttributeTypes;
import attribute.Attribute;
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
		battr1.setId(System.currentTimeMillis());

		Attribute battr2 = new Attribute();
		battr2.setAttributeType(AttributeTypes.TIMESTAMP);
		battr2.setDefinition("attr2");
		battr2.setId(System.currentTimeMillis());
		
		entity.setAttributes(Arrays.asList(battr1, battr2));
		
		assertEquals(entity.getAttributes().size(), 2);
	}
	
	@Test
	public void testSaveEntityToFile() {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		Entity entity = new Entity();
		entity.setName("newEntity");
		entity.setCommentary("test");
		
		EntityUtils.createEntity(entity);
		def row = adUtils.getConnection().firstRow("select * from app_table")
		assertEquals(entity.getName(), row.name)
		
		adUtils.exitApplication();
	}
}
