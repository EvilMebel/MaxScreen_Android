package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities;

import java.util.Date;

public class News {
	public static final String IDNEWS = "idNews";
	public static final String TEXT = "Text";
	public static final String IMAGE = "Image";
	public static final String DATE = "Date";
	public static final String TOPIC = "Topic";
	
	
	private int idNews;
	private String Text;
	private String Image;
	private Date date;
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
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getTopic() {
		return Topic;
	}
	public void setTopic(String topic) {
		Topic = topic;
	}
	
	
	

}
