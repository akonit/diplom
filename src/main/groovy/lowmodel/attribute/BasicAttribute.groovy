package lowmodel.attribute

import lowmodel.attribute.type.*;

import org.apache.log4j.Logger;

import interfaces.LowModelApi;

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
class BasicAttribute implements LowModelApi, Serializable {
	
	static Logger log = Logger.getLogger(BasicAttribute.class.getName());
	
	//может ли один атрибут иметь несколько групп?
	
	private KeyGroup keyGroup;//список?

	private String id;
	
	/**
	 * Тип атрибута в бд.
	 */
	private AttributeTypes attributeType;
	
	/**
	 * Кастомный тип атрибута. varchar(20) например. на форме предполагается отображать
	 * и изменять именно его
	 */
	private String activeAttributeType;
	
	/**
	 * описание атрибута
	 */
	private String definition;
	
	/**
	 * Заметки
	 */
	private String note;
	
	//вынести в контроллер?
	/**
	 * Установка пользовательского типа атрибута
	 * @param newSubTypeName новый тип атрибута
	 * @return успех/неуспех операции
	 */
	public boolean changeSubAttrType(String newTypeName) {
		if (attributeType.modifyable) {
			activeAttributeType = newTypeName;
		    log.debug("changeAttrType {" + newTypeName + "} - ok");
			return true;
		} else {
		    log.error("changeAttrType {" + newTypeName + "} - failed, not modifyable type");
			return false;
		}
	}
	
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public AttributeTypes getAttributeType() {
		return attributeType;
	}

	public void setAttributeType(AttributeTypes attributeType) {
		this.attributeType = attributeType;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public KeyGroup getKeyGroup() {
		return keyGroup;
	}

	public void setKeyGroup(KeyGroup keyGroup) {
		this.keyGroup = keyGroup;
	}
}
