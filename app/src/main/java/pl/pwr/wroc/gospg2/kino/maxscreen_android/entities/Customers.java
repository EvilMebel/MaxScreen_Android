package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Customers {
	public static final String IDCUSTOMER = "idCustomer";
	public static final String NAME = "Name";
	public static final String SURNAME = "Surname";
	public static final String E_MAIL = "Email";
	public static final String TELEFON = "Telefon";
	public static final String NICK = "Nick";
	public static final String PASSMD5 = "PassMD5";
	public static final String FACEBOOKID = "FacebookId";
	public static final String TOKEN = "Token";
	public static final String AVATAR = "Avatar";
	
	
	private int idCustomer;
	private String Name;
	private String Surname;
	private String E_Mail;
	private String Telefon;
	private String Nick;
	private String PassMD5;
	private String FacebookId;
	private String Token;
	private String Avatar;
	
	public int getIdCustomer() {
		return idCustomer;
	}
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
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
	
	public String getE_Mail() {
		return E_Mail;
	}
	public void setE_Mail(String e_Mail) {
		E_Mail = e_Mail;
	}
	public String getTelefon() {
		return Telefon;
	}
	public void setTelefon(String telefon) {
		Telefon = telefon;
	}
	public String getNick() {
		return Nick;
	}
	public void setNick(String nick) {
		Nick = nick;
	}
	public String getPassMD5() {
		return PassMD5;
	}
	public void setPassMD5(String passMD5) {
		PassMD5 = passMD5;
	}
	public String getFacebookId() {
		return FacebookId;
	}
	public void setFacebookId(String facebookId) {
		FacebookId = facebookId;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	public String getAvatar() {
		return Avatar;
	}
	public void setAvatar(String avatar) {
		Avatar = avatar;
	}
	
	

}
