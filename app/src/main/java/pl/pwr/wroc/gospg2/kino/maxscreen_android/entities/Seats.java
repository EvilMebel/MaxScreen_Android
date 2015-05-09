package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Seats {
	public static final String IDSEATS = "idSeats";
	public static final String COUNT = "Count";
	public static final String ROW = "Row";
	public static final String HALLS_IDHALL = "Halls_idHall";
	
	
	private String idSeats;
	private String Count;
	private String Row;
	private String Halls_idHall;
	private Halls HallsEntity;
	
	
	public String getIdSeats() {
		return idSeats;
	}
	public void setIdSeats(String idSeats) {
		this.idSeats = idSeats;
	}
	public String getCount() {
		return Count;
	}
	public void setCount(String count) {
		Count = count;
	}
	public String getRow() {
		return Row;
	}
	public void setRow(String row) {
		Row = row;
	}
	public String getHalls_idHall() {
		return Halls_idHall;
	}
	public void setHalls_idHall(String halls_idHall) {
		Halls_idHall = halls_idHall;
	}
	public Halls getHallsEntity() {
		return HallsEntity;
	}
	public void setHallsEntity(Halls hallsEntity) {
		HallsEntity = hallsEntity;
	}
	
}
