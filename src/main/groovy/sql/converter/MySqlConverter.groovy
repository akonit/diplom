package sql.converter;

import java.util.List;

import relationship.Relationship;
import attribute.Attribute
import entity.Entity;
import entity.Index

/**
 * Особенности синтаксиса:
 * ограничение unique записывается отдельно от самих атрибутов.
 */
public class MySqlConverter extends SqlConverter {

	@Override
	public String convertToSql(List<Entity> entities,
			List<Relationship> relations) {
		StringBuilder sb = new StringBuilder();

		if(entities != null) {
		    StringBuilder fk = new StringBuilder();
			for (Entity entity : entities) {
				sb.append(convertEntity(entity, relations, fk)).append("\n");
			}
			sb.append(fk)
		}
		sb.delete(sb.size() - 1, sb.size());
		return sb.toString();
	}

	private StringBuilder convertEntity(Entity entity, List<Relationship> relations,
		StringBuilder foreignKeys) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE ");
		sb.append(entity.getName());

		if (entity.getAttributes() != null) {
			List<String> uniques = new ArrayList<>();
			List<String> primaries = new ArrayList<>();
			
			sb.append(" (\n");
			for (Attribute attribute : entity.getAttributes()) {
				sb.append("\t").append(appendAttribute(attribute)).append("\n");
				if (attribute.getConstraints().isUnique()) {
					uniques.add(attribute.getName());
				}
				if (attribute.getConstraints().isPrimary()) {
					primaries.add(attribute.getName())
				}
			}
			
			if (!primaries.empty) {
				sb.append(appendPrimary(primaries, entity.getName()))
			}
			
			if (!uniques.empty) {
				sb.append(appendUnique(uniques));
			} 
			
			sb.append(appendIndexes(entity.indexes));
			foreignKeys.append(appendForeign(entity, relations));
			
			sb.delete(sb.size() - 2, sb.size());
			sb.append(");");
		}
		return sb;
	}
	
	private StringBuilder appendAttribute(Attribute attribute) {
		StringBuilder sb = new StringBuilder();
		sb
		  .append(attribute.name)
		  .append(" ")
		  .append(attribute.activeAttributeType)
		  
		if (!attribute.getConstraints().isNullable()) {
			sb.append(" NOT NULL")
		}
		
		sb.append(",")
		return sb;
	}
	
	private StringBuilder appendUnique(List<String> attributes) {
		StringBuilder sb = new StringBuilder();
		sb.append("\t").append("UNIQUE (");
		for (String unique : attributes) {
			sb.append(unique).append(", ");
		}
		sb.delete(sb.size() - 2, sb.size());
		sb.append("),\n");
		return sb;
	}
	
	private StringBuilder appendPrimary(List<String> attributes, String entityName) {
		StringBuilder sb = new StringBuilder();
		sb
		  .append("\t")
		  .append("CONSTRAINT pk_")
		  .append(entityName)
		  .append(" PRIMARY KEY (");
		for (String primary : attributes) {
			sb.append(primary).append(", ");
		}
		sb.delete(sb.size() - 2, sb.size());
		sb.append("),\n");
		return sb;
	}
	
	private StringBuilder appendIndexes(List<Index> indexes) {
		StringBuilder sb = new StringBuilder();
		for (Index index : indexes) {
			if (!index.attributes.empty) {
				sb.append("\t").append("INDEX (");
				for (Attribute attr : index.attributes) {
					sb.append(attr.name).append(", ");
				}
				sb.delete(sb.size() - 2, sb.size());
				sb.append("),\n");
			}
		}
		return sb;
	}
	
	private StringBuilder appendForeign(Entity entity, List<Relationship> relations) {
		StringBuilder sb = new StringBuilder();
		int num = 1;
		for (Relationship relation : relations) {
			if (relation.toEntity == entity) {
				if (!relation.index.attributes.empty) {
					sb
					  .append("ALTER TABLE ")
					  .append(entity.name)
					  .append("\n\t")
					  .append("ADD CONSTRAINT fk_")
					    .append(entity.name)
					    .append("_")
					    .append(relation.index.name)
					    .append(num)
					  .append(" FOREIGN KEY (");
					for (Attribute attr : relation.toAttr) {
						sb.append(attr.name).append(", ")
					}
					sb.delete(sb.size() - 2, sb.size());
					
					sb
					  .append(") ")
					  .append("REFERENCES ")
					  .append(relation.fromEntity.name)
					  .append(" (")
					for (Attribute attr : relation.index.attributes) {
						sb.append(attr.name).append(", ")
					}
					sb.delete(sb.size() - 2, sb.size());
					sb.append(");\n");
					num++;
				}
			}
		}
		return sb;
	}
}
