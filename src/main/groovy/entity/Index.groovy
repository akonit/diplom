package entity

import attribute.Attribute
import utils.Status

public class Index implements Serializable {
	
	long id

	private String name
	
	private String commentary
	
	private long entityId
	
	private long entityTime
	
	private long time
	
	private Status status
	
	private boolean isDeleted
	
	private List<Attribute> attributes = new ArrayList<>();
	
	public void addAttribute(Attribute attr) {
		if (!attributes.contains(attr)) {
			attributes.add(attr)
			attr.indexes.add(this)
		}
	}
	
	public void removeAttribute(Attribute attr) {
		attributes.remove(attr)
		attr.indexes.remove(this)
	}
}
