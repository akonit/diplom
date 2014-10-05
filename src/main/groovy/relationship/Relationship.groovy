package relationship;

import org.apache.log4j.Logger;

import attribute.Attribute;
import relationship.Relationship;
import utils.Status
import entity.Entity;
import entity.Index

/**
 * Связи между таблицами (FK).
 */
//добавить имя?
public class Relationship implements Serializable {

	static Logger log = Logger.getLogger(Relationship.class.getName())
	
	private long id
	
	private List<Attribute> toAttr = new ArrayList<>()
	
	private long fromEntityId
	
	private long fromEntityTime
	
	private long toEntityId
	
	private long toEntityTime
	
	private long time
	
	private boolean isDeleted
	
	private Status status
	
	private long indexId
	
	private long indexTime
	
	public static class Cardinality implements Serializable {
		
		private boolean identifying
	}
	
	private Cardinality cardinality = new Cardinality()

	public List<Attribute> getToAttr() {
		return toAttr
	}

	public void setToAttr(List<Attribute> toAttr) {
		this.toAttr = toAttr
	}
	
	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality
	}
	
	public Cardinality getCardinality() {
		return cardinality
	}
	
	public void setIndex(Index index) {
		this.index = index;
	}
	
	public Index getIndex() {
		return index;
	}
}
