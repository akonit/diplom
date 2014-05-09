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
	public static Relationship assignRelationship(Entity fromEntity, 
			Entity toEntity, Index index, boolean identifying) {
		Relationship relationship = new Relationship();
		relationship.setIndex(index);
        relationship.setFromEntity(fromEntity);
		relationship.setToEntity(toEntity);

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
			toAttr.id = String.valueOf(System.currentTimeMillis());//заменить на UUID? или и так сойдет
			relationship.getToAttr().add(toAttr);

			if(toEntity.attributes == null) {
				toEntity.attributes = new ArrayList<>();
			}
			toEntity.attributes.add(toAttr);//здесь была валидация >.<

			log.info("assignRelationship - from {" + fromEntity.name + "} to {" + toEntity.name +
					"} attribute {" + toAttr.name + "} by {" + index.name + "}");
		}
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
