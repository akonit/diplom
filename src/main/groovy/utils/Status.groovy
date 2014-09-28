package utils

/**
 * Состояние объекта. Текущее состояние - самая последняя по времени
 * запись в БД в статусе 'DONE'.
 */
public enum Status {

	/**
	 * Выполненное действие.
	 */
	DONE("D"),
	
	/**
	 * Отмененное действие.
	 */
	UNDONE("U")
	
	private String name
	
	
	private Status(String name) {
		this.name = name
	}
	
	public static Status getByName(String name) {
		for (Status status : Status.values()) {
			if(status.name.equals(name)) {
				return status
			}
		}
		
		return null
	}
	
	public String getName() {
		return name
	}
}
