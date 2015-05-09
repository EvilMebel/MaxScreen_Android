package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Tickets {
	public static final String IDTICKET = "idTicket";
	public static final String ROW = "Row";
	public static final String LINE = "Line";
	//keys
	public static final String RESERVATION_IDRESERVATION = "Reservation_idReservation";
	public static final String RELIEF_IDRELIEF = "Relief_idRelief";
	
	
	private int idTicket;
	private int Row;
	private int Line;
	//keys
	private int Reservation_idReservation;
	private int Relief_idRelief;
	private Reservation ReservationEntity;
	private Relief ReliefEntity;
	
	
	public int getIdTicket() {
		return idTicket;
	}
	public void setIdTicket(int idTicket) {
		this.idTicket = idTicket;
	}
	public int getRow() {
		return Row;
	}
	public void setRow(int row) {
		Row = row;
	}
	public int getLine() {
		return Line;
	}
	public void setLine(int line) {
		Line = line;
	}
	public int getReservation_idReservation() {
		return Reservation_idReservation;
	}
	public void setReservation_idReservation(int reservation_idReservation) {
		Reservation_idReservation = reservation_idReservation;
	}
	public int getRelief_idRelief() {
		return Relief_idRelief;
	}
	public void setRelief_idRelief(int relief_idRelief) {
		Relief_idRelief = relief_idRelief;
	}
	public Reservation getReservationEntity() {
		return ReservationEntity;
	}
	public void setReservationEntity(Reservation reservationEntity) {
		ReservationEntity = reservationEntity;
	}
	public Relief getReliefEntity() {
		return ReliefEntity;
	}
	public void setReliefEntity(Relief reliefEntity) {
		ReliefEntity = reliefEntity;
	}
	
}
