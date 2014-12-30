package utils

import java.util.List;

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
	public static Relationship createRelationship(Entity fromEntity, 
			Entity toEntity, Index index, boolean identifying,
			long cardinalityType, Long cardinalityNumber) {
		Relationship relationship = new Relationship()
		relationship.indexId = index.id
        relationship.fromEntityId = fromEntity.id
		relationship.toEntityId = toEntity.id
		relationship.indexTime = index.time
        relationship.fromEntityTime = fromEntity.time
		relationship.toEntityTime = toEntity.time
		relationship.cardinality.identifying = identifying
		relationship.cardinality.cardinalityType = Relationship.Cardinality.CardinalityType.getByNumber(cardinalityType)
		if (cardinalityNumber == null) {
			relationship.cardinality.cardinalityNumber = 0
		} else {
		    relationship.cardinality.cardinalityNumber = cardinalityNumber
		}

		List newAttrs = new ArrayList<>()
		long time = System.currentTimeMillis()
		def rows = UserDataUtils.connection.rows("select distinct(aa.id) as id from app_index ai, "
			+ " app_attribute aa, app_index_attribute aia where ai.id = ? "
			+ " and ai.time = ? and ai.id = aia.index_id and "
			+ " aa.id = aia.attribute_id and aia.status = ? and aia.is_deleted = ?", 
			[index.id, index.time, Status.DONE.name, 0])
		for (def row : rows) {
			Attribute fromAttr = AttributeUtils.getCurrent(row.id)
			if (fromAttr == null) {
				continue
			}
			if (!fromAttr.constraints.primary) {
				throw new RuntimeException("createRelationship [" +
					fromAttr.name + "] -> attempt to use non PK as FK");
			}
			Attribute toAttr = new Attribute()
			toAttr.attributeType = fromAttr.attributeType
			toAttr.name = fromAttr.name
			toAttr.definition = fromAttr.definition
			toAttr.activeAttributeType = fromAttr.activeAttributeType
			toAttr.constraints.foreign = true
			if (identifying) {
				toAttr.constraints.primary = true
				relationship.cardinality.identifying = true
			}
			toAttr.id = System.currentTimeMillis()
			toAttr.time = time
			toAttr.entityId = toEntity.id
			toAttr.entityTime = toEntity.time
			relationship.toAttr.add(toAttr)
			newAttrs.add(toAttr)
			AttributeUtils.createAttribute(toAttr, toEntity.id, time)

			if(toEntity.attributes == null) {
				toEntity.attributes = new ArrayList<>()
			}
			toEntity.attributes.add(toAttr);

			log.info("createRelationship - from {" + fromEntity.name + "} to {" + toEntity.name +
					"} attribute {" + toAttr.name + "} by {" + index.name + "}")
		}
	    saveRelationship(relationship, index.id, time, newAttrs)
		
		return relationship;
	}
	
	private static void saveRelationship(Relationship rel, long indexId, long time, List<Attribute> newAttrs) {
		try {
			rel.id = System.currentTimeMillis()
			int identify = rel.cardinality.identifying ? 1 : 0
			UserDataUtils.connection.execute("insert into app_relation "
					+ " (id, time, status, table_from_id, table_to_id, index_id, identify, is_deleted, "
					+ " table_to_time, table_from_time, index_time, "
                    + " cardinality_type, cardinality_number) "
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						rel.id,
						time,
						Status.DONE.getName(),
						rel.fromEntityId,
						rel.toEntityId,
						indexId,
						identify,
						0,
						rel.toEntityTime,
						rel.fromEntityTime,
						rel.indexTime,
						rel.cardinality.cardinalityType.number,
						rel.cardinality.cardinalityNumber
					])
			
			if (newAttrs != null & !newAttrs.isEmpty()) {
				for (Attribute attr : newAttrs) {
					UserDataUtils.connection.execute("insert into relation_to_attr"
						+ " (id, status, time, relation_id, attribute_id, is_deleted, attribute_time, relation_time) "
						+ " values(?, ?, ?, ?, ?, ?, ?, ?)", [
							System.currentTimeMillis(),
							Status.DONE.getName(),
							time,
							rel.id, 
							attr.id,
							0,
							attr.time,
							time])
					Thread.sleep(10)
				}
			}
			
			UserDataUtils.cleanUpUndone()
			log.info("createRelationship from " 
				+ rel.fromEntityId + " to " + rel.toEntityId + " -> done, " + rel.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createRelationship from " 
				+ rel.fromEntityId + " to " + rel.toEntityId + " -> failed", e)
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
					+ " (id, time, status, table_from_id, table_to_id, index_id, identify, is_deleted,"
					+ " table_to_time, table_from_time, index_time, "
					+ " cardinality_type, cardinality_number) "
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						current.id,
						time,
						Status.DONE.getName(),
						current.fromEntityId,
						current.toEntityId,
						current.indexId,
						current.cardinality.identifying ? 1 : 0,
						1,
						current.toEntityTime,
						current.fromEntityTime,
						current.indexTime,
						current.cardinality.cardinalityType.number,
						current.cardinality.cardinalityNumber
					])
			UserDataUtils.connection.eachRow("select * from app_attribute where id in"
				+ " (select attribute_id from relation_to_attr where relation_id = ?) "
				+ " and is_deleted = 0",
					[relationId]) { row ->
						AttributeUtils.deleteAttribute(row.id, time)
					}
			
			UserDataUtils.cleanUpUndone()
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
		current.fromEntityTime = row.table_from_time
		current.toEntityTime = row.table_to_time
		current.indexTime = row.index_time
		current.cardinality = new Relationship.Cardinality()
		current.cardinality.identifying = row.identify == 0 ? false : true
		current.cardinality.cardinalityNumber = row.cardinality_number
		current.cardinality.cardinalityType = Relationship.Cardinality.CardinalityType.getByNumber(row.cardinality_type)
		current.isDeleted = row.is_deleted == 0 ? false : true
		current.status = Status.DONE
		
		return current
	}
	
	public static List<Relationship> getRelations() {
		List<Relationship> relations = new ArrayList<>()
		UserDataUtils.connection.eachRow("select distinct(id) from app_relation") {
			Relationship r = RelationshipUtils.getCurrent(it.id)
			if (!r.isDeleted) {
				relations.add(r)
			}
		}
		return relations
	}
}
