package pl.pwr.wroc.gospg2.kino.maxscreen_android;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.ShareDialog;
import com.google.common.eventbus.Subscribe;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.LoadingDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.enums.PendingAction;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.AfterLoginEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.EditAccountEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.FinishReservationEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToLoginBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToRegistrationBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenCalendarEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenMovieInfoEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenNewsEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenProfileEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenRoomEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.CalendarFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.EditAccountFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.FinishReservationFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.LoginFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.MainFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.MovieInfoFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.MyReservationFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.ProfileFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.PromotionsFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.RegisterFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments.ReservationRoomFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreference;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.FragmentFrame;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
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

    //drawer
    @InjectView (R.id.facebook_user_text)
    TextView mUserText;

    //drawer options
    @InjectView (R.id.my_profile)
    View drawerMyProfile;

    @InjectView (R.id.promotions)
    View drawerPromotions;

    @InjectView (R.id.my_reservations)
    View drawerMyReservations;

    @InjectView (R.id.contact)
    View drawerContact;

    @InjectView (R.id.login_out)
    View drawerLoginLogout;

    @InjectView (R.id.login_out_text)
    TextView drawerLoginLogoutText;

    @InjectView (R.id.calendar)
    View drawerCalendar;

    private Fragment mFragment;
    private Fragment mSecondFragment;
    private String FRAGMENT_TAG_MAIN_NEWS = "FRAGMENT_TAG_MAIN_NEWS";
    private String FRAGMENT_TAG_EDIT_ACC = "FRAGMENT_TAG_EDIT_ACC";
    private String FRAGMENT_TAG_MY_RESERVATION = "FRAGMENT_TAG_MY_RESERVATION";
    private String FRAGMENT_TAG_MY_PROFILE = "FRAGMENT_TAG_MY_PROFILE";
    private String FRAGMENT_TAG_MY_PROMOTIONS = "FRAGMENT_TAG_MY_PROMOTIONS";
    private String FRAGMENT_TAG_CALENDAR = "FRAGMENT_TAG_CALENDAR";
    private String FRAGMENT_TAG_MOVIE_INFO = "FRAGMENT_TAG_MOVIE_INFO";
    private String FRAGMENT_TAG_RESERVATION = "FRAGMENT_TAG_RESERVATION";//Z WIDOKIEM SALI
    private String FRAGMENT_TAG_FINISH_RESERVATION = "FRAGMENT_TAG_FINISH_RESERVATION";
    private String FRAGMENT_TAG_CONTACT= "FRAGMENT_TAG_CONTACT";
    private String FRAGMENT_TAG_LOGIN = "FRAGMENT_TAG_LOGIN";
    private String FRAGMENT_TAG_REGISTER = "FRAGMENT_TAG_REGISTER";

    List<FragmentFrame> fragmentsStack;

    @InjectView (R.id.profilePicture)
    private ProfilePictureView profilePictureView;

    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private ShareDialog shareDialog;

    private PendingAction pendingAction = PendingAction.NONE;
    private static final String tag = "FBactions";
    private LoadingDialogFragment mLoadingDialogFragment;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentsStack = new ArrayList<FragmentFrame>();

        FacebookSdk.sdkInitialize(getApplicationContext());
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));

        callbackManager = CallbackManager.Factory.create();
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //showLoadingDialog();
                updateUI();
                // It's possible that we were waiting for Profile to be populated in order to
                // post a status update.

                getProfileData(currentProfile);

                //GraphRequest.newMeRequest(LoginManager.getInstance().)
            }
        };

        updateUI();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        MSData.getInstance().setAccessToken(loginResult.getAccessToken());
                        handlePendingAction();
                        updateUI();
                        log("Zalogowany! - onSuccess");



                        //todo hide login fragment
                        commitMainFragment();

                        //todo polacz z baza u utworz konto uzytkownika
                        //todo przekieruj do wypelnienia danych
                    }

                    @Override
                    public void onCancel() {
                        if (pendingAction != PendingAction.NONE) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                        log("Wstecz! - onCancel");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (pendingAction != PendingAction.NONE
                                && exception instanceof FacebookAuthorizationException) {
                            showAlert();
                            pendingAction = PendingAction.NONE;
                        }
                        updateUI();
                        log("BLAD! - onError");
                    }

                    private void showAlert() {
                        new AlertDialog.Builder(MainActivity.this)
                                .setTitle(R.string.cancelled)
                                .setMessage(R.string.permission_not_granted)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                });

        setDrawerLayout();
        commitMainFragment();
    }

    private void getProfileData(Profile currentProfile) {
        Profile profile = currentProfile;
        if(profile!=null) {
            log("Profile!=null!");
            //log("email=" + profile.ge);
            AccessToken accessToken = MSData.getInstance().getAccessToken();
            if(accessToken!=null) {
                log("accessToken!=null");
                //LoginManager.getInstance().logInWithReadPermissions(this,Utils.getFbReadPermissions());
                GraphRequest request = createRequestForProfileData();
                request.setCallback(new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        log("graphResponse="+graphResponse);
                        log("graphResponse="+graphResponse.getRawResponse());
                        JSONObject jsonObject = graphResponse.getJSONObject();
                        try {
                            String name = jsonObject.getString("first_name");
                            String surname = jsonObject.getString("last_name");
                            String fbId = jsonObject.getString("id");
                            String email = jsonObject.getString("email");//todo



                            log("Try register Facebook!");
                            tryRegisterFacebook(name,surname,fbId,email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FBactions", "ERROR register!");
                            failedFacebookLogin();
                        }

                    }
                });
                request.executeAsync();

                /*   //todo old version - email error!
                GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {
                        log("json="+jsonObject);
                        log("graphResponse="+graphResponse);
                        log("graphResponse="+graphResponse.getRawResponse());
                        try {
                            String name = jsonObject.getString("first_name");
                            String surname = jsonObject.getString("last_name");
                            String fbId = jsonObject.getString("id");
                            String email = jsonObject.getString("email");//todo



                            log("Try register Facebook!");
                            tryRegisterFacebook(name,surname,fbId,email);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.e("FBactions", "ERROR register!");
                            failedFacebookLogin();
                        }

                    }
                }).executeAsync();*/

            } else {
                hideLoadingDialog();
                log("token is NULL!");

            }

        } else {
            log("Profile == NULL!");
            MSData.getInstance().setAccessToken(null);
            hideLoadingDialog();
        }
    }

    private GraphRequest createRequestForProfileData() {
        HashSet<String> extraFields = new HashSet<String>();
        extraFields.add("email");
        extraFields.add("first_name");
        extraFields.add("last_name");
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, "/v2.3/me", null);

        Set<String> fields = new HashSet<String>(extraFields);
        String[] requiredFields = new String[]{
                "id",
                "name"
        };
        fields.addAll(Arrays.asList(requiredFields));

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        return request;
    }



    private void showLoadingDialog() {
        mLoadingDialogFragment = new LoadingDialogFragment();
        mLoadingDialogFragment.show(getSupportFragmentManager(), null);
    }

    private void hideLoadingDialog() {
        if(mLoadingDialogFragment!=null)
            mLoadingDialogFragment.dismiss();
    }

    private void tryRegisterFacebook(String name, String surname, String fbId, String email) {
        RequestParams params = new RequestParams();

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/customer/registerfb/"+email+ "/"+ name+"/" + surname+"/"+fbId;
        Log.d(tag, "REGISTERF!" + link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //todfokjfgnbfkjn
                Log.d("MainActivity", "response:" + response);

                int status = -1;
                String msg = "";
                Customers c = null;

                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getInt(Customers.IDCUSTOMER);

                    //error msg is in name
                    if (status == -1) {
                        msg = object.getString(Customers.NAME);
                    } else {
                        c = Customers.parseEntity(object);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    c = null;
                }


                if (c != null) {
                    Toast.makeText(getApplicationContext(), "Witamy " + c.getName() + "\n" + c.getSurname(), Toast.LENGTH_SHORT).show();
                    ApplicationPreferences.getInstance().setCurrentCustomer(c);

                } else {
                    failedFacebookLogin();
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }


                hideLoadingDialog();
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                failedFacebookLogin();
                Utils.showAsyncError(getApplicationContext(), statusCode, error, content);
            }
        });

    }

    private void failedFacebookLogin() {
        Toast.makeText(getApplicationContext(),getString(R.string.login_error),Toast.LENGTH_SHORT).show();
        LoginManager.getInstance().logOut();
        updateUI();
        hideLoadingDialog();
    }

    private void log(String s) {
        Log.d(tag, s);
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
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
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

                commitProfileMyFragment();
                closeDrawer();
                break;
            case R.id.promotions:
                commitPromotionsFragment();
                closeDrawer();

                break;
            case R.id.my_reservations:
                commitMyReservationsFragment();
                closeDrawer();

                break;
            case R.id.contact:

                break;
            case R.id.login_out:
                if(Utils.isIsLoggedIn()) {
                    //todo logout
                    if(Utils.isLoggedInFacebook()) {
                        LoginManager.getInstance().logOut();
                    } else {
                        ApplicationPreferences preference = ApplicationPreferences.getInstance();
                        preference.setBoolean(ApplicationPreference.LOGGED_IN_CLASSIC,false);
                    }
                    ApplicationPreferences.getInstance().setCurrentCustomer(null);
                    updateUI();
                } else {
                    commitLoginFragment();
                }
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

    private void updateUI() {
        updateDrawerUI();
    }

    private void updateDrawerUI() {
        if(Utils.isIsLoggedIn()) {
            Profile profile = Profile.getCurrentProfile();

            drawerMyProfile.setVisibility(View.VISIBLE);
            drawerLoginLogout.setVisibility(View.VISIBLE);
            drawerLoginLogoutText.setText("Wyloguj");

            if(Utils.isLoggedInFacebook()) {
                profilePictureView.setProfileId(profile.getId());
                mUserText.setText(profile.getFirstName() + " " + profile.getLastName());
            } else {
                mUserText.setText("Witamy ponownie! - baza");//todo logging trough database
            }
        } else {
            drawerMyProfile.setVisibility(View.GONE);
            drawerLoginLogoutText.setText("Zaloguj");

            profilePictureView.setProfileId(null);
            mUserText.setText(null);
        }
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;
        // These actions may re-set pendingAction if they are still pending, but we assume they
        // will succeed.
        pendingAction = PendingAction.NONE;

        switch (previouslyPendingAction) {
            case NONE:
                break;
            case POST_PHOTO:
                //todo postPhoto();
                break;
            case POST_STATUS_UPDATE:
                //todo postStatusUpdate();
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

        //ft.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in);

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MAIN_NEWS) == null) {

            Fragment mFragment = new MainFragment();
            //fragmentsStack.add(new FragmentFrame(mFragment,FRAGMENT_TAG_MAIN_NEWS));

            //ft.add(mFragment,FRAGMENT_TAG_MAIN_NEWS).addToBackStack(null);
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

    private void commitRoomFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new ReservationRoomFragment();
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_RESERVATION) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_RESERVATION).commit();
        }

        mTitle.setText("Widok sali kinowej");
        enableBackButton();
    }

    private void commitPromotionsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new PromotionsFragment();
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MY_PROMOTIONS) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MY_PROMOTIONS).commit();
        }

        mTitle.setText("Promocje");
        enableBackButton();
    }

    private void commitProfileMyFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = ProfileFragment.newInstance(true,-1);
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MY_PROFILE) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MY_PROFILE).commit();
        }

        mTitle.setText("Moj profil");
        enableBackButton();
    }

    private void commitProfileFragment(OpenProfileEventBus bus) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        String endTag;
        if(bus.getCustomerId()!=-1) {
            mFragment = ProfileFragment.newInstance(false, bus.getCustomerId());
            endTag = "id"+bus.getCustomerId();
        } else {
            mFragment = ProfileFragment.newInstance(bus.getFacebookId());
            endTag = "fb"+bus.getFacebookId();
        }
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MY_PROFILE+endTag) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MY_PROFILE+endTag).commit();
        }

        mTitle.setText("Moj profil");
        enableBackButton();
    }

    private void commitMovieInfoFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new MovieInfoFragment();

        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MOVIE_INFO) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MOVIE_INFO).commit();
        }

        mTitle.setText("Info filmu");
        enableBackButton();
    }

    private void commitMyReservationsFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = new MyReservationFragment();

        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_MY_RESERVATION) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_MY_RESERVATION).commit();
        }

        mTitle.setText("Moje rezerwacje");
        enableBackButton();
    }

    private void commitFinishReservation(int reservationId, String tickets) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mFragment = FinishReservationFragment.newInstance(reservationId,tickets);

        //TODO ADD PARAMS
        //ft.setCustomAnimations(android.R.anim.fade_in,android.R.anim.fade_out);
        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_FINISH_RESERVATION) == null) {
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_FINISH_RESERVATION).commit();
        }

        mTitle.setText("Podsumowanie rezerwacji");
        enableBackButton();
    }

    private void commitEditAccount() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        //ft.setCustomAnimations(android.R.anim.fade_out, android.R.anim.fade_in);

        if (getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG_EDIT_ACC) == null) {

            Fragment mFragment = new EditAccountFragment();
            //fragmentsStack.add(new FragmentFrame(mFragment,FRAGMENT_TAG_MAIN_NEWS));

            //ft.add(mFragment,FRAGMENT_TAG_MAIN_NEWS).addToBackStack(null);
            ft.replace(R.id.content_frame1, mFragment, FRAGMENT_TAG_EDIT_ACC).commit();
        }//.setCustomAnimations(android.R.anim.fade_out,android.R.anim.fade_in) //TODO
        mTitle.setText(R.string.news);
        enableMenuButton();
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

    @Subscribe
    public void openRoom(OpenRoomEventBus bus) {
        commitRoomFragment();
    }

    @Subscribe
    public void openNews(OpenNewsEventBus bus) {
        commitMainFragment();
    }

    @Subscribe
    public void editAccount(EditAccountEventBus bus) {
        commitEditAccount();
    }

    @Subscribe
    public void openCalendar(OpenCalendarEventBus bus) {
        commitCalendarFragment();
    }





    @Subscribe
    public void openMovieInfo(OpenMovieInfoEventBus bus) {
        //save data in singleton
        if(bus.getIdMovie()!=-1) {
            Movie movie = new Movie();
            movie.setIdMovie(bus.getIdMovie());
            MSData.getInstance().setCurrentMovie(movie);
        }
        if(bus.getMovie()!=null) {
            MSData.getInstance().setCurrentMovie(bus.getMovie());
        }

        commitMovieInfoFragment();
    }

    @Subscribe
    public void afterLogin(AfterLoginEventBus bus) {
        commitMainFragment();
        updateUI();
    }

    @Subscribe
    public void finishReservation(FinishReservationEventBus bus) {
        commitFinishReservation(bus.getReservationId(), bus.getTickets());
    }

    @Subscribe
    public void openProfile(OpenProfileEventBus bus) {
        updateUI();
        commitProfileFragment(bus);
    }





}
