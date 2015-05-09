package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

public class Relief {
	public static final String IDRELIEF= "idRelief";
	public static final String NAME= "Name";
	public static final String PERCENTDISCOUNT= "PercentDiscount";
	
	
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

}
