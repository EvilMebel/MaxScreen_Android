package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.common.eventbus.EventBus;

import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupons;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Tickets;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.LruBitmapCache;


public class MaxScreen extends Application {
    private static Context sContext;
    private static EventBus sBus;


    //valley
    public static final String TAG = MaxScreen.class
            .getSimpleName();
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static MaxScreen mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("App","initailize app");
        sContext = getApplicationContext();
        sBus = new EventBus("MaxScreen");

        mInstance = this;
    }

    public static Context getContext() {
        return sContext;
    }

    public static EventBus getBus() {
        return sBus;
    }


    /*
                        VALLEY
     */
    public static synchronized MaxScreen getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }


    public ImageLoader getImageLoader() {
        getRequestQueue();
        if (mImageLoader == null) {
            mImageLoader = new ImageLoader(this.mRequestQueue,
                    new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }
}
