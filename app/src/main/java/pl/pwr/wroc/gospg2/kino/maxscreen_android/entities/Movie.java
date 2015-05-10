package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import java.util.List;

public class Movie {
	public static final String IDMOVE = "idMove";
	public static final String TITLE = "Title";
	public static final String DESCRIPTION = "Description";
	public static final String DIRECTOR = "Director";
	public static final String MINUTES = "Minutes";
	public static final String KIND = "Kind";
	public static final String CAST = "Cast";
	public static final String WANTTOSEETHIS = "WantToSeeThis";
	public static final String IMAGES = "Images";
	
	private int idMove;
	private String Title;
	private String Description;
	private String Director;
	private int Minutes;
	private String Kind;
	private String Cast;
	private int WantToSeeThis;
	private String Images;



	//todo for adapter
	private List<Seance> seances;
	
	
	public int getIdMove() {
		return idMove;
	}
	public void setIdMove(int idMove) {
		this.idMove = idMove;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDirector() {
		return Director;
	}
	public void setDirector(String director) {
		Director = director;
	}
	public int getMinutes() {
		return Minutes;
	}
	public void setMinutes(int minutes) {
		Minutes = minutes;
	}
	public String getKind() {
		return Kind;
	}
	public void setKind(String kind) {
		Kind = kind;
	}
	public String getCast() {
		return Cast;
	}
	public void setCast(String cast) {
		Cast = cast;
	}
	public int getWantToSeeThis() {
		return WantToSeeThis;
	}
	public void setWantToSeeThis(int wantToSeeThis) {
		WantToSeeThis = wantToSeeThis;
	}
	public String getImages() {
		return Images;
	}
	public void setImages(String images) {
		Images = images;
	}

	public List<Seance> getSeances() {
		return seances;
	}

	public void setSeances(List<Seance> seances) {
		this.seances = seances;
	}
}
