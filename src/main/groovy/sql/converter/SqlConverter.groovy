package sql.converter

import attribute.Attribute
import entity.Entity
import entity.Index
import groovy.sql.Sql
import utils.*
import relationship.Relationship

abstract class SqlConverter {

	/**
	 * Преобразование текущей диаграммы к SQL-коду
	 * @return
	 */
	public abstract String convertToSql()
}
