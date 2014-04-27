package sql.converter;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import attribute.Attribute;
import attribute.AttributeTypes;
import static org.junit.Assert.*;
import relationship.Relationship;
import sql.ConnectionManager;
import sql.Database;
import entity.Entity;
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
		ConverterCommander cc = new ConverterCommander();
		List<Relationship> relations = Arrays.asList();
		
		Entity entity1 = new Entity();
		entity1.setName("entity_1_" + System.currentTimeMillis());
		Attribute attr1 = new Attribute();
		attr1.setAttributeType(AttributeTypes.DATE);
		attr1.setName("testDate1");
		attr1.setDefinition("тестовый атрибут");
		attr1.setId(String.valueOf(System.currentTimeMillis()));
		attr1.setActiveAttributeType(attr1.getAttributeType().getName());
		entity1.setAttributes(Arrays.asList(attr1));

		Entity entity2 = new Entity();
		entity2.setName("entity_2_" + System.currentTimeMillis());
		Attribute attr2 = new Attribute();
		attr2.setAttributeType(AttributeTypes.DATE);
		attr2.setName("testDate2");
		attr2.setDefinition("тестовый атрибут");
		attr2.setId(String.valueOf(System.currentTimeMillis()));
		attr2.setActiveAttributeType(attr2.getAttributeType().getName());
		Attribute attr3 = new Attribute();
		attr3.setAttributeType(AttributeTypes.VARCHAR);
		attr3.setName("testChar");
		attr3.setDefinition("тестовый атрибут");
		attr3.setId(String.valueOf(System.currentTimeMillis()));
		attr3.setActiveAttributeType("VARCHAR(64)");		
		entity2.setAttributes(Arrays.asList(attr2, attr3));
		
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
