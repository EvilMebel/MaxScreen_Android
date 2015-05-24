package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class Rate {
	public static final String IDRATE = "idRate";
	public static final String RATE = "rate";
	public static final String COMMENT = "comment";
	//keys
	public static final String MOVIE_IDMOVE = "movieidMove";
	public static final String CUSTOMERS_IDCUSTOMER = "customeridCustomer";
	
	
	private int idRate;
	private int Rating;
	private String Comment;
	//keys
	private int Movie_idMove;
	private int Customers_idCustomer;
	private Movie MovieEntity;
	private Customers CustomersEntity;
	
	
	public int getIdRate() {
		return idRate;
	}
	public void setIdRate(int idRate) {
		this.idRate = idRate;
	}
	public int getRate() {
		return Rating;
	}
	public void setRate(int rate) {
		Rating = rate;
	}
	public String getComment() {
		return Comment;
	}
	public void setComment(String comment) {
		Comment = comment;
	}
	public int getMovie_idMove() {
		return Movie_idMove;
	}
	public void setMovie_idMove(int movie_idMove) {
		Movie_idMove = movie_idMove;
	}
	public int getCustomers_idCustomer() {
		return Customers_idCustomer;
	}
	public void setCustomers_idCustomer(int customers_idCustomer) {
		Customers_idCustomer = customers_idCustomer;
	}
	public Movie getMovieEntity() {
		return MovieEntity;
	}
	public void setMovieEntity(Movie movieEntity) {
		MovieEntity = movieEntity;
	}
	public Customers getCustomersEntity() {
		return CustomersEntity;
	}
	public void setCustomersEntity(Customers customersEntity) {
		CustomersEntity = customersEntity;
	}

	public static Rate parseEntity(JSONObject object, boolean inception) {
		Rate n = new Rate();

		Log.d("Rate", "Parse:" + object.toString());

		try {
			n.setIdRate(object.getInt(Rate.IDRATE));

			/*//ou...
			String date = object.getString(Rate.OPREMIERE);
			GregorianCalendar calendar = Converter.DATETIMEformatToGreg(date);
			n.setPremiere(calendar);*/

			n.setRate(object.getInt(Rate.RATE));
			n.setComment(object.getString(Rate.COMMENT));

			if(inception) {
				n.setCustomersEntity(Customers.parseEntity(object.getJSONObject(Rate.CUSTOMERS_IDCUSTOMER)));
				n.setMovieEntity(Movie.parseEntity(object.getJSONObject(Rate.MOVIE_IDMOVE)));
			} else {
				n.setCustomers_idCustomer(object.getJSONObject(Rate.CUSTOMERS_IDCUSTOMER).getInt(Customers.IDCUSTOMER));
				n.setMovie_idMove(object.getJSONObject(Rate.MOVIE_IDMOVE).getInt(Movie.IDMOVIE));
			}

		} catch (JSONException e) {
			e.printStackTrace();
			n = null;
		}

		return n;
	}
}
