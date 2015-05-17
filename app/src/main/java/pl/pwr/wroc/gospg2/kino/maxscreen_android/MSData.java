package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.graphics.Bitmap;

import com.facebook.AccessToken;

import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupons;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Tickets;


public class MSData {
    private static MSData msData;

    //data
    private Movie currentMovie;
    private List<Tickets> tickets;
    private Coupons coupon;
    private Halls room;
    private Seance seance;
    private AccessToken accessToken;

    private MSData() { }

    public static MSData getInstance() {
        if(msData == null) {
            msData = new MSData();
        }

        return  msData;
    }


    public Movie getCurrentMovie() {
        return currentMovie;
    }

    public void setCurrentMovie(Movie currentMovie) {
        this.currentMovie = currentMovie;
    }

    public List<Tickets> getTickets() {
        return tickets;
    }

    public void setTickets(List<Tickets> tickets) {
        this.tickets = tickets;
    }

    public Coupons getCoupon() {
        return coupon;
    }

    public void setCoupon(Coupons coupon) {
        this.coupon = coupon;
    }

    public Halls getRoom() {
        return room;
    }

    public void setRoom(Halls room) {
        this.room = room;
    }

    public Seance getSeance() {
        return seance;
    }

    public void setSeance(Seance seance) {
        this.seance = seance;
    }

    public void setAccessToken(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public AccessToken getAccessToken() {
        return accessToken;
    }
}
