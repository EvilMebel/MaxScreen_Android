package pl.pwr.wroc.gospg2.kino.maxscreen_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.service.textservice.SpellCheckerService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreference;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;

/**
 * Created by Evil on 2015-05-04.
 */
public class Utils {

    public static boolean isIsLoggedIn() {
        return isLoggedInDatabase() || isLoggedInFacebook();
    }

    public static boolean isLoggedInFacebook() {
        boolean gotToken = AccessToken.getCurrentAccessToken() != null;
        Profile profile = Profile.getCurrentProfile();
        return gotToken && profile != null;
    }

    public static boolean isLoggedInDatabase() {
        ApplicationPreferences preference = ApplicationPreferences.getInstance();
        return preference.getBoolean(ApplicationPreference.LOGGED_IN_CLASSIC);

    }


    public static boolean isDownloadManagerAvailable() {
        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
                return false;
            }
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
            List<ResolveInfo> list = MaxScreen.getContext().getPackageManager().queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            return list.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public static DisplayMetrics getDeviceMetrics(Activity a) {
        DisplayMetrics metrics = new DisplayMetrics();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            a.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
        } else {
            a.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }

        return metrics;
    }

    public static String MD5(String md5) {
        try { java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i){
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            } return sb.toString();
        }
        catch (java.security.NoSuchAlgorithmException e) { }
        return null;
    }

    public static void showAsyncError(Context context, int statusCode, Throwable error,
                                      String content) {

        // When Http response code is '404'
        if (statusCode == 404) {
            Toast.makeText(context, "Requested resource not found", Toast.LENGTH_LONG).show();
        }
        // When Http response code is '500'
        else if (statusCode == 500) {
            Toast.makeText(context, "Something went wrong at server end", Toast.LENGTH_LONG).show();
        }
        // When Http response code other than 404, 500
        else {
            Log.e("ShowAsyncError", "ERROR:" + error.getMessage());
            Toast.makeText(context, statusCode + "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
        }

    }

    public static Collection<String> getFbReadPermissions() {
        List<String> fbPermissions = new ArrayList<String>();

        fbPermissions.add("user_friends");
        //fbPermissions.add("publish_actions");
        fbPermissions.add("email");

        return fbPermissions;
    }
}
