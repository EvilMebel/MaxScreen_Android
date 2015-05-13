package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

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


	public static Halls parseEntity(JSONObject object) {
		Halls h = new Halls();

		try {
			h.setIdHall(object.getInt(Halls.IDHALL));
			h.setName_Hall(object.getString(Halls.NAME_HALL));
			h.setStructureFile(object.getString(Halls.STRUCTUREFILE));
			h.setRows(object.getInt(Halls.ROWS));
			Log.e("parse", "end? ");
		} catch (JSONException e) {
			Log.e("parse", "error ");
			e.printStackTrace();
			h = null;
		}

		return h;
	}
}
