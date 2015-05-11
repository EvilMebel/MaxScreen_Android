package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import java.util.Date;
import java.util.GregorianCalendar;

public class Coupon_DB {
	public static final String IDCOUPON_DB = "idCoupon_DB";
	public static final String ID_COUPON = "ID_Coupon";
	public static final String DATE = "Date";
	public static final String DESCRIPTION = "Description";
	public static final String DISCOUNT = "Discount";
	public static final String VERSION = "Version";
	
	
	private int idCoupon_DB;
	private String ID_Coupon;
	private GregorianCalendar date;
	private String Description;
	private int Discount;
	private String Version;
	
	
	public int getIdCoupon_DB() {
		return idCoupon_DB;
	}
	public void setIdCoupon_DB(int idCoupon_DB) {
		this.idCoupon_DB = idCoupon_DB;
	}
	public String getID_Coupon() {
		return ID_Coupon;
	}
	public void setID_Coupon(String iD_Coupon) {
		ID_Coupon = iD_Coupon;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public int getDiscount() {
		return Discount;
	}
	public void setDiscount(int discount) {
		Discount = discount;
	}
	public String getVersion() {
		return Version;
	}
	public void setVersion(String version) {
		Version = version;
	}

	
	

}
