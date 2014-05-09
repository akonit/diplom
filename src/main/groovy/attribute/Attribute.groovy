package attribute

import lowmodel.attribute.type.*;

import org.apache.log4j.Logger;

import entity.Index
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
class Attribute implements LowModelApi, Serializable {
	
	static Logger log = Logger.getLogger(Attribute.class.getName());
	
	private String id;
	
	/**
	 * Название атрибута.
	 */
	private String name;
	
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
	
	private List<Index> indexes;
	
	/**
	 * Ограничения, накладываемые на атрибуты.
	 */
	public class Constraints implements Serializable {
		private boolean nullable = true;
		
		private boolean unique = false;
		
		private boolean primary = false;
		
		//скорее всего, лишнее, если только декоративные функции
		private boolean foreign = false;
		
		public void setNullable(boolean nullable) {
			this.nullable = nullable;
		}
		
		public boolean isNullable() {
			return nullable;
		}
		
		public void setUnique(boolean unique) {
			this.unique = unique;
		}
		
		public boolean isUnique() {
			return unique;
		}
		
		public void setPrimary(boolean primary) {
			this.primary = primary;
		}
		
		public boolean isPrimary() {
			return primary;
		}
		
		public void setForeign(boolean foreign) {
			this.foreign = foreign;
		}
		
		public boolean isForeign() {
			return foreign;
		}
	}
	
	private Constraints constraints;
	
	public Attribute() {
		constraints = new Constraints();
		indexes = new ArrayList<>();
	}
	
	//вынести в контроллер?
	/**
	 * Установка пользовательского типа атрибута
	 * @param newTypeName новый тип атрибута
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
	
	public void addToIndex(Index index) {
		if (!indexes.contains(index)) {
			indexes.add(index);
			index.attributes.add(this);
		}
	}
	
	public void removeFromIndex(Index index) {
		indexes.remove(index);
		index.attributes.remove(this)
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
	
	public String getActiveAttributeType() {
		return activeAttributeType;
	}
	
	public void setActiveAttributeType(String activeAttributeType) {
		this.activeAttributeType = activeAttributeType;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setConstraints(Constraints constraints) {
		this.constraints = constraints;
	}
	
	public Constraints getConstraints() {
		return constraints;
	}
	
	public List<Index> getIndexes() {
		return indexes;
	}
	
	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}
}
