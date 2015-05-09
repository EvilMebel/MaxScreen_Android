package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class WantToSee {
	public static final String MOVIE_IDMOVE = "Movie_idMove";
	public static final String CUSTOMERS_IDCUSTOMER = "Customers_idCustomer";
	
	private String Movie_idMove;
	private String Customers_idCustomer;
	private Movie MovieEntity;
	private Customers CustomersEntity;
	
	
	public String getMovie_idMove() {
		return Movie_idMove;
	}
	public void setMovie_idMove(String movie_idMove) {
		Movie_idMove = movie_idMove;
	}
	public String getCustomers_idCustomer() {
		return Customers_idCustomer;
	}
	public void setCustomers_idCustomer(String customers_idCustomer) {
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

}
