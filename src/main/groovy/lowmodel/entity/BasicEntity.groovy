package lowmodel.entity;

import java.util.List;

import lowmodel.attribute.BasicAttribute;

/**
 * Таблица БД.
 */
public class BasicEntity {

	/**
	 * Название таблицы.
	 */
	private String name;
	
	/**
	 * Список атрибутов таблицы.
	 */
	private List<BasicAttribute> attributes;

	public List<BasicAttribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<BasicAttribute> attributes) {
		this.attributes = attributes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
