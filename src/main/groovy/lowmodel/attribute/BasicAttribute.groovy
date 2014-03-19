package lowmodel.attribute

import lowmodel.attribute.type.*;
import interfaces.LowModelApi
import interfaces.SubAttributeTypes

/**
 * Базовое поле таблицы - pk, fk, просто атрибут.
 * keyGroups - способ назначения типа ключа в ервине!
 */
class BasicAttribute implements LowModelApi, Serializable {
	
	//может ли один атрибут иметь несколько групп?
	
	private KeyGroup keyGroup;//список?

	private String id;
	
	private AttributeType attrType;
	
	/**
	 * Тип атрибута в бд.
	 */
	private String subAttributeType;
	
	//описание атрибута
	private String definition;
	
	
	private String note;
	
	/**
	 * Получение полной информации о конкретном типе атрибута
	 */
	public SubAttributeTypes getFullSubAttributeData() {
		switch (attrType) {
			case AttributeType.STRING:
				return StringTypes.getByName(subAttributeType);
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
