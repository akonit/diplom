package interfaces;

import lowmodel.database.Databases;

/**
 * Конкретизированные типы атрибутов. Используются при генерации схемы БД (в отличие от групп
 * атрибутов).
 */
public interface SubAttributeTypes {

	String getName();
	
	Databases getDatabase();
	
	boolean isModifyable();
}
