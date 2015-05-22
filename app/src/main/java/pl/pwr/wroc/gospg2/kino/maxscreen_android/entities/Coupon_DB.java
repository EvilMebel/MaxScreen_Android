package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class Coupon_DB {
	public static final String IDCOUPON_DB = "idCouponDB";
	public static final String ID_COUPON = "IDCoupon";
	public static final String DATE = "date";
	public static final String DESCRIPTION = "description";
	public static final String DISCOUNT = "discount";
	public static final String VERSION = "version";
	
	
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


	public static Coupon_DB parseEntity(JSONObject object) {
		Coupon_DB n = new Coupon_DB();
		Log.d("Coupon_DB", "Parse:" + object.toString());

		try {
			n.setIdCoupon_DB(object.getInt(Coupon_DB.IDCOUPON_DB));
			String date = object.getString(Coupon_DB.DATE);
			GregorianCalendar calendar = Converter.DATETIMEformatToGreg(date);
			n.setDate(calendar);
			n.setID_Coupon(object.getString(Coupon_DB.ID_COUPON));
			n.setDescription(object.getString(Coupon_DB.DESCRIPTION));
			n.setDiscount(object.getInt(Coupon_DB.DISCOUNT));
			n.setVersion(object.getString(Coupon_DB.VERSION));

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return n;
	}
}
