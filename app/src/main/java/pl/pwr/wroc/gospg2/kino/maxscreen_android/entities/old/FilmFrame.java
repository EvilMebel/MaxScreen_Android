package pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.old;

import java.sql.Time;
import java.util.ArrayList;

/**
 * Created by Evil on 2015-04-01.
 */
public class FilmFrame {
    private ArrayList<SSeans> seanses;
    private int idMovie;
    private String imagePath;

    public FilmFrame(ArrayList<SSeans> seanses) {
        this.seanses = seanses;
    }

    public FilmFrame() {
    }

    public ArrayList<SSeans> getSeanses() {
        return seanses;
    }

    public void setSeanses(ArrayList<SSeans> seanses) {
        this.seanses = seanses;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(int idMovie) {
        this.idMovie = idMovie;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public static class SSeans {
        public int id;
        public Time time;
    }
}
