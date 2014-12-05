package attribute

import lowmodel.attribute.type.*

import org.apache.log4j.Logger
import utils.Status

import entity.Index

/**
 * Базовое поле таблицы - pk, fk, просто атрибут.
 * keyGroups - способ назначения типа ключа в ервине!
 * Предполагаемое взаимодействие с view - по выбранному названию атрибута назначается
 * конкретный тип.
 */
//добавить ссылку на родительскую таблицу? для удобства обработки связей между таблицами 
//по атрибутам. или хранить их в связи рядом
//добавить билдер
//добавить имя атрибута - его в лог и на отображение в таблице. чтобы
//было что то вроде "String (FK)"
class Attribute implements Serializable {
	
	static Logger log = Logger.getLogger(Attribute.class.getName())
	
	private long id
	
	/**
	 * Название атрибута.
	 */
	private String name
	
	/**
	 * Тип атрибута в бд.
	 */
	private AttributeTypes attributeType
	
	private long time
	
	private Status status
	
	private long entityId
	
	private long entityTime
	
	/**
	 * Кастомный тип атрибута. varchar(20) например. на форме предполагается отображать
	 * и изменять именно его
	 */
	private String activeAttributeType
	
	/**
	 * описание атрибута
	 */
	private String definition
	
	/**
	 * Заметки
	 */
	private String commentary
	
	private List<Index> indexes
	
	/**
	 * Ограничения, накладываемые на атрибуты.
	 */
	public static class Constraints implements Serializable {
		private boolean nullable = true
		
		private boolean unique = false
		
		private boolean primary = false
		
		//скорее всего, лишнее, если только декоративные функции
		private boolean foreign = false
	}
	
	private Constraints constraints
	
	private boolean isDeleted
	
	public Attribute() {
		constraints = new Constraints()
		indexes = new ArrayList<>()
	}
	
	//вынести в контроллер?
	/**
	 * Установка пользовательского типа атрибута
	 * @param newTypeName новый тип атрибута
	 * @return успех/неуспех операции
	 */
	public boolean changeSubAttrType(String newTypeName) {
		if (attributeType.modifyable) {
			activeAttributeType = newTypeName
		    log.debug("changeAttrType {" + newTypeName + "} - ok")
			return true
		} else {
		    log.error("changeAttrType {" + newTypeName + "} - failed, not modifyable type")
			return false
		}
	}
	
	public void addToIndex(Index index) {
		if (!indexes.contains(index)) {
			indexes.add(index)
			index.attributes.add(this)
		}
	}
	
	public void removeFromIndex(Index index) {
		indexes.remove(index)
		index.attributes.remove(this)
	}
}
