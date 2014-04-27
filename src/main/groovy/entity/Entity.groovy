package entity;

import java.util.List;

import attribute.Attribute;

/**
 * Таблица БД.
 */
public class Entity {

	/**
	 * Название таблицы.
	 */
	private String name;
	
	/**
	 * Список атрибутов таблицы.
	 */
	private List<Attribute> attributes;

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
