package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

/**
 * Created by Evil on 2015-05-20.
 */
public class FinishReservationEventBus {
    private int reservationId;
    private String tickets;

    public FinishReservationEventBus(int reservationId, String tickets) {
        this.reservationId = reservationId;
        this.tickets = tickets;
    }

    public int getReservationId() {
        return reservationId;
    }

    public String getTickets() {
        return tickets;
    }
}
