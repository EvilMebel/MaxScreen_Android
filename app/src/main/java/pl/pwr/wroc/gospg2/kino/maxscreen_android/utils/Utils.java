package pl.pwr.wroc.gospg2.kino.maxscreen_android.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.service.textservice.SpellCheckerService;

import com.facebook.AccessToken;
import com.facebook.Profile;

import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;

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
        return false;//todo login through database
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

}
