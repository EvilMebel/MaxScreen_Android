package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.View;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import roboguice.fragment.RoboFragment;


public class RoboEventFragment extends RoboFragment {


    @Override
    public void onStart() {
        super.onStart();

        MaxScreen.getBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();

        MaxScreen.getBus().unregister(this);
    }
}
