package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Customers {
	public static final String IDCUSTOMER = "idCustomer";
	public static final String NAME = "name";
	public static final String SURNAME = "surname";
	public static final String E_MAIL = "EMail";
	public static final String TELEFON = "phone";
	public static final String NICK = "nick";
	public static final String PASSMD5 = "passMD5";
	public static final String FACEBOOKID = "facebookId";
	public static final String TOKEN = "token";
	public static final String AVATAR = "avatar";
	
	
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


	public static Customers parseEntity(JSONObject object) {
		Customers h = new Customers();

		try {
			h.setIdCustomer(object.getInt(Customers.IDCUSTOMER));

			if(object.has(Customers.NAME))
				h.setName(object.getString(Customers.NAME));

			if(object.has(Customers.SURNAME))
				h.setSurname(object.getString(Customers.SURNAME));

			if(object.has(Customers.E_MAIL))
				h.setE_Mail(object.getString(Customers.E_MAIL));

			if(object.has(Customers.TELEFON))
				h.setTelefon(object.getString(Customers.TELEFON));

			if(object.has(Customers.NICK))
				h.setNick(object.getString(Customers.NICK));

			if(object.has(Customers.PASSMD5))
				h.setPassMD5(object.getString(Customers.PASSMD5));

			if(object.has(Customers.TOKEN))
			h.setToken(object.getString(Customers.TOKEN));

			if(object.has(Customers.AVATAR))
			h.setAvatar(object.getString(Customers.AVATAR));

			//can be null
			if(object.has(Customers.FACEBOOKID))
				h.setFacebookId(object.getString(Customers.FACEBOOKID));
			Log.e("parse", "end? ");
		} catch (JSONException e) {
			Log.e("parse", "error ");
			e.printStackTrace();
			h = null;
		}

		return h;
	}
}
