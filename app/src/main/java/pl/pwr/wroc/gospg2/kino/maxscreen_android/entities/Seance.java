package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class Seance {
	public static final String IDSEANCE = "idSeance";
	public static final String DATE = "Date";
	public static final String TYPE = "Type";
	public static final String PRICE = "Price";
	//keys
	public static final String HALLS_IDHALL = "Hall_idHall";
	public static final String MOVIE_IDMOVE = "Movie_idMove";

	private int idSeance;
	private GregorianCalendar date;
	private String Type;
	private int Price;
	//keys
	private int Halls_idHall;
	private int Movie_idMove;
	private Halls HallsEntity;
	private Movie MovieEntity;
	private int dateString;

	public int getIdSeance() {
		return idSeance;
	}
	public void setIdSeance(int idSeance) {
		this.idSeance = idSeance;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public int getPrice() {
		return Price;
	}
	public void setPrice(int price) {
		Price = price;
	}
	public int getHalls_idHall() {
		return Halls_idHall;
	}
	public void setHalls_idHall(int halls_idHall) {
		Halls_idHall = halls_idHall;
	}
	public int getMovie_idMove() {
		return Movie_idMove;
	}
	public void setMovie_idMove(int movie_idMove) {
		Movie_idMove = movie_idMove;
	}
	public Halls getHallsEntity() {
		return HallsEntity;
	}
	public void setHallsEntity(Halls hallsEntity) {
		HallsEntity = hallsEntity;
	}
	public Movie getMovieEntity() {
		return MovieEntity;
	}
	public void setMovieEntity(Movie movieEntity) {
		MovieEntity = movieEntity;
	}

	/*
					TOOLS
	 */

	//todo make it prettier!
	public String getDateString() {
		Log.d("Seance","Date=" + getDate().get(Calendar.HOUR));
		return Converter.getHourFromGreCale(getDate());
	}

	public static Seance parseEntity(JSONObject object) {
		Seance n = new Seance();

		try {
			n.setIdSeance(object.getInt(Seance.IDSEANCE));
			String date = object.getString(Seance.DATE);
			GregorianCalendar calendar = Converter.DATETIMEformatToGreg(date);
			n.setDate(calendar);
			n.setType(object.getString(Seance.TYPE));
			n.setPrice(object.getInt(Seance.PRICE));
			n.setHalls_idHall(object.getInt(Seance.HALLS_IDHALL));
			n.setMovie_idMove(object.getInt(Seance.MOVIE_IDMOVE));
		} catch (JSONException e) {
			Log.e("parse","error parse");
			e.printStackTrace();
			n = null;
		}

		return n;
	}
}
