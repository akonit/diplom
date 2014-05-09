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
	private List<Attribute> attributes = new ArrayList<>();
	
	/**
	 * Список всех индексов таблицы. 
	 */
	private List<Index> indexes = new ArrayList<>();

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public List<Index> getIndexes() {
		return indexes;
	}
	
	public void setIndexes(List<Index> indexes) {
		this.indexes = indexes;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
}
