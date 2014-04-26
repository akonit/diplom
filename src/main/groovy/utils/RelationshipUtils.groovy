package utils

import org.apache.log4j.Logger;

import lowmodel.attribute.BasicAttribute
import lowmodel.entity.BasicEntity
import lowmodel.relationship.BasicRelationship
import lowmodel.attribute.KeyGroup;

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
	public static BasicRelationship assignRelationship(BasicEntity fromEntity, 
		BasicEntity toEntity, BasicAttribute fromAttr) {
		BasicRelationship relationship = new BasicRelationship();
		
		BasicAttribute toAttr = new BasicAttribute();
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
	public static void dropRelationship(BasicEntity entity, BasicAttribute attr) {
			entity.attributes.remove(attr);
			//валидация? 
			log.info("dropRelationship - remove {" + attr.definition +"} from {" +
				entity.name + "}");
	}
}
