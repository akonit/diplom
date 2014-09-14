package entity;

import attribute.Attribute

//предполагается использовать его для FK. мб что еще сюда добавить - on delete cascade и т д
//добавить listeners для обновления fk столбцов при изменении индекса
public class Index implements Serializable {
	
	long id

	private String name;
	
	private String commentary
	
	private List<Attribute> attributes = new ArrayList<>();
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addAttribute(Attribute attr) {
		if (!attributes.contains(attr)) {
			attributes.add(attr)
			attr.indexes.add(this)
		}
	}
	
	public void removeAttribute(Attribute attr) {
		attributes.remove(attr);
		attr.indexes.remove(this);
	}
	
	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}
	
	public List<Attribute> getAttributes() {
		return attributes;
	}
}
