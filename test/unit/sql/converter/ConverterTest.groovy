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
		entity1.name = "table1"
		EntityUtils.createEntity(entity1)
		Attribute attr1 = new Attribute()
		attr1.attributeType = AttributeTypes.DATE
		attr1.activeAttributeType = AttributeTypes.DATE.name
		attr1.name = "attr1"
		attr1.definition = "тестовый атрибут"
		attr1.id = System.currentTimeMillis()
		attr1.constraints.nullable = false
		attr1.constraints.primary = true
		AttributeUtils.createAttribute(attr1, entity1.id)
		List<Attribute> attrs1 = new ArrayList<>()
		attrs1.add(attr1)
		entity1.attributes = attrs1

		Entity entity2 = new Entity()
		entity2.name = "table2"
		EntityUtils.createEntity(entity2)
		Attribute attr2 = new Attribute()
		attr2.attributeType = AttributeTypes.DATE
		attr2.activeAttributeType = AttributeTypes.DATE.name
		attr2.name = "attr2"
		attr2.definition = "тестовый атрибут"
		attr2.id = System.currentTimeMillis()
		attr2.constraints.primary = true
		AttributeUtils.createAttribute(attr2, entity2.id)
		Attribute attr3 = new Attribute()
		attr3.attributeType = AttributeTypes.VARCHAR
		attr3.activeAttributeType = "VARCHAR(64)"
		attr3.name = "attr3"
		attr3.definition = "тестовый атрибут"
		attr3.id = System.currentTimeMillis()	
		attr3.constraints.primary = true
		AttributeUtils.createAttribute(attr3, entity2.id)
		List<Attribute> attrs2 = new ArrayList<>()
		attrs2.add(attr2)
		attrs2.add(attr3)
		entity2.attributes = attrs2
		
		Entity entity3 = new Entity()
		entity3.name = "table3"
		EntityUtils.createEntity(entity3)
		Attribute attr4 = new Attribute()
		attr4.attributeType = AttributeTypes.INTEGER
		attr4.activeAttributeType = AttributeTypes.INTEGER.name
		attr4.name = "attr4"
		attr4.definition = "тестовый атрибут"
		attr4.id = System.currentTimeMillis()
		attr4.constraints.nullable = false
		attr4.constraints.primary = true
		AttributeUtils.createAttribute(attr4, entity3.id)
		List<Attribute> attrs3 = new ArrayList<>()
		attrs3.add(attr4)
		entity3.attributes = attrs3
		
		Index i = new Index()
		i.name = "testIndex"
		i.addAttribute(attr2)
		i.addAttribute(attr3)
		IndexUtils.createIndex(i, entity2.id)
		
		Relationship r1 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity2.id), 
			EntityUtils.getCurrent(entity1.id), 
			IndexUtils.getCurrent(i.id), false, 1, 1)
		
		String sqlScript = cc.convertToSql(Database.MY_SQL)
		System.out.println(sqlScript)
		assertTrue(sqlScript.contains(entity1.name))
		assertTrue(sqlScript.contains(entity2.name))
		assertTrue(sqlScript.contains(entity3.name))
		
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
	
	@Test
	public void testMySqlConverter2() throws Exception {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		ConverterCommander cc = new ConverterCommander()
		
		Entity entity1 = new Entity()
		entity1.name = "table1"
		EntityUtils.createEntity(entity1)
		Attribute attr1 = new Attribute()
		attr1.attributeType = AttributeTypes.DATE
		attr1.activeAttributeType = AttributeTypes.DATE.name
		attr1.name = "attr1"
		attr1.definition = "тестовый атрибут"
		attr1.id = System.currentTimeMillis()
		attr1.constraints.nullable = false
		attr1.constraints.primary = true
		AttributeUtils.createAttribute(attr1, entity1.id)
		Attribute attr2 = new Attribute()
		attr2.attributeType = AttributeTypes.INTEGER
		attr2.activeAttributeType = AttributeTypes.INTEGER.name
		attr2.name = "attr2"
		attr2.definition = "тестовый атрибут"
		attr2.id = System.currentTimeMillis()
		attr2.constraints.nullable = false
		attr2.constraints.primary = true
		AttributeUtils.createAttribute(attr2, entity1.id)
		List<Attribute> attrs1 = new ArrayList<>()
		attrs1.add(attr1)
		attrs1.add(attr2)
		entity1.attributes = attrs1
		
		attr2 = AttributeUtils.getCurrent(attr2.id)
		attr2.name = "attr2_renamed"
		AttributeUtils.updateAttribute(attr2)
		
		String sqlScript = cc.convertToSql(Database.MY_SQL)
		assertTrue(sqlScript.contains(attr2.name))
		System.out.println("BEFORE")
		System.out.println("--------------")
		System.out.println(sqlScript)
		System.out.println("--------------")
		
		UserDataUtils.undo();
		
		sqlScript = cc.convertToSql(Database.MY_SQL)
		assertFalse(sqlScript.contains(attr2.name))
		assertTrue(sqlScript.contains("attr2"))
		System.out.println("AFTER")
		System.out.println("--------------")
        System.out.println(sqlScript);
		System.out.println("--------------")
	}	
	
	@Test
	public void testMySqlConverter3() throws Exception {
		UserDataUtils adUtils = new UserDataUtils()
		String name = "myNewDb" + System.currentTimeMillis()
		adUtils.createNewFile(name)
		
		ConverterCommander cc = new ConverterCommander()
		
		Entity entity1 = new Entity()
		entity1.name = "Faculties"
		EntityUtils.createEntity(entity1)
		Attribute attr1_1 = new Attribute()
		attr1_1.attributeType = AttributeTypes.INTEGER
		attr1_1.activeAttributeType = AttributeTypes.INTEGER.name
		attr1_1.name = "faculty_code"
		attr1_1.definition = "код факультета"
		attr1_1.id = System.currentTimeMillis()
		attr1_1.constraints.nullable = false
		attr1_1.constraints.primary = true
		AttributeUtils.createAttribute(attr1_1, entity1.id)
		Attribute attr1_2 = new Attribute()
		attr1_2.attributeType = AttributeTypes.VARCHAR
		attr1_2.activeAttributeType = "VARCHAR(256)"
		attr1_2.name = "fac_name"
		attr1_2.definition = "название факультета"
		attr1_2.id = System.currentTimeMillis()
		attr1_2.constraints.nullable = false
		attr1_2.constraints.primary = false
		AttributeUtils.createAttribute(attr1_2, entity1.id)
		Attribute attr1_3 = new Attribute()
		attr1_3.attributeType = AttributeTypes.VARCHAR
		attr1_3.activeAttributeType = "VARCHAR(256)"
		attr1_3.name = "profil_exam"
		attr1_3.definition = "экзамен по профильному предмету"
		attr1_3.id = System.currentTimeMillis()
		attr1_3.constraints.nullable = false
		attr1_3.constraints.primary = false
		AttributeUtils.createAttribute(attr1_3, entity1.id)
		List<Attribute> attrs1 = new ArrayList<>()
		attrs1.add(attr1_1)
		attrs1.add(attr1_2)
		attrs1.add(attr1_3)
		entity1.attributes = attrs1

		Entity entity2 = new Entity()
		entity2.name = "Specialities"
		EntityUtils.createEntity(entity2)
		Attribute attr2_1 = new Attribute()
		attr2_1.attributeType = AttributeTypes.INTEGER
		attr2_1.activeAttributeType = AttributeTypes.INTEGER.name
		attr2_1.name = "speciality_code"
		attr2_1.definition = "код специальности"
		attr2_1.id = System.currentTimeMillis()
		attr2_1.constraints.primary = true
		AttributeUtils.createAttribute(attr2_1, entity2.id)
		Attribute attr2_2 = new Attribute()
		attr2_2.attributeType = AttributeTypes.VARCHAR
		attr2_2.activeAttributeType = "VARCHAR(256)"
		attr2_2.name = "spec_name"
		attr2_2.definition = "название специальности"
		attr2_2.id = System.currentTimeMillis()
		attr2_2.constraints.primary = false
		attr2_2.constraints.nullable = false
		AttributeUtils.createAttribute(attr2_2, entity2.id)
		Attribute attr2_3 = new Attribute()
		attr2_3.attributeType = AttributeTypes.INTEGER
		attr2_3.activeAttributeType = AttributeTypes.INTEGER.name
		attr2_3.name = "number_nopay_places"
		attr2_3.definition = "количество бюджетных мест"
		attr2_3.id = System.currentTimeMillis()
		attr2_3.constraints.primary = false
		attr2_3.constraints.nullable = false
		AttributeUtils.createAttribute(attr2_3, entity2.id)
		Attribute attr2_4 = new Attribute()
		attr2_4.attributeType = AttributeTypes.INTEGER
		attr2_4.activeAttributeType = AttributeTypes.INTEGER.name
		attr2_4.name = "number_pay_places"
		attr2_4.definition = "количество платных мест"
		attr2_4.id = System.currentTimeMillis()
		attr2_4.constraints.primary = false
		attr2_4.constraints.nullable = false
		AttributeUtils.createAttribute(attr2_4, entity2.id)
		List<Attribute> attrs2 = new ArrayList<>()
		attrs2.add(attr2_1)
		attrs2.add(attr2_2)
		attrs2.add(attr2_3)
		attrs2.add(attr2_4)
		entity2.attributes = attrs2
		
		Index i1_2 = new Index()
		i1_2.name = "faculties_to_specialities"
		i1_2.addAttribute(attr1_1)
		IndexUtils.createIndex(i1_2, entity1.id)
		
		Relationship r1_2 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity1.id),
			EntityUtils.getCurrent(entity2.id),
			IndexUtils.getCurrent(i1_2.id), false, 2, 1)
		
		Entity entity3 = new Entity()
		entity3.name = "Ankets"
		EntityUtils.createEntity(entity3)
		Attribute attr3_1 = new Attribute()
		attr3_1.attributeType = AttributeTypes.INTEGER
		attr3_1.activeAttributeType = AttributeTypes.INTEGER.name
		attr3_1.name = "regist_numer"
		attr3_1.definition = "номер анкеты абитуриента"
		attr3_1.id = System.currentTimeMillis()
		attr3_1.constraints.primary = true
		AttributeUtils.createAttribute(attr3_1, entity3.id)
		Attribute attr3_2 = new Attribute()
		attr3_2.attributeType = AttributeTypes.VARCHAR
		attr3_2.activeAttributeType = "VARCHAR(256)"
		attr3_2.name = "surname"
		attr3_2.definition = "фамилия абитуриента"
		attr3_2.id = System.currentTimeMillis()
		attr3_2.constraints.nullable = false
		attr3_2.constraints.primary = false
		AttributeUtils.createAttribute(attr3_2, entity3.id)
		Attribute attr3_3 = new Attribute()
		attr3_3.attributeType = AttributeTypes.VARCHAR
		attr3_3.activeAttributeType = "VARCHAR(256)"
		attr3_3.name = "name"
		attr3_3.definition = "имя абитуриента"
		attr3_3.id = System.currentTimeMillis()
		attr3_3.constraints.nullable = false
		attr3_3.constraints.primary = false
		AttributeUtils.createAttribute(attr3_3, entity3.id)
		Attribute attr3_4 = new Attribute()
		attr3_4.attributeType = AttributeTypes.VARCHAR
		attr3_4.activeAttributeType = "VARCHAR(256)"
		attr3_4.name = "second_name"
		attr3_4.definition = "отчество абитуриента"
		attr3_4.id = System.currentTimeMillis()
		attr3_4.constraints.nullable = false
		attr3_4.constraints.primary = false
		AttributeUtils.createAttribute(attr3_4, entity3.id)
		Attribute attr3_5 = new Attribute()
		attr3_5.attributeType = AttributeTypes.INTEGER
		attr3_5.activeAttributeType = AttributeTypes.INTEGER.name
		attr3_5.name = "pasport_serie"
		attr3_5.definition = "серия паспорта"
		attr3_5.id = System.currentTimeMillis()
		attr3_5.constraints.nullable = false
		attr3_5.constraints.primary = false
		AttributeUtils.createAttribute(attr3_5, entity3.id)
		Attribute attr3_6 = new Attribute()
		attr3_6.attributeType = AttributeTypes.INTEGER
		attr3_6.activeAttributeType = AttributeTypes.INTEGER.name
		attr3_6.name = "pasport_number"
		attr3_6.definition = "номер паспорта"
		attr3_6.id = System.currentTimeMillis()
		attr3_6.constraints.nullable = false
		attr3_6.constraints.primary = false
		AttributeUtils.createAttribute(attr3_6, entity3.id)
		Attribute attr3_7 = new Attribute()
		attr3_7.attributeType = AttributeTypes.VARCHAR
		attr3_7.activeAttributeType = "VARCHAR(256)"
		attr3_7.name = "EGE_number"
		attr3_7.definition = "номер ЕГЭ"
		attr3_7.id = System.currentTimeMillis()
		attr3_7.constraints.nullable = false
		attr3_7.constraints.primary = false
		AttributeUtils.createAttribute(attr3_7, entity3.id)
		Attribute attr3_8 = new Attribute()
		attr3_8.attributeType = AttributeTypes.TIMESTAMP
		attr3_8.activeAttributeType = AttributeTypes.TIMESTAMP.name
		attr3_8.name = "birthday"
		attr3_8.definition = "день рождения абитуриента"
		attr3_8.id = System.currentTimeMillis()
		attr3_8.constraints.nullable = false
		attr3_8.constraints.primary = false
		AttributeUtils.createAttribute(attr3_8, entity3.id)
		Attribute attr3_9 = new Attribute()
		attr3_9.attributeType = AttributeTypes.VARCHAR
		attr3_9.activeAttributeType = "VARCHAR(256)"
		attr3_9.name = "birthplace"
		attr3_9.definition = "место рождения абитуриента"
		attr3_9.id = System.currentTimeMillis()
		attr3_9.constraints.nullable = false
		attr3_9.constraints.primary = false
		AttributeUtils.createAttribute(attr3_9, entity3.id)
		Attribute attr3_10 = new Attribute()
		attr3_10.attributeType = AttributeTypes.VARCHAR
		attr3_10.activeAttributeType = "VARCHAR(256)"
		attr3_10.name = "registr_adress"
		attr3_10.definition = "место прописки абитуриента"
		attr3_10.id = System.currentTimeMillis()
		attr3_10.constraints.nullable = false
		attr3_10.constraints.primary = false
		AttributeUtils.createAttribute(attr3_10, entity3.id)
		Attribute attr3_11 = new Attribute()
		attr3_11.attributeType = AttributeTypes.INTEGER
		attr3_11.activeAttributeType = AttributeTypes.INTEGER.name
		attr3_11.name = "dormitory"
		attr3_11.definition = "необходимость общежития"
		attr3_11.id = System.currentTimeMillis()
		attr3_11.constraints.nullable = false
		attr3_11.constraints.primary = false
		AttributeUtils.createAttribute(attr3_11, entity3.id)
		List<Attribute> attrs3 = new ArrayList<>()
		attrs3.add(attr3_1)
		attrs3.add(attr3_2)
		attrs3.add(attr3_3)
		attrs3.add(attr3_4)
		attrs3.add(attr3_5)
		attrs3.add(attr3_6)
		attrs3.add(attr3_7)
		attrs3.add(attr3_8)
		attrs3.add(attr3_9)
		attrs3.add(attr3_10)
		attrs3.add(attr3_11)
		entity3.attributes = attrs3
		
		Entity entity4 = new Entity()
		entity4.name = "Abiturients"
		EntityUtils.createEntity(entity4)
		
		Index i2_4 = new Index()
		i2_4.name = "specialities_to_abiturients"
		i2_4.addAttribute(attr2_1)
		IndexUtils.createIndex(i2_4, entity2.id)
		Relationship r2_4 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity2.id),
			EntityUtils.getCurrent(entity4.id),
			IndexUtils.getCurrent(i2_4.id), true, 1, 1)
		
		Index i3_4 = new Index()
		i3_4.name = "ankets_to_abiturients"
		i3_4.addAttribute(attr3_1)
		IndexUtils.createIndex(i3_4, entity3.id)
		Relationship r3_4 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity3.id),
			EntityUtils.getCurrent(entity4.id),
			IndexUtils.getCurrent(i3_4.id), true, 1, 1)
		
		Entity entity5 = new Entity()
		entity5.name = "Olympiade_marks"
		EntityUtils.createEntity(entity5)
		Attribute attr5_1 = new Attribute()
		attr5_1.attributeType = AttributeTypes.INTEGER
		attr5_1.activeAttributeType = AttributeTypes.INTEGER.name
		attr5_1.name = "mark"
		attr5_1.definition = "оценка по олимпиаде"
		attr5_1.id = System.currentTimeMillis()
		attr5_1.constraints.primary = false
		attr5_1.constraints.nullable = true
		AttributeUtils.createAttribute(attr5_1, entity5.id)
		List<Attribute> attrs5 = new ArrayList<>()
		attrs5.add(attr5_1)
		entity5.attributes = attrs5
		
		Index i3_5 = new Index()
		i3_5.name = "ankets_to_olympiade_marks"
		i3_5.addAttribute(attr3_1)
		IndexUtils.createIndex(i3_5, entity3.id)
		Relationship r3_5 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity3.id),
			EntityUtils.getCurrent(entity5.id),
			IndexUtils.getCurrent(i3_5.id), true, 1, 1)
		
		Entity entity6 = new Entity()
		entity6.name = "EGE_marks"
		EntityUtils.createEntity(entity6)
		Attribute attr6_1 = new Attribute()
		attr6_1.attributeType = AttributeTypes.INTEGER
		attr6_1.activeAttributeType = AttributeTypes.INTEGER.name
		attr6_1.name = "math_mark"
		attr6_1.definition = "балл за ЕГЭ по математике"
		attr6_1.id = System.currentTimeMillis()
		attr6_1.constraints.primary = false
		attr6_1.constraints.nullable = false
		AttributeUtils.createAttribute(attr6_1, entity6.id)
		Attribute attr6_2 = new Attribute()
		attr6_2.attributeType = AttributeTypes.INTEGER
		attr6_2.activeAttributeType = AttributeTypes.INTEGER.name
		attr6_2.name = "rus_lang_mark"
		attr6_2.definition = "балл за ЕГЭ по русскому языку"
		attr6_2.id = System.currentTimeMillis()
		attr6_2.constraints.primary = false
		attr6_2.constraints.nullable = false
		AttributeUtils.createAttribute(attr6_2, entity6.id)
		Attribute attr6_3 = new Attribute()
		attr6_3.attributeType = AttributeTypes.INTEGER
		attr6_3.activeAttributeType = AttributeTypes.INTEGER.name
		attr6_3.name = "profil_subj_mark"
		attr6_3.definition = "балл за ЕГЭ по профильному предмету"
		attr6_3.id = System.currentTimeMillis()
		attr6_3.constraints.primary = false
		attr6_3.constraints.nullable = false
		AttributeUtils.createAttribute(attr6_3, entity6.id)
		List<Attribute> attrs6 = new ArrayList<>()
		attrs6.add(attr6_1)
		attrs6.add(attr6_2)
		attrs6.add(attr6_3)
		entity6.attributes = attrs6
		
		Index i3_6 = new Index()
		i3_6.name = "ankets_to_EGE_marks"
		i3_6.addAttribute(attr3_1)
		IndexUtils.createIndex(i3_6, entity3.id)
		Relationship r3_6 = RelationshipUtils.createRelationship(
			EntityUtils.getCurrent(entity3.id),
			EntityUtils.getCurrent(entity6.id),
			IndexUtils.getCurrent(i3_6.id), true, 1, 1)
		
		String sqlScript = cc.convertToSql(Database.MY_SQL)
		System.out.println(sqlScript)
		assertTrue(sqlScript.contains(entity1.name))
		assertTrue(sqlScript.contains(entity2.name))
		assertTrue(sqlScript.contains(entity3.name))
		assertTrue(sqlScript.contains(entity4.name))
		assertTrue(sqlScript.contains(entity5.name))
		assertTrue(sqlScript.contains(entity6.name))
	}
}
