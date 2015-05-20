package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class Reservation {
	public static final String IDRESERVATION = "idReservation";
	public static final String BOUGHT = "bought";
	//keys
	public static final String CUSTOMERS_IDCUSTOMER = "customeridCustomer";
	public static final String SEANCE_IDSEANCE = "seanceidSeance";
	
	
	private int idReservation;
	private boolean Bought;
	
	private int Customers_idCustomer;
	private int Seance_idSeance;
	private Customers CustomersEntity;
	private Seance SeanceEntity;
	
	
	public int getIdReservation() {
		return idReservation;
	}
	public void setIdReservation(int idReservation) {
		this.idReservation = idReservation;
	}
	public boolean getBought() {
		return Bought;
	}
	public void setBought(boolean bought) {
		Bought = bought;
	}
	public int getCustomers_idCustomer() {
		return Customers_idCustomer;
	}
	public void setCustomers_idCustomer(int customers_idCustomer) {
		Customers_idCustomer = customers_idCustomer;
	}
	public int getSeance_idSeance() {
		return Seance_idSeance;
	}
	public void setSeance_idSeance(int seance_idSeance) {
		Seance_idSeance = seance_idSeance;
	}
	public Customers getCustomersEntity() {
		return CustomersEntity;
	}
	public void setCustomersEntity(Customers customersEntity) {
		CustomersEntity = customersEntity;
	}
	public Seance getSeanceEntity() {
		return SeanceEntity;
	}
	public void setSeanceEntity(Seance seanceEntity) {
		SeanceEntity = seanceEntity;
	}


	public static Reservation parseEntity(JSONObject object, boolean inception) {
		Reservation h = new Reservation();

		try {
			h.setIdReservation(object.getInt(Reservation.IDRESERVATION));
			h.setBought(object.getBoolean(Reservation.BOUGHT));


			if(inception) {
				h.setCustomersEntity(Customers.parseEntity(object.getJSONObject(Reservation.CUSTOMERS_IDCUSTOMER),inception));
				h.setSeanceEntity(Seance.parseEntity(object.getJSONObject(Reservation.SEANCE_IDSEANCE),inception));
			} else {
				//todo
			}

			Log.e("parse", "end? ");
		} catch (JSONException e) {
			Log.e("parse", "error ");
			e.printStackTrace();
			h = null;
		}

		return h;
	}
}
