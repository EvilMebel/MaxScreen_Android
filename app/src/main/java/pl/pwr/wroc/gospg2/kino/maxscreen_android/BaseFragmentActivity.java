package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import roboguice.activity.RoboFragmentActivity;

public class BaseFragmentActivity extends RoboFragmentActivity {
    @Override
    protected void onStart() {
        super.onStart();

        MaxScreen.getBus().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        MaxScreen.getBus().unregister(this);
    }
}
