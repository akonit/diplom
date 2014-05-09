package relationship;

import org.apache.log4j.Logger;

import attribute.Attribute;
import relationship.Relationship;
import entity.Entity;
import entity.Index

/**
 * Связи между таблицами (FK). Создание предполагается через обращение к пакету utils.
 */
public class Relationship implements Serializable {

	static Logger log = Logger.getLogger(Relationship.class.getName());
	
	private Index index;
	
	private List<Attribute> toAttr = new ArrayList<>();
	
	private Entity fromEntity;
	
	private Entity toEntity;
	
	public class Cardinality implements Serializable {
		private boolean identifying;
		
		//enum type - нужно ли?
		
		public void setIdentifying(boolean identifying) {
			this.identifying = identifying;
			if (identifying) {
				for (Attribute attr : toAttr) {
					attr.getConstraints().setPrimary(true);
				}
			} else {
				for (Attribute attr : toAttr) {
					attr.getConstraints().setPrimary(false);
				}
			}
		}
		
		public boolean isIdentifying() {
			return identifying;
		}
	}
	
	private Cardinality cardinality = new Cardinality();

	public List<Attribute> getToAttr() {
		return toAttr;
	}

	public void setToAttr(List<Attribute> toAttr) {
		this.toAttr = toAttr;
	}

	public Entity getFromEntity() {
		return fromEntity;
	}

	public void setFromEntity(Entity fromEntity) {
		this.fromEntity = fromEntity;
	}

	public Entity getToEntity() {
		return toEntity;
	}

	public void setToEntity(Entity toEntity) {
		this.toEntity = toEntity;
	}
	
	public void setCardinality(Cardinality cardinality) {
		this.cardinality = cardinality;
	}
	
	public Cardinality getCardinality() {
		return cardinality;
	}
	
	public void setIndex(Index index) {
		this.index = index;
	}
	
	public Index getIndex() {
		return index;
	}
}
