package sql.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import attribute.Attribute;
import attribute.AttributeTypes;
import static org.junit.Assert.*;
import relationship.Relationship;
import sql.ConnectionManager;
import sql.Database;
import utils.*;
import entity.Entity;
import entity.Index;
import groovy.sql.Sql;

public class ConverterTest {

	@Test
	public void testNotExistingConverter() {
		ConverterCommander cc = new ConverterCommander();
		boolean isException = false;
		
		try {
			cc.convertToSql(null, Arrays.asList(new Entity()), Arrays.asList(new Relationship()));
		} catch (Exception e) {
			isException = true;
		}
		
		assertTrue(isException);
	}
	
	@Test
	public void testMySqlConverter() throws Exception {
		UserDataUtils adUtils = new UserDataUtils();
		String name = "myNewDb" + System.currentTimeMillis();
		adUtils.createNewFile(name);
		
		ConverterCommander cc = new ConverterCommander();
		
		Entity entity1 = new Entity();
		entity1.setName("entity_1_" + System.currentTimeMillis());
		EntityUtils.createEntity(entity1);
		Attribute attr1 = new Attribute();
		attr1.setAttributeType(AttributeTypes.DATE);
		attr1.setActiveAttributeType(AttributeTypes.DATE.getName())
		attr1.setName("testDate1");
		attr1.setDefinition("тестовый атрибут");
		attr1.setId(System.currentTimeMillis());
		attr1.setActiveAttributeType(attr1.getAttributeType().getName());
		attr1.getConstraints().setNullable(false);
		attr1.getConstraints().setPrimary(true);
		AttributeUtils.createAttribute(attr1, entity1.getId())
		List<Attribute> attrs = new ArrayList<>();
		attrs.add(attr1);
		entity1.setAttributes(attrs);

		Entity entity2 = new Entity();
		entity2.setName("entity_2_" + System.currentTimeMillis());
		EntityUtils.createEntity(entity2);
		Attribute attr2 = new Attribute();
		attr2.setAttributeType(AttributeTypes.DATE);
		attr2.setActiveAttributeType(AttributeTypes.DATE.getName())
		attr2.setName("testDate2");
		attr2.setDefinition("тестовый атрибут");
		attr2.setId(System.currentTimeMillis());
		attr2.setActiveAttributeType(attr2.getAttributeType().getName());
		attr2.getConstraints().setUnique(true);
		AttributeUtils.createAttribute(attr2, entity2.getId())
		Attribute attr3 = new Attribute();
		attr3.setAttributeType(AttributeTypes.VARCHAR);
		attr3.setName("testChar");
		attr3.setDefinition("тестовый атрибут");
		attr3.setId(System.currentTimeMillis());
		attr3.setActiveAttributeType("VARCHAR(64)");		
		attr3.getConstraints().setUnique(true);
		AttributeUtils.createAttribute(attr3, entity2.getId())
		entity2.setAttributes(Arrays.asList(attr2, attr3));
		
		Index i = new Index();
		i.setName("testIndex");
		entity2.getIndexes().add(i);
		i.addAttribute(attr2);
		i.addAttribute(attr3);
		IndexUtils.createIndex(i, entity2.getId())
		
		Relationship r1 = RelationshipUtils.assignRelationship(entity2, entity1, i, true);
		List<Relationship> relations = Arrays.asList(r1);
		
		List<Entity> entities = Arrays.asList(entity1, entity2);
		String sqlScript = cc.convertToSql(Database.MY_SQL, entities, relations);
		System.out.println(sqlScript);
		assertTrue(sqlScript.contains(entity1.getName()));
		assertTrue(sqlScript.contains(entity2.getName()));
		
		/*
		 * Проверка наката полученного скрипта на примере конкретной БД. тест валидный, 
		 * настройки подключения подставлять свои.	
		String url = "jdbc:mysql://localhost/PRODUCTS";
		String user = "root";
		String password = "password";
		ConnectionManager cm = new ConnectionManager();
		Sql sql = cm.openAndGetConnection(url, user, password, Database.MY_SQL);
		String[] statements = sqlScript.split(";");
		for (String statement : statements) {
			sql.execute(statement.concat(";"));
		}
		cm.closeConnection(sql);	
		 */
	}
}
