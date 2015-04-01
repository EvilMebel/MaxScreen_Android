package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.google.common.eventbus.EventBus;


public class Happen extends Application {
    private static Context sContext;
    private static EventBus sBus;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("App","initailize app");
        sContext = getApplicationContext();
        sBus = new EventBus("five");

        /*ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        ImageLoader.getInstance().init(config);*///TODO

        //prepare local database
        //SimpleDB.initSharedPreferences(getApplicationContext());

    }

    public static Context getContext() {
        return sContext;
    }

    public static EventBus getBus() {
        return sBus;
    }
}
