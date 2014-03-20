package lowmodel.attribute.type

import interfaces.SubAttributeTypes

/**
 * Группа атрибутов. переименовать enum?
 */
public enum AttributeType {
		STRING ("String", StringTypes.getAllNames()), 
		DATE_TIME("Datetime", DatetimeTypes.getAllNames()), 
		BLOB(null, null),
		NUMBER(null, null),
		DEFAULT(null, null);//все предыдущие типы. возможно, нет смысла его отдельно делать
		
		/**
		 * Название группы атрибута для представления.
		 */
		private String name;
		
		/**
		 * Конкретизированные типы атрибута.
		 */
		public List<String> subTypes;
		
		private AttributeType (String name, List<String> subTypes) {
			this.name = name;
			this.subTypes = subTypes;
		}
}
