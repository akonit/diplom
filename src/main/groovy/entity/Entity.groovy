package entity;

import java.util.List;

import attribute.Attribute;

/**
 * Таблица БД.
 */
public class Entity {
	
	private long id

	/**
	 * Название таблицы.
	 */
	private String name;
	
	private String commentary
	
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
	
	public void setCommentary(String commentary) {
		this.commentary = commentary
	}
	
	public String getCommentary() {
		return commentary
	}
	
	public void setId(long id) {
		this.id = id
	}
	
	public long getId() {
		return id
	}
}
