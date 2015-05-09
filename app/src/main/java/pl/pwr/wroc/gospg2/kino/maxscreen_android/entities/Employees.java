package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Employees {
	public static final String IDEMPLOYEES = "idEmployees";
	public static final String NAME = "Name";
	public static final String SURNAME = "Surname";
	public static final String E_MAIL = "E-mail";
	public static final String PASSWORD = "Password";
	//keys
	public static final String POSITION_IDPOSITION = "Position_idPosition";
	public static final String TOKEN = "Token";
	
	
	private int idEmployees;
	private String Name;
	private String Surname;
	private String E_mail;
	private String Password;
	private String Token;
	
	private int Position_idPosition;
	private Position PositionEntity;
	
	
	public int getIdEmployees() {
		return idEmployees;
	}
	public void setIdEmployees(int idEmployees) {
		this.idEmployees = idEmployees;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getSurname() {
		return Surname;
	}
	public void setSurname(String surname) {
		Surname = surname;
	}
	public String getE_mail() {
		return E_mail;
	}
	public void setE_mail(String e_mail) {
		E_mail = e_mail;
	}
	public String getPassword() {
		return Password;
	}
	public void setPassword(String password) {
		Password = password;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public int getPosition_idPosition() {
		return Position_idPosition;
	}
	public void setPosition_idPosition(int position_idPosition) {
		Position_idPosition = position_idPosition;
	}
	public Position getPositionEntity() {
		return PositionEntity;
	}
	public void setPositionEntity(Position positionEntity) {
		PositionEntity = positionEntity;
	}
	
}
