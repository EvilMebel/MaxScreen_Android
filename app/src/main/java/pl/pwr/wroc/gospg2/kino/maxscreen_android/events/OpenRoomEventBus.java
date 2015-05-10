package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;

/**
 * Created by Evil on 2015-05-09.
 */
public class OpenRoomEventBus {
    private Movie movie;
    private Seance seance;

    public OpenRoomEventBus(Movie movie, Seance seance) {
        this.movie = movie;
        this.seance = seance;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }
}
