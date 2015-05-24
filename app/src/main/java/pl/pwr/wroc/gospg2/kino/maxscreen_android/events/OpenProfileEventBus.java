package pl.pwr.wroc.gospg2.kino.maxscreen_android.events;

/**
 * Created by Evil on 2015-05-24.
 */
public class OpenProfileEventBus {
    private int customerId;
    private long facebookId;

    public OpenProfileEventBus(int customerId) {
        this.customerId = customerId;
        facebookId=-1;
    }

    public OpenProfileEventBus(long facebookId) {
        this.facebookId = facebookId;
        customerId = -1;
    }

    public int getCustomerId() {
        return customerId;
    }

    public long getFacebookId() {
        return facebookId;
    }
}
