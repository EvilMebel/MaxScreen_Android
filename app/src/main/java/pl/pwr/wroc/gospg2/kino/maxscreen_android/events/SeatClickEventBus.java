package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.SeatView;

/**
 * Created by Evil on 2015-05-09.
 */
public class SeatClickEventBus {
    private SeatView seatView;

    public SeatClickEventBus(SeatView seatView) {
        this.seatView = seatView;
    }

    public SeatView getSeatView() {
        return seatView;
    }
}
