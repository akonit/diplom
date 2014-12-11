package sql.converter

import attribute.Attribute
import entity.Entity
import entity.Index
import groovy.sql.Sql
import utils.*
import relationship.Relationship

abstract class SqlConverter {

	/**
	 * Преобразование текущей диаграммы к SQL-коду
	 * @return
	 */
	public abstract String convertToSql();
	
	/**
	 * Получение списка всех актуальных неудаленных таблиц.
	 * @param connection
	 * @return
	 */
	public List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_table") {
			Entity e = EntityUtils.getCurrent(it.id)
			if (!e.isDeleted) {
				entities.add(e)
			}
		}
		return entities
	}
	
	public List<Relationship> getRelations() {
		List<Relationship> relations = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_relation") {
			Relationship r = RelationshipUtils.getCurrent(it.id)
			if (!r.isDeleted) {
				relations.add(r)
			}
		}
		return relations
	}
	
	public List<Attribute> getAttributes(long entityId) {
		List<Attribute> attributes = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_attribute "
			+ "where table_id = ?", [entityId]) {
			Attribute a = AttributeUtils.getCurrent(it.id)
			if (!a.isDeleted) {
				attributes.add(a)
			}
		}
		return attributes
	}
	
	public List<Index> getIndexes(long entityId) {
		List<Index> indexes = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_index "
			+ "where table_id = ?", [entityId]) {
			Index i = IndexUtils.getCurrent(it.id)
			if (!i.isDeleted) {
				indexes.add(i)
			}
		}
		return indexes
	}
	
	public List<Attribute> getIndexAttributes(long indexId) {
		List<Attribute> attributes = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(attribute_id) from app_index_attribute "
			+ "where index_id = ?", [indexId]) {
			Attribute a = AttributeUtils.getCurrent(it.attribute_id)
			if (!a.isDeleted) {
				attributes.add(a)
			}
		}
		return attributes
	}
	
	public List<Attribute> getRelationAttributes(long relationId) {
		List<Attribute> attributes = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(attribute_id) from relation_to_attr "
			+ "where relation_id = ?", [relationId]) {
			Attribute a = AttributeUtils.getCurrent(it.attribute_id)
			if (!a.isDeleted) {
				attributes.add(a)
			}
		}
		return attributes
	}
}
