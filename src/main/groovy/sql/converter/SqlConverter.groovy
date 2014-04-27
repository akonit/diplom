package sql.converter

import entity.Entity
import relationship.Relationship

abstract class SqlConverter {

	/**
	 * Преобразование диаграммы к SQL-коду
	 * @param entities
	 * @param relations
	 * @return
	 */
	public abstract String convertToSql(List<Entity> entities, List<Relationship> relations);
	
	//getRollbackInstruction - если будет нечем заняться
}
