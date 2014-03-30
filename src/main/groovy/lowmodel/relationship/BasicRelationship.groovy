package lowmodel.relationship;

import lowmodel.attribute.BasicAttribute;
import lowmodel.entity.BasicEntity;

import org.apache.log4j.Logger;

/**
 * Связи между таблицами (FK). Создание предполагается через обращение к пакету utils.
 */
//проверить на избыточность данных
public class BasicRelationship {

	static Logger log = Logger.getLogger(BasicRelationship.class.getName());
	
	private BasicAttribute fromAttr;
	
	private BasicAttribute toAttr;
	
	private BasicEntity fromEntity;
	
	private BasicEntity toEntity;

	public BasicAttribute getFromAttr() {
		return fromAttr;
	}

	public void setFromAttr(BasicAttribute fromAttr) {
		this.fromAttr = fromAttr;
	}

	public BasicAttribute getToAttr() {
		return toAttr;
	}

	public void setToAttr(BasicAttribute toAttr) {
		this.toAttr = toAttr;
	}

	public BasicEntity getFromEntity() {
		return fromEntity;
	}

	public void setFromEntity(BasicEntity fromEntity) {
		this.fromEntity = fromEntity;
	}

	public BasicEntity getToEntity() {
		return toEntity;
	}

	public void setToEntity(BasicEntity toEntity) {
		this.toEntity = toEntity;
	}
	
}
