package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.old;

import java.util.Calendar;

/**
 * Created by Evil on 2015-03-31.
 */
public class News {
    int id;
    Calendar date;
    String image_src;
    String text;

    public News(int id, Calendar date, String image_src, String text) {
        this.id = id;
        this.date = date;
        this.image_src = image_src;
        this.text = text;
    }

    public News() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getImage_src() {
        return image_src;
    }

    public void setImage_src(String image_src) {
        this.image_src = image_src;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
