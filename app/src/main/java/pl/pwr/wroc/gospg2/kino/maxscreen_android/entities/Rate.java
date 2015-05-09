package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Rate {
	public static final String IDRATE = "idRate";
	public static final String RATE = "Rate";
	public static final String COMMENT = "Comment";
	//keys
	public static final String MOVIE_IDMOVE = "Movie_idMove";
	public static final String CUSTOMERS_IDCUSTOMER = "Customers_idCustomer";
	
	
	private int idRate;
	private int Rate;
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
		return Rate;
	}
	public void setRate(int rate) {
		Rate = rate;
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
	
}
