package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class Movie {
	public static final String IDMOVIE = "idMovie";
	public static final String TITLE = "Title";
	public static final String DESCRIPTION = "Description";
	public static final String DIRECTOR = "Director";
	public static final String MINUTES = "Minutes";
	public static final String KIND = "Kind";
	public static final String CAST = "Cast";
	public static final String WANTTOSEETHIS = "WantToSeeThis";
	public static final String IMAGES = "Images";
	public static final String SCRIPT = "Script";
	public static final String PREMIERE = "Premiere";
	
	private int idMove;
	private String Title;
	private String Description;
	private String Director;
	private int Minutes;
	private String Kind;
	private String Cast;
	private int WantToSeeThis;
	private String Images;
	private String Script;//todo
	private GregorianCalendar Premiere;



	//todo for adapter
	private List<Seance> seances;
	
	
	public int getIdMove() {
		return idMove;
	}
	public void setIdMovie(int idMove) {
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

	public String getScript() {
		return Script;
	}

	public void setScript(String script) {
		Script = script;
	}

	public GregorianCalendar getPremiere() {
		return Premiere;
	}

	public void setPremiere(GregorianCalendar premiere) {
		Premiere = premiere;
	}

	public List<Seance> getSeances() {
		return seances;
	}

	public void setSeances(List<Seance> seances) {
		this.seances = seances;
	}

	/*
					tools
	 */
	public static Movie parseEntity(JSONObject object) {
		Movie n = new Movie();

		Log.d("Movie", "Parse:" + object.toString());

		try {
			n.setIdMovie(object.getInt(Movie.IDMOVIE));

			String date = object.getString(Movie.PREMIERE);
			GregorianCalendar calendar = Converter.DATETIMEformatToGreg(date);
			n.setPremiere(calendar);

			n.setImages(object.getString(Movie.IMAGES));
			n.setTitle(object.getString(Movie.TITLE));
			n.setDescription(object.getString(Movie.DESCRIPTION));
			n.setDirector(object.getString(Movie.DIRECTOR));
			n.setKind(object.getString(Movie.KIND));
			n.setCast(object.getString(Movie.CAST));
			n.setScript(object.getString(Movie.SCRIPT));
			n.setMinutes(object.getInt(Movie.MINUTES));
			n.setWantToSeeThis(object.getInt(Movie.WANTTOSEETHIS));

		} catch (JSONException e) {
			e.printStackTrace();
			n = null;
		}

		return n;
	}
}
