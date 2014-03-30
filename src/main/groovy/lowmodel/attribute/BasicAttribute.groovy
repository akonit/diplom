package lowmodel.attribute

import lowmodel.attribute.type.*;
import org.apache.log4j.Logger
import interfaces.LowModelApi
import interfaces.SubAttributeTypes

/**
 * Базовое поле таблицы - pk, fk, просто атрибут.
 * keyGroups - способ назначения типа ключа в ервине!
 * Предполагаемое взаимодействие с view - по выбранной группе
 * выдается список названий типов атрибута. по выбранному названию атрибуту назначается
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
	
	private AttributeType attrType;
	
	/**
	 * Тип атрибута в бд.
	 */
	private SubAttributeTypes subAttributeType;
	
	/**
	 * Кастомный тип атрибута. varchar(20) например. на форме предполагается отображать
	 * и изменять именно его
	 */
	private String activeSubAttributeType;
	
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
	public boolean changeSubAttrType(String newSubTypeName) {
		if (subAttributeType.modifyable) {
			activeSubAttributeType = newSubTypeName;
		    log.debug("changeSubAttrType {" + newSubTypeName + "} - ok");
			return true;
		} else {
		    log.error("changeSubAttrType {" + newSubTypeName + "} - fail, not modifyable type");
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

	public AttributeType getAttrType() {
		return attrType;
	}

	public void setAttrType(AttributeType attrType) {
		this.attrType = attrType;
	}

	public SubAttributeTypes getSubAttributeType() {
		return subAttributeType;
	}

	public void setSubAttributeType(SubAttributeTypes subAttributeType) {
		this.subAttributeType = subAttributeType;
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
