package utils

import org.apache.log4j.Logger;

import attribute.Attribute;
import relationship.Relationship;
import entity.Entity;
import entity.Index

final class RelationshipUtils {
	
	static Logger log = Logger.getLogger(RelationshipUtils.class.getName());

	private RelationshipUtils() {
		
	}
	
	/**
	 * Создание связей между таблицами. Вызывается из контроллера
	 * @param fromEntity родительская таблица.
	 * @param toEntity дочерняя таблица.
	 * @param index группа родительских атрибутов.
	 * @return созданная связь.
	 */
	//добавить валидацию на создание FK в зависимой таблице
	public static Relationship createRelationship(Entity fromEntity, 
			Entity toEntity, Index index, boolean identifying) {
		Relationship relationship = new Relationship()
		relationship.indexId = index.id
        relationship.fromEntityId = fromEntity.id
		relationship.toEntityId = toEntity.id
		relationship.cardinality.identifying = identifying

		List newAttrs = new ArrayList<>()
		long time = System.currentTimeMillis()
		for(Attribute fromAttr : index.attributes) {
			Attribute toAttr = new Attribute()
			toAttr.attributeType = fromAttr.attributeType
			toAttr.name = fromAttr.name
			toAttr.definition = fromAttr.definition
			toAttr.activeAttributeType = fromAttr.activeAttributeType
			toAttr.getConstraints().setForeign(true)
			if (identifying) {
				toAttr.getConstraints().setPrimary(true)
				relationship.cardinality.identifying = true
			}
			toAttr.id = System.currentTimeMillis()
			relationship.getToAttr().add(toAttr)
			newAttrs.add(toAttr)
			AttributeUtils.createAttribute(toAttr, toEntity.id, time)

			if(toEntity.attributes == null) {
				toEntity.attributes = new ArrayList<>()
			}
			toEntity.attributes.add(toAttr);//здесь была валидация >.<

			log.info("createRelationship - from {" + fromEntity.name + "} to {" + toEntity.name +
					"} attribute {" + toAttr.name + "} by {" + index.name + "}")
		}
	    saveRelationship(relationship, index.id, time, newAttrs)
		
		return relationship;//добавить результат на модель в список связей
	}
	
	private static void saveRelationship(Relationship rel, long indexId, long time, List<Attribute> newAttrs) {
		try {
			rel.id = System.currentTimeMillis()
			int identify = rel.cardinality.identifying ? 1 : 0
			UserDataUtils.connection.execute("insert into app_relation "
					+ " (id, time, status, table_from_id, table_to_id, index_id, identify, is_deleted) "
					+ " values(?, ?, ?, ?, ?, ?, ?, ?)",
					[
						rel.id,
						time,
						Status.DONE.getName(),
						rel.fromEntityId,
						rel.toEntityId,
						indexId,
						identify,
						0
					])
			
			if (newAttrs != null & !newAttrs.isEmpty()) {
				for (Attribute attr : newAttrs) {
					UserDataUtils.connection.execute("insert into relation_to_attr"
						+ " (id, status, time, relation_id, attribute_id, is_deleted) "
						+ " values(?, ?, ?, ?, ?, ?)", [
							System.currentTimeMillis(),
							Status.DONE.getName(),
							time,
							rel.id, 
							attr.id,
							0])
				}
			}
			log.info("createRelationship from " 
				+ rel.fromEntityId + "to " + rel.toEntityId + " -> done, " + rel.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createRelationship from " 
				+ rel.fromEntityId + "to " + rel.toEntityId + " -> failed", e)
			throw new RuntimeException("failed to create relation")
		}
	}
	
	/**
	 * Удалит запись из файла. Заодно удалит атрибуты, порожденные при создании.
	 */
	public static void deleteRelationship(long relationId) {
		deleteRelationship(relationId, System.currentTimeMillis())
	}
	
	public static void deleteRelationship(long relationId, long time) {
		try {
			Relationship current = getCurrent(relationId)
			if (current == null || current.isDeleted) {
				return null
			}
			UserDataUtils.connection.execute("insert into app_relation "
					+ " (id, time, status, table_from_id, table_to_id, index_id, identify, is_deleted) "
					+ " values(?, ?, ?, ?, ?, ?, ?, ?)",
					[
						current.id,
						time,
						Status.DONE.getName(),
						current.fromEntityId,
						current.toEntityId,
						current.indexId,
						current.cardinality.identifying ? 1 : 0,
						1
					])
			UserDataUtils.connection.eachRow("select * from app_attribute where id in"
				+ " (select attribute_id from relation_to_attr where relation_id = ?) "
				+ " and is_deleted = 0",
					[relationId]) { row ->
						AttributeUtils.deleteAttribute(row.id, time)
					}
			log.info("deleteRelationship [" + relationId + "] -> done")
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("deleteRelationship [" + relationId + "] -> failed", e)
			throw new RuntimeException("failed to delete relation")
		}
	}
	
	public static Relationship getCurrent(long id) {
		def row = UserDataUtils.connection.firstRow("select * from app_relation where id = ? and status = ? order by time desc",
				[
					id,
					Status.DONE.getName()
				])
		if (row == null) {
			return row
		}
		
		Relationship current = new Relationship()
		current.id = row.id
		current.time = row.time
		current.fromEntityId = row.table_from_id
		current.toEntityId = row.table_to_id
		current.indexId = row.index_id
		current.cardinality = new Relationship.Cardinality()
		current.cardinality.identifying = row.identify == 0 ? false : true
		current.isDeleted = row.is_deleted == 0 ? false : true
		
		return current
	}
}
