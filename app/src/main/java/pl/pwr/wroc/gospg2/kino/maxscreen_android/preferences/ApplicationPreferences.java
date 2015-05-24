package pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;


public class ApplicationPreferences {
    private static ApplicationPreferences sManager;

    private HashMap<ApplicationPreference, Boolean> defaultsHashMap = new HashMap<ApplicationPreference, Boolean>();

    private ApplicationPreferences() {
        defaultsHashMap.put(ApplicationPreference.IS_FIRST_RUN, DefaultApplicationPreferences.IS_FIRST_RUN);
        defaultsHashMap.put(ApplicationPreference.LOGGED_IN_CLASSIC, DefaultApplicationPreferences.LOGGED_IN_CLASSIC);
/*
        defaultsHashMap.put(ApplicationPreference.USER_IDCUSTOMER, DefaultApplicationPreferences.USER_IDCUSTOMER);
        defaultsHashMap.put(ApplicationPreference.USER_NAME, DefaultApplicationPreferences.USER_NAME);
        defaultsHashMap.put(ApplicationPreference.USER_SURNAME, DefaultApplicationPreferences.USER_SURNAME);
        defaultsHashMap.put(ApplicationPreference.USER_E_MAIL, DefaultApplicationPreferences.USER_E_MAIL);
        defaultsHashMap.put(ApplicationPreference.USER_TELEFON, DefaultApplicationPreferences.USER_TELEFON);
        defaultsHashMap.put(ApplicationPreference.USER_NICK, DefaultApplicationPreferences.USER_NICK);
        defaultsHashMap.put(ApplicationPreference.USER_PASSMD5, DefaultApplicationPreferences.USER_PASSMD5);
        defaultsHashMap.put(ApplicationPreference.USER_FACEBOOKID, DefaultApplicationPreferences.USER_FACEBOOKID);
        defaultsHashMap.put(ApplicationPreference.USER_TOKEN, DefaultApplicationPreferences.USER_TOKEN);
        defaultsHashMap.put(ApplicationPreference.USER_AVATAR, DefaultApplicationPreferences.USER_AVATAR);
        */

    }

    public static ApplicationPreferences getInstance() {
        if (sManager == null) {
            sManager = new ApplicationPreferences();
        }
        return sManager;
    }

    public void setBoolean(ApplicationPreference p, Boolean value) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putBoolean(p.toString(), value);
        editor.commit();
    }

    public Boolean getBoolean(ApplicationPreference p) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

        return settings.getBoolean(p.toString(), defaultsHashMap.get(p));
    }


    public void setString(ApplicationPreference p, String value) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString(p.toString(), value);
        editor.commit();
    }

    public String getString(ApplicationPreference p) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

        return settings.getString(p.toString(), null);
    }

    public void setFloat(ApplicationPreference p, float value) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putFloat(p.toString(), value);
        editor.commit();
    }

    public float getFloat(ApplicationPreference p) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

        return settings.getFloat(p.toString(), -1);
    }


    public void setInt(ApplicationPreference p, int value) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        editor.putInt(p.toString(), value);
        editor.commit();
    }

    public int getInt(ApplicationPreference p) {
        Context context = MaxScreen.getContext();
        SharedPreferences settings = context.getSharedPreferences(DefaultApplicationPreferences.PREFERENCES_NAME, Context.MODE_PRIVATE);

        return settings.getInt(p.toString(), 0);
    }

    public void setCurrentCustomer(Customers c) {
        if(c!=null) {
            setInt(ApplicationPreference.USER_IDCUSTOMER, c.getIdCustomer());
            setString(ApplicationPreference.USER_AVATAR, c.getAvatar());
            setString(ApplicationPreference.USER_FACEBOOKID, c.getFacebookId());
            setString(ApplicationPreference.USER_E_MAIL, c.getE_Mail());
            setString(ApplicationPreference.USER_NAME, c.getName());
            setString(ApplicationPreference.USER_NICK, c.getNick());
            setString(ApplicationPreference.USER_SURNAME, c.getSurname());
            setString(ApplicationPreference.USER_TELEFON, c.getTelefon());
            setString(ApplicationPreference.USER_TOKEN, c.getToken());
        } else {
            resetCurrentCustomer();
        }
    }

    public void resetCurrentCustomer() {
        setInt(ApplicationPreference.USER_IDCUSTOMER, -1);
        setString(ApplicationPreference.USER_AVATAR, null);
        setString(ApplicationPreference.USER_FACEBOOKID, null);
        setString(ApplicationPreference.USER_E_MAIL,null);
        setString(ApplicationPreference.USER_NAME, null);
        setString(ApplicationPreference.USER_NICK, null);
        setString(ApplicationPreference.USER_SURNAME, null);
        setString(ApplicationPreference.USER_TELEFON, null);
        setString(ApplicationPreference.USER_TOKEN, null);
    }

    public Customers getCurrentCustomer() {
        Customers c = null;
        int index = getInt(ApplicationPreference.USER_IDCUSTOMER);

        if(index!=-1) {
            c = new Customers();
            c.setIdCustomer(index);
            c.setAvatar(getString(ApplicationPreference.USER_AVATAR));
            c.setFacebookId(getString(ApplicationPreference.USER_FACEBOOKID));
            c.setE_Mail(getString(ApplicationPreference.USER_E_MAIL));
            c.setName(getString(ApplicationPreference.USER_NAME));
            c.setNick(getString(ApplicationPreference.USER_NICK));
            c.setSurname(getString(ApplicationPreference.USER_SURNAME));
            c.setTelefon(getString(ApplicationPreference.USER_TELEFON));
            c.setToken(getString(ApplicationPreference.USER_TOKEN));
        }

        return c;


    }

}
