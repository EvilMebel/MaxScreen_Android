package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;

/**
 * Created by Evil on 2015-05-20.
 */
public class ChoosedReliefEventBus {
    private Relief relief;

    public ChoosedReliefEventBus(Relief relief) {
        this.relief = relief;
    }

    public Relief getRelief() {
        return relief;
    }
}
