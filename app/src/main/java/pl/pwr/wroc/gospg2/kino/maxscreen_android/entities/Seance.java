package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import java.util.Date;

public class Seance {
	public static final String IDSEANCE = "idSeance";
	public static final String DATE = "Date";
	public static final String TYPE = "Type";
	public static final String PRICE = "Price";
	//keys
	public static final String HALLS_IDHALL = "Halls_idHall";
	public static final String MOVIE_IDMOVE = "Movie_idMove";
	
	
	private int idSeance;
	private Date date;
	private String Type;
	private int Price;
	//keys
	private int Halls_idHall;
	private int Movie_idMove;
	private Halls HallsEntity;
	private Movie MovieEntity;
	
	
	public int getIdSeance() {
		return idSeance;
	}
	public void setIdSeance(int idSeance) {
		this.idSeance = idSeance;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
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
}
