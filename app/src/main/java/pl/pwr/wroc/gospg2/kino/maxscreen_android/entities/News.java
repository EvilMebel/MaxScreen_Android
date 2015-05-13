package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

public class News {
	public static final String IDNEWS = "idNews";
	public static final String TEXT = "Text";
	public static final String IMAGE = "Image";
	public static final String DATE = "Date";
	public static final String TOPIC = "Topic";
	
	
	private int idNews;
	private String Text;
	private String Image;
	private GregorianCalendar date;
	private String Topic;
	
	
	public int getIdNews() {
		return idNews;
	}
	public void setIdNews(int idNews) {
		this.idNews = idNews;
	}
	public String getText() {
		return Text;
	}
	public void setText(String text) {
		Text = text;
	}
	public String getImage() {
		return Image;
	}
	public void setImage(String image) {
		Image = image;
	}
	public GregorianCalendar getDate() {
		return date;
	}
	public void setDate(GregorianCalendar date) {
		this.date = date;
	}
	public String getTopic() {
		return Topic;
	}
	public void setTopic(String topic) {
		Topic = topic;
	}

	public static News parseEntity(JSONObject object) {
		News n = new News();
		Log.d("News","Parse:" +object.toString());

		try {
			n.setIdNews(object.getInt(News.IDNEWS));
			String date = object.getString(News.DATE);
			GregorianCalendar calendar = Converter.DATETIMEformatToGreg(date);
			n.setDate(calendar);
			n.setImage(object.getString(News.IMAGE));
			n.setText(object.getString(News.TEXT));
			n.setTopic(object.getString(News.TOPIC));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return n;
	}
	
	
	

}
