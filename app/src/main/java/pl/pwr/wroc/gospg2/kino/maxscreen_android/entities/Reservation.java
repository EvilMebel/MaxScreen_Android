package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import java.util.Date;

public class Reservation {
	public static final String IDRESERVATION = "idReservation";
	public static final String BOUGHT = "Bought";
	//keys
	public static final String CUSTOMERS_IDCUSTOMER = "Customers_idCustomer";
	public static final String SEANCE_IDSEANCE = "Seance_idSeance";
	
	
	private int idReservation;
	private Date Bought;
	
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
	public Date getBought() {
		return Bought;
	}
	public void setBought(Date bought) {
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
	
	

}
