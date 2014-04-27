package utils

import org.apache.log4j.Logger;

import attribute.Attribute;
import attribute.KeyGroup;
import relationship.Relationship;
import entity.Entity;

//заменить в логах definition атрибутов на name, когда оно появится
final class RelationshipUtils {
	
	static Logger log = Logger.getLogger(RelationshipUtils.class.getName());

	private RelationshipUtils() {
		
	}
	
	/**
	 * Создание связей между таблицами. Вызывается из контроллера
	 * @param fromEntity родиетльская таблица.
	 * @param toEntity дочерняя таблица.
	 * @param fromAttr родительский атрибут.
	 * @return созданная связь.
	 */
	//посмотреть, какие связи помимо FK бывают, расширить метод при необходимости
	//добавить валидацию на создание FK в зависимой таблице
	public static Relationship assignRelationship(Entity fromEntity, 
		Entity toEntity, Attribute fromAttr) {
		Relationship relationship = new Relationship();
		
		Attribute toAttr = new Attribute();
		toAttr.attributeType = fromAttr.attributeType;
		toAttr.definition = fromAttr.definition;
		toAttr.keyGroup = KeyGroup.FOREIGN_KEY;
		toAttr.id = String.valueOf(System.currentTimeMillis());//заменить на UUID? или и так сойдет
		
		relationship.setFromAttr(fromAttr);
		relationship.setFromEntity(fromEntity);
		relationship.setToEntity(toEntity);
		relationship.setToAttr(toAttr);
		
		if(toEntity.attributes == null) {
		    toEntity.attributes = new ArrayList<>();
		} 
		toEntity.attributes.add(toAttr);//здесь была валидация >.<
			
		log.info("assignRelationship - from {" + fromEntity.name + "} to {" + toEntity.name +
			"} by {" + toAttr.definition + "}");
		return relationship;//добавить результат на модель в список связей
	}
		
	/**
	 * Удаление связи между таблицами. Метод вызывается из контроллера.
	 * Предполагается, что после вызова метода связь будет удалена из 
	 * коллекции связей на модели.
	 * @param toEntity
	 * @param fromAttr
	 */
	public static void dropRelationship(Entity entity, Attribute attr) {
			entity.attributes.remove(attr);
			//валидация? 
			log.info("dropRelationship - remove {" + attr.definition +"} from {" +
				entity.name + "}");
	}
}
