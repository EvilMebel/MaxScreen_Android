package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Halls {
	public static final String IDHALL = "idHall";
	public static final String NAME_HALL = "Name_Hall";
	public static final String ROWS = "Rows";
	public static final String STRUCTUREFILE = "StructureFile";
	
	private int idHall;
	private String Name_Hall;
	private int Rows;
	private String StructureFile;
	
	
	public int getIdHall() {
		return idHall;
	}
	public void setIdHall(int idHall) {
		this.idHall = idHall;
	}
	public String getName_Hall() {
		return Name_Hall;
	}
	public void setName_Hall(String name_Hall) {
		Name_Hall = name_Hall;
	}
	public int getRows() {
		return Rows;
	}
	public void setRows(int rows) {
		Rows = rows;
	}
	public String getStructureFile() {
		return StructureFile;
	}
	public void setStructureFile(String structureFile) {
		StructureFile = structureFile;
	}
	
	

}
