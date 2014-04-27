package relationship;

import org.apache.log4j.Logger;

import attribute.Attribute;
import relationship.Relationship;
import entity.Entity;

/**
 * Связи между таблицами (FK). Создание предполагается через обращение к пакету utils.
 */
//проверить на избыточность данных
public class Relationship {

	static Logger log = Logger.getLogger(Relationship.class.getName());
	
	private Attribute fromAttr;
	
	private Attribute toAttr;
	
	private Entity fromEntity;
	
	private Entity toEntity;

	public Attribute getFromAttr() {
		return fromAttr;
	}

	public void setFromAttr(Attribute fromAttr) {
		this.fromAttr = fromAttr;
	}

	public Attribute getToAttr() {
		return toAttr;
	}

	public void setToAttr(Attribute toAttr) {
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
	
}
