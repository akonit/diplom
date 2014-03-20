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
}
