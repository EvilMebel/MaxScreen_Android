package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;

/**
 * Created by Evil on 2015-05-11.
 */
public class OpenMovieInfoEventBus {
    private int idMovie=-1;
    private Movie movie;

    public OpenMovieInfoEventBus(int idMovie, Movie movie) {
        this.idMovie = idMovie;
        this.movie = movie;
    }

    public int getIdMovie() {
        return idMovie;
    }

    public Movie getMovie() {
        return movie;
    }
}
