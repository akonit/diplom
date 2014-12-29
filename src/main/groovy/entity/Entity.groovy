package entity

import java.util.List
import utils.Status

import attribute.Attribute

/**
 * Таблица БД.
 */
public class Entity {
	
	private long id

	/**
	 * Название таблицы.
	 */
	private String name
	
	private String commentary
	
	private Status status
	
	private boolean isDeleted
	
	private long time
	
	/**
	 * Список атрибутов таблицы.
	 */
	 private List<Attribute> attributes = new ArrayList<>()
	 /**
	 * Список всех индексов таблицы.
	 */
	 private List<Index> indexes = new ArrayList<>()
	
	/**
	 * Координата верхнего левого угла таблицы по оси X.
	 */
	private long xCoord
	
	/**
	 * Координата верхнего левого угла таблицы по оси Y.
	 */	
	private long yCoord
	
	/**
	 * Высота таблицы.
	 */
	private long height
	
	/**
	 * Ширина таблицы.
	 */
	private long width
}
