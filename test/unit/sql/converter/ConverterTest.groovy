package sql.converter

import java.util.ArrayList
import java.util.Arrays
import java.util.List

import org.junit.Test
import org.junit.Ignore

import attribute.Attribute
import attribute.AttributeTypes
import static org.junit.Assert.*
import relationship.Relationship
import sql.ConnectionManager
import sql.Database
import utils.*
import entity.Entity
import entity.Index
import groovy.sql.Sql

public class ConverterTest {

	@Test
	@Ignore
	public void testNotExistingConverter() {
		ConverterCommander cc = new ConverterCommander()
		boolean isException = false
		
		try {
			cc.convertToSql(null, Arrays.asList(new Entity()), Arrays.asList(new Relationship()))
		} catch (Exception e) {
			isException = true
		}
		
		assertTrue(isException)
	}
	
	@Test
	public void testMySqlConverter1() throws Exception {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		ConverterCommander cc = new ConverterCommander()
		
		Entity entity1 = new Entity()
		entity1.name = "entity_1_" + System.currentTimeMillis()
		EntityUtils.createEntity(entity1)
		Attribute attr1 = new Attribute()
		attr1.attributeType = AttributeTypes.DATE
		attr1.activeAttributeType = AttributeTypes.DATE.name
		attr1.name = "testDate1"
		attr1.definition = "тестовый атрибут"
		attr1.id = System.currentTimeMillis()
		attr1.constraints.nullable = false
		attr1.constraints.primary = true
		AttributeUtils.createAttribute(attr1, entity1.id)
		List<Attribute> attrs1 = new ArrayList<>()
		attrs1.add(attr1)
		entity1.attributes = attrs1

		Entity entity2 = new Entity()
		entity2.name = "entity_2_" + System.currentTimeMillis()
		EntityUtils.createEntity(entity2)
		Attribute attr2 = new Attribute()
		attr2.attributeType = AttributeTypes.DATE
		attr2.activeAttributeType = AttributeTypes.DATE.name
		attr2.name = "testDate2"
		attr2.definition = "тестовый атрибут"
		attr2.id = System.currentTimeMillis()
		attr2.constraints.unique = true
		AttributeUtils.createAttribute(attr2, entity2.id)
		Attribute attr3 = new Attribute()
		attr3.attributeType = AttributeTypes.VARCHAR
		attr3.activeAttributeType = "VARCHAR(64)"
		attr3.name = "testChar"
		attr3.definition = "тестовый атрибут"
		attr3.id = System.currentTimeMillis()	
		attr3.constraints.unique = true
		AttributeUtils.createAttribute(attr3, entity2.id)
		List<Attribute> attrs2 = new ArrayList<>()
		attrs2.add(attr2)
		attrs2.add(attr3)
		entity2.attributes = attrs2
		
		Index i = new Index()
		i.name = "testIndex"
		i.addAttribute(attr2)
		i.addAttribute(attr3)
		IndexUtils.createIndex(i, entity2.id)
		
		Relationship r1 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity2.id), 
			EntityUtils.getCurrent(entity1.id), 
			IndexUtils.getCurrent(i.id), false, 1, 1)
		
		String sqlScript = cc.convertToSql(Database.MY_SQL);
		System.out.println(sqlScript)
		assertTrue(sqlScript.contains(entity1.name))
		assertTrue(sqlScript.contains(entity2.name))
		
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
