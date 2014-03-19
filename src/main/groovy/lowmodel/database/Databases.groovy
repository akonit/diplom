package lowmodel.database

//дописать еще что нибудь
public enum Databases {

	ORACLE ("Oracle");
	
	private String name;
	
	private Databases(String name) {
		this.name = name;
	}
}
