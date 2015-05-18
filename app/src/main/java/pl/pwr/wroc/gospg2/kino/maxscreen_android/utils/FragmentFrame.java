package pl.pwr.wroc.gospg2.kino.maxscreen_android.utils;

import android.support.v4.app.Fragment;

/**
 * Created by Evil on 2015-05-18.
 */
public class FragmentFrame {
    public Fragment fragment;
    public String tag;

    public FragmentFrame(Fragment fragment, String tag) {
        this.fragment = fragment;
        this.tag = tag;
    }
}
