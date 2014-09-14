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
	 * Запись в файл.
	 * @param newAttrs - атрибуты, созданные после добавления связи.
	 */
	public static void createRelationship(Relationship rel, List<Attribute> newAttrs) {
		try {
			rel.id = System.currentTimeMillis()
			int identify = rel.cardinality.identifying ? 1 : 0
			UserDataUtils.connection.execute("insert into app_relation(id, table_from_id, table_to_id, identify) values(?, ?, ?, ?)",
				[rel.id, rel.fromEntity.id, rel.toEntity.id, identify])
			
			if (newAttrs != null & !newAttrs.isEmpty()) {
				for (Attribute attr : newAttrs) {
					UserDataUtils.connection.execute("insert into relation_to_attr(relation_id, attribute_id) values(?, ?)",
						[rel.id, attr.id])
				}
			}
		} catch (Exception e) {
			UserDataUtils.connection.rollback()
			log.error("createRelationship from " + rel.fromEntity.name + "to " + rel.toEntity.name + " -> failed", e)
		}
	}
	
	//NB: вызывает запись в файл.
	/**
	 * Создание связей между таблицами. Вызывается из контроллера
	 * @param fromEntity родительская таблица.
	 * @param toEntity дочерняя таблица.
	 * @param index группа родительских атрибутов.
	 * @return созданная связь.
	 */
	//добавить валидацию на создание FK в зависимой таблице
	public static Relationship assignRelationship(Entity fromEntity, 
			Entity toEntity, Index index, boolean identifying) {
		Relationship relationship = new Relationship();
		relationship.setIndex(index);
        relationship.setFromEntity(fromEntity);
		relationship.setToEntity(toEntity);
		relationship.cardinality.identifying = identifying

		List newAttrs = new ArrayList<>()
		for(Attribute fromAttr : index.attributes) {
			Attribute toAttr = new Attribute();
			toAttr.attributeType = fromAttr.attributeType;
			toAttr.name = fromAttr.name
			toAttr.definition = fromAttr.definition;
			toAttr.activeAttributeType = fromAttr.activeAttributeType
			toAttr.getConstraints().setForeign(true);
			if (identifying) {
				toAttr.getConstraints().setPrimary(true);
				relationship.cardinality.identifying = true;
			}
			toAttr.id = System.currentTimeMillis();
			relationship.getToAttr().add(toAttr);
			newAttrs.add(toAttr)
			AttributeUtils.createAttribute(toAttr, toEntity.id)

			if(toEntity.attributes == null) {
				toEntity.attributes = new ArrayList<>();
			}
			toEntity.attributes.add(toAttr);//здесь была валидация >.<

			log.info("assignRelationship - from {" + fromEntity.name + "} to {" + toEntity.name +
					"} attribute {" + toAttr.name + "} by {" + index.name + "}");
		}
	    createRelationship(relationship, newAttrs)
		
		return relationship;//добавить результат на модель в список связей
	}
		
	/**
	 * Удаление связи между таблицами. Метод вызывается из контроллера.
	 * Предполагается, что после вызова метода связь будет удалена из 
	 * коллекции связей на модели.
	 * @param entity 
	 * @param relationship
	 */
	public static void dropRelationship(Entity entity, Relationship relation) {
			entity.attributes.removeAll(relation.getToAttr());
			//валидация? 
			log.info("dropRelationship - remove {" + relation.index.name +"} from {" +
				entity.name + "}");
	}
}
