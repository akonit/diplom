package sql.converter;

import java.util.List;

import relationship.Relationship;
import attribute.Attribute
import entity.Entity;

public class MySqlConverter extends SqlConverter {

	@Override
	public String convertToSql(List<Entity> entities,
			List<Relationship> relations) {
		StringBuilder sb = new StringBuilder();

		if(entities != null) {
			for (Entity entity : entities) {
				sb.append(convertEntity(entity)).append("\n");
			}
			
		}
		sb.delete(sb.size() - 1, sb.size());
		return sb.toString();
	}

	private StringBuilder convertEntity(Entity entity) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(entity.getName());

		if (entity.getAttributes() != null) {
			sb.append(" (\n");
			for (Attribute attribute : entity.getAttributes()) {
				sb.append("\t").append(appendAttribute(attribute)).append("\n");
			}
			sb.delete(sb.size() - 2, sb.size());
			sb.append(");");
		}
		return sb;
	}
	
	//добавить constraints, keyGroups
	private StringBuilder appendAttribute(Attribute attribute) {
		StringBuilder sb = new StringBuilder();
		sb
		  .append(attribute.name)
		  .append(" ")
		  .append(attribute.activeAttributeType)
		  .append(",")
		return sb;
	}
}
