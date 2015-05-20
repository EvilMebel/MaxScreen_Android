package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class Relief {
	public static final String IDRELIEF= "idRelief";
	public static final String NAME= "name";
	public static final String PERCENTDISCOUNT= "percentDiscount";
	
	
	private int idRelief;
	private String Name;
	private int PercentDiscount;
	
	
	public int getIdRelief() {
		return idRelief;
	}
	public void setIdRelief(int idRelief) {
		this.idRelief = idRelief;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public int getPercentDiscount() {
		return PercentDiscount;
	}
	public void setPercentDiscount(int percentDiscount) {
		PercentDiscount = percentDiscount;
	}

	public static Relief parseEntity(JSONObject object) {
		Relief n = new Relief();

		Log.d("Movie", "Parse:" + object.toString());

		try {
			n.setIdRelief(object.getInt(Relief.IDRELIEF));
			n.setName(object.getString(Relief.NAME));
			n.setPercentDiscount(object.getInt(Relief.PERCENTDISCOUNT));

		} catch (JSONException e) {
			e.printStackTrace();
			n = null;
		}

		return n;
	}
}
