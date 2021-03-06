package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Tickets {
	public static final String IDTICKET = "idTicket";
	public static final String ROW = "row";
	public static final String LINE = "line";
	//keys
	public static final String RESERVATION_IDRESERVATION = "reservationidReservation";
	public static final String RELIEF_IDRELIEF = "reliefidRelief";
	
	
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

	public static Tickets parseEntitySimple(JSONObject object) {
		Tickets h = new Tickets();

		try {
			h.setLine(object.getInt(Tickets.LINE));
			h.setRow(object.getInt(Tickets.ROW));

		} catch (JSONException e) {
			Log.e("parse", "error ");
			e.printStackTrace();
			h = null;
		}

		return h;
	}

	public static Tickets parseEntityWRelief(JSONObject object) {
		Tickets h = new Tickets();

		try {
			h.setIdTicket(object.getInt(Tickets.IDTICKET));
			h.setLine(object.getInt(Tickets.LINE));
			h.setRow(object.getInt(Tickets.ROW));

			h.setReliefEntity(Relief.parseEntity(object.getJSONObject(Tickets.RELIEF_IDRELIEF)));

		} catch (JSONException e) {
			Log.e("parse", "error ");
			e.printStackTrace();
			h = null;
		}

		return h;
	}
}
