package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.common.eventbus.Subscribe;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToLoginBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToRegistrationBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.CalendarFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.LoginFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.MainFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.RegisterFragment;
import roboguice.activity.RoboFragmentActivity;
import roboguice.inject.InjectView;


public class MainActivity extends BaseFragmentActivity {

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @InjectView (R.id.left_drawer)
    LinearLayout mDrawerList;

    @InjectView (R.id.content_frame1)
    FrameLayout contentLayout;

    @InjectView (R.id.content_frame2)
    FrameLayout contentLayouT2;

    @InjectView (R.id.title)
    TextView mTitle;

    @InjectView (R.id.back)
    ImageView mBack;

    @InjectView (R.id.menu)
    ImageView mMenu;

    private Fragment mFragment;
    private Fragment mSecondFragment;
    private String FRAGMENT_TAG_MAIN_NEWS = "FRAGMENT_TAG_MAIN_NEWS";
    private String FRAGMENT_TAG_MY_RESERVATION = "FRAGMENT_TAG_MY_RESERVATION";
    private String FRAGMENT_TAG_MY_PROFILE = "FRAGMENT_TAG_MY_PROFILE";
    private String FRAGMENT_TAG_CALENDAR = "FRAGMENT_TAG_CALENDAR";
    private String FRAGMENT_TAG_FILM_INFO = "FRAGMENT_TAG_FILM_INFO";
    private String FRAGMENT_TAG_RESERVATION = "FRAGMENT_TAG_RESERVATION";//Z WIDOKIEM SALI
    private String FRAGMENT_TAG_FINISH_RESERVATION = "FRAGMENT_TAG_FINISH_RESERVATION";
    private String FRAGMENT_TAG_CONTACT= "FRAGMENT_TAG_CONTACT";
    private String FRAGMENT_TAG_LOGIN = "FRAGMENT_TAG_LOGIN";
    private String FRAGMENT_TAG_REGISTER = "FRAGMENT_TAG_REGISTER";


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setDrawerLayout();
        commitMainFragment();
    }

    private void setDrawerLayout() {
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        // mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED); //blokowanie
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerStateChanged(int state) {
            }

            @Override
            public void onDrawerSlide(View drawerView, float offset) {
                float moveBy = (mDrawerList.getWidth() * offset);

                contentLayout.setTranslationX(moveBy);

               /* if (!mImgvMoreOptions.isPressed()) {
                    mImgvMoreOptions.setPressed(true);
                }*/
            }

            @Override
            public void onDrawerOpened(View v) {
                //mImgvMoreOptions.setPressed(true);
            }

            @Override
            public void onDrawerClosed(View v) {
                /*mImgvMoreOptions.setPressed(false);

                if(mOnCloseAction != null) {
                    mOnCloseAction.run();
                }*/
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void buttonClick(View view) {
        switch (view.getId()) {
            case R.id.my_profile:

                break;
            case R.id.promotions:

                break;
            case R.id.my_reservations:

                break;
            case R.id.contact:

                break;
            case R.id.login_out:
                commitLoginFragment();
                closeDrawer();
                break;
            case R.id.calendar://repertuar
                commitCalendarFragment();
                closeDrawer();

                break;


            /*
                    ACTION BAR
             */
            case R.id.back:
                commitMainFragment();
                break;
            case R.id.menu:
                openDrawer();
                break;
            default:
                Log.e(getClass().toString(),"not supported drawer button id=" + + view.getId());
                break;
        }
    }
    
    
    public void openDrawer(View view) {
        openDrawer();
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }

    public void lockDrawer() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public void closeDrawer() {
        mDrawerLayout.closeDrawer(Gravity.LEFT);
    }



    private void enableBackButton() {
        if(mBack.getVisibility()==View.GONE) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            mBack.setAnimation(anim);
            mBack.setVisibility(View.VISIBLE);

            anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            mMenu.setAnimation(anim);
            mMenu.setVisibility(View.GONE);
        }
    }

    private void enableMenuButton() {
        if(mMenu.getVisibility()==View.GONE) {
            Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_in);
            mMenu.setAnimation(anim);
            mMenu.setVisibility(View.VISIBLE);

            anim = AnimationUtils.loadAnimation(getApplicationContext(), android.R.anim.fade_out);
            mBack.setAnimation(anim);
            mBack.setVisibility(View.GONE);
        }

    }

    /*
                    FRAGMENTS
     */

    private void commitMainFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new MainFragment();
        //ft.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in);

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAIN_NEWS) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MAIN_NEWS).commit();
        }//.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_in) //TODO
        mTitle.setText(R.string.news);
        enableMenuButton();
    }

    private void commitLoginFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new LoginFragment();
        //ft.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_in);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_LOGIN) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_LOGIN).commit();
        }
        mTitle.setText(R.string.login);
        enableBackButton();
    }

    private void commitRegisterFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new RegisterFragment();
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_REGISTER) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_REGISTER).commit();
        }
        mTitle.setText(R.string.registration);
        enableBackButton();
    }

    private void commitCalendarFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new CalendarFragment();
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_CALENDAR) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_CALENDAR).commit();
        }

        mTitle.setText(R.string.calendar);
        enableBackButton();
    }

    /*
                        EVENT BUS
     */
    @Subscribe
    public void openRegistration(GoToRegistrationBus bus) {
        commitRegisterFragment();
    }

    @Subscribe
    public void openLogin(GoToLoginBus bus) {
        commitLoginFragment();
    }


}
