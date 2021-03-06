package utils

import java.util.List;

import org.apache.log4j.Logger

import attribute.AttributeTypes
import attribute.Attribute
import entity.Entity
import lowmodel.attribute.type.*

final class AttributeUtils {
	
	static Logger log = Logger.getLogger(AttributeUtils.class.getName())
	
	private AttributeUtils() {
		
	}
	
	/**
	 * Создание атрибута таблицы.
	 * @param entityId - идентификатор таблицы, которой принадлежит атрибут.
	 */
	public static void createAttribute(Attribute attr, long entityId) {
		createAttribute(attr, entityId, System.currentTimeMillis())
	}
	
	public static void createAttribute(Attribute attr, long entityId, long time) {
		try {
			Entity e = EntityUtils.getCurrent(entityId)
			attr.id = System.currentTimeMillis()
			UserDataUtils.connection.execute("insert into app_attribute"
					+ " (id, time, status, name, type, definition, commentary, is_primary, is_nullable,"
					+ " is_unique, table_id, table_time, is_deleted)"
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						attr.id,
						time,
						Status.DONE.getName(),
						attr.name,
						attr.activeAttributeType,
						attr.definition,
						attr.commentary,
						attr.constraints.primary ? 1 : 0,
						attr.constraints.nullable ? 1 : 0,
						attr.constraints.unique ? 1 : 0,
						entityId,
						e.time,
						0
					])
			
			UserDataUtils.cleanUpUndone()
			log.info("createAttribute ["+ attr.name + "] -> done, " + attr.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createAttribute [" + attr.name + "] -> failed", e)
			throw new RuntimeException("failed to create attribute")
		}
	}
	
	/**
	 * Обновление атрибута таблицы.
	 * @param entityId - идентификатор таблицы, которой принадлежит атрибут.
	 */
	public static void updateAttribute(Attribute attr) {
		try {
			UserDataUtils.connection.execute("insert into app_attribute"
					+ " (id, time, status, name, type, definition, commentary, is_primary, is_nullable,"
					+ " is_unique, table_id, table_time, is_deleted)"
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						attr.id,
						System.currentTimeMillis(),
						attr.status.getName(),
						attr.name,
						attr.activeAttributeType,
						attr.definition,
						attr.commentary,
						attr.constraints.primary ? 1 : 0,
						attr.constraints.nullable ? 1 : 0,
						attr.constraints.unique ? 1 : 0,
						attr.entityId,
						attr.entityTime,
						0
					])
			
			UserDataUtils.cleanUpUndone()
			log.info("createAttribute ["+ attr.name + "] -> done, " + attr.id)
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createAttribute [" + attr.name + "] -> failed", e)
			throw new RuntimeException("failed to create attribute")
		}
	}
	
	/**
	 * Удалит атрибут
	 */
	public static void deleteAttribute(long attrId) {
		deleteAttribute(attrId, System.currentTimeMillis());
	}
	
	public static void deleteAttribute(long attrId, long time) {
		try {
			Attribute current = getCurrent(attrId)
			if (current == null || current.isDeleted) {
				return null
			}

			UserDataUtils.connection.execute("insert into app_attribute"
					+ " (id, time, status, name, type, definition, commentary, is_primary, is_nullable,"
					+ " is_unique, table_id, table_time, is_deleted)"
					+ " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
					[
						current.id,
						time,
						Status.DONE.getName(),
						current.name,
						current.activeAttributeType,
						current.definition,
						current.commentary,
						current.constraints.primary ? 1 : 0,
						current.constraints.nullable ? 1 : 0,
						current.constraints.unique ? 1 : 0,
						current.entityId,
						current.entityTime,
						1
					])
			UserDataUtils.connection.eachRow("select distinct rta.id as id "
					+ " from relation_to_attr rta, app_attribute at where "
					+ " rta.attribute_id = at.id and at.id = ?", [current.id]) {
						def row = UserDataUtils.connection.firstRow("select relation_id, is_deleted, "
							    + "attribute_time, relation_time from relation_to_attr "
								+ " where id = ? and status = ? order by time desc",
								[
									it.id,
									Status.DONE.getName()
								])
						if (row != null && row.is_deleted == 0) {
							UserDataUtils.connection.execute("insert into relation_to_attr "
									+ " (id, status, time, attribute_id, relation_id, is_deleted, "
									+ " attribute_time, relation_time) values "
									+ " (?, ?, ?, ?, ?, ?, ?, ?)", [
										it.id,
										Status.DONE.getName(),
										time,
										current.id,
										row.relation_id,
										1,
                                        time,
										row.relation_time
									])
							RelationshipUtils.deleteRelationship(row.relation_id, time)
						}
					}
			UserDataUtils.connection.eachRow("select distinct aia.id as id  "
					+ " from app_index_attribute aia, app_attribute at where "
					+ " aia.attribute_id = at.id and at.id = ?", [current.id]) {
						def row = UserDataUtils.connection.firstRow("select index_id, attribute_time, index_time"
							    + " from app_index_attribute "
								+ " where is_deleted = 0 and id = ? and status = ? order by time desc",
								[
									it.id,
									Status.DONE.getName()
								])
						if (row != null && row.is_deleted == 0) {
							UserDataUtils.connection.execute("insert into app_index_attribute "
									+ " (id, status, time, attribute_id, index_id, is_deleted, attribute_time, index_time) values "
									+ "(?, ?, ?, ?, ?, ?, ?, ?)", [
										it.id,
										Status.DONE,
										time,
										current.id,
										row.index_id,
										1,
										row.attribute_time,
										row.index_time
									])
						}
					}
			
			UserDataUtils.cleanUpUndone()
			log.info("deleteAttribute [" + attrId + "] -> done")
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("deleteAttribute [" + attrId + "] -> failed", e)
			throw new RuntimeException("failed to delete attribute")
		}
	}
	
	public static Attribute getCurrent(long id) {
		def row = UserDataUtils.connection.firstRow("select * from app_attribute where id = ? and status = ? order by time desc",
				[
					id,
					Status.DONE.getName()
				])
		if (row == null) {
			return row
		}
		
		Attribute current = new Attribute()
		current.id = row.id
		current.time = row.time
		current.entityId = row.table_id
		current.entityTime = row.table_time
		current.name = row.name
		current.activeAttributeType = row.type
		current.definition = row.definition
		current.commentary = row.commentary
		current.constraints = new Attribute.Constraints()
		current.constraints.primary = row.is_primary == 0 ? false : true
		current.constraints.nullable = row.is_nullable == 0 ? false : true
		current.constraints.unique = row.is_unique == 0 ? false : true
		current.isDeleted = row.is_deleted == 0 ? false : true
		current.status = Status.DONE
		
		return current
	}
	
	/**
	 * Назначение атрибуту типа с указанным названием. Метод предположительно для view
	 * @param attr атрибут
	 * @param subTypeName имя назначаемого типа
	 */
	 public static void signTypeToAttr(Attribute attr, String typeName) {
	     attr.activeAttributeType = typeName
	     attr.attributeType = AttributeTypes.getByName(typeName)
	 }
	
	public static List<Attribute> getAttributes(long entityId) {
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
	
	public static List<Attribute> getIndexAttributes(long indexId) {
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
	
	public static List<Attribute> getRelationAttributes(long relationId) {
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
