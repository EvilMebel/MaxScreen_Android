package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.ListDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenNewsEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreference;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.HorizontalListView;
import roboguice.inject.InjectView;


public class ProfileFragment extends RoboEventFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_MY_PROFILE = "ARG_PARAM_MY_PROFILE";
    private static final String ARG_PARAM_USER_ID = "ARG_PARAM_USER_ID";
    private static final String ARG_PARAM_FACEBOOK_ID = "ARG_PARAM_FACEBOOK_ID";

    // TODO: Rename and change types of parameters
    private boolean mMyProfle;
    private int mUserId = -1;
    private long mFacebookId;


    @InjectView (R.id.profilePicture)
    ProfilePictureView mAvatar;

    @InjectView (R.id.username)
    TextView mUsername;

    @InjectView (R.id.prof_friends)
    Button mFriends;

    @InjectView (R.id.prof_seen_movies)
    Button mSeenMovies;

    @InjectView (R.id.prof_wanted_movies)
    Button mWantedMovies;
    private Customers currentCustomer;
    private AsyncHttpClient client;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isMyProfile Parameter 1.
     * @param userId Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(boolean isMyProfile, int userId) {
        Log.d("getInstance","userId="+userId);
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_MY_PROFILE, isMyProfile);
        args.putInt(ARG_PARAM_USER_ID, userId);
        args.putLong(ARG_PARAM_FACEBOOK_ID, -1);
        fragment.setArguments(args);
        return fragment;
    }

    public static ProfileFragment newInstance(long facebookId) {
        Log.d("getInstance","fbId="+facebookId);
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        //args.putBoolean(ARG_PARAM_MY_PROFILE, isMyProfile);
        //args.putInt(ARG_PARAM_USER_ID, userId);
        args.putLong(ARG_PARAM_FACEBOOK_ID, facebookId);
        args.putInt(ARG_PARAM_USER_ID, -1);
        fragment.setArguments(args);
        return fragment;
    }

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMyProfle = getArguments().getBoolean(ARG_PARAM_MY_PROFILE);
            mUserId = getArguments().getInt(ARG_PARAM_USER_ID);
            mFacebookId = getArguments().getLong(ARG_PARAM_FACEBOOK_ID);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getData();
        setListeners();
    }

    private void setListeners() {
        mWantedMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment dialog = new ListDialogFragment();
                dialog.setAdapterType(ListDialogFragment.AdapterType.MOVIES);
                dialog.setCurrentUser(currentCustomer);
                dialog.show(getFragmentManager(), null);
            }
        });

        mSeenMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListDialogFragment dialog = new ListDialogFragment();
                dialog.setAdapterType(ListDialogFragment.AdapterType.COMMENTED_MOVIES);
                dialog.setCurrentUser(currentCustomer);
                dialog.show(getFragmentManager(), null);
            }
        });

        mFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentCustomer.getFacebookId() != null) {
                    ListDialogFragment dialog = new ListDialogFragment();
                    dialog.setAdapterType(ListDialogFragment.AdapterType.FACEBOOK_FRIENDS);
                    dialog.setCurrentUser(currentCustomer);
                    dialog.show(getFragmentManager(), null);
                }
            }
        });
    }

    private void getData() {
        Customers c = ApplicationPreferences.getInstance().getCurrentCustomer();
        if(c!=null) {
            if(mFacebookId!=-1 && c.getFacebookId().equals(Long.toString(mFacebookId))) {
                mMyProfle = true;
            } else if(mUserId == c.getIdCustomer()) {
                mMyProfle = true;
            }
        }

        if(mMyProfle) {
            currentCustomer = ApplicationPreferences.getInstance().getCurrentCustomer();

            if(Utils.isLoggedInFacebook()) {
                Profile profile = Profile.getCurrentProfile();
                //load avatar etc
                mAvatar.setProfileId(profile.getId());


            } else {
                mFriends.setVisibility(View.GONE);
            }


            if(currentCustomer!=null) {
                mUsername.setText(currentCustomer.getName()+" "+ currentCustomer.getSurname());
                mUsername.setVisibility(View.VISIBLE);
                //get user by facebook id
                if(currentCustomer.getFacebookId()!=null)
                    mFriends.setVisibility(View.VISIBLE);
            } else {
                mUsername.setVisibility(View.GONE);
            }


        } else {
            //todo get user data by Id - REST

            if(currentCustomer == null) {

                if(mUserId!=-1) {
                    downloadUser(mUserId);
                } else if(mFacebookId!=-1) {
                    downloadUser(mFacebookId);

                } else {
                    MaxScreen.getBus().post(new OpenNewsEventBus());
                }


            } else {
                //downloaded! : D
                mUsername.setText(currentCustomer.getName()+" "+ currentCustomer.getSurname());

                if(currentCustomer.getFacebookId()!=null) {
                    mAvatar.setProfileId(currentCustomer.getFacebookId());//todo bug?
                    mFriends.setVisibility(View.VISIBLE);
                }
            }

            //get user by id
        }


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(client!=null) {
            client.cancelRequests(getActivity(),true);
        }
    }


    private void downloadUser(int customerId) {
        //todo loading
        RequestParams params = new RequestParams();

        // Make RESTful webservice call using AsyncHttpClient object
        client = new AsyncHttpClient();
        String link = Net.dbIp + "/customer/"+customerId + "?";
        Log.d(getTag(), "GET!" + link);
        client.get(link, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                super.onSuccess(statusCode, headers, responseBody);
            }

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //todfokjfgnbfkjn
                Log.d(getTag(), "response:" + response);
                currentCustomer = null;

                try {
                    JSONObject object = new JSONObject(response);
                    if (object.has(Customers.IDCUSTOMER) && object.getInt(Customers.IDCUSTOMER) != -1) {
                        currentCustomer = Customers.parseEntity(object);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    currentCustomer = null;
                }


                if (currentCustomer != null) {
                    getData();//refresh!
                    Toast.makeText(getActivity(), "user wczytany -normal! "+currentCustomer, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_parsing_users), Toast.LENGTH_SHORT).show();
                }

            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }

    private void downloadUser(long facebookId) {
        //todo loading
        RequestParams params = new RequestParams();

        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/customer/fb/"+facebookId + "?";
        Log.d(getTag(), "GET!" + link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //todfokjfgnbfkjn
                Log.d(getTag(), "response:" + response);
                currentCustomer = null;

                try {
                    JSONObject object = new JSONObject(response);
                    if(object.has(Customers.IDCUSTOMER) && object.getInt(Customers.IDCUSTOMER)!=-1) {
                        currentCustomer = Customers.parseEntity(object);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    currentCustomer = null;
                }


                if (currentCustomer != null) {
                    getData();//refresh!
                    Toast.makeText(getActivity(), "user wczytany-fb! "+currentCustomer, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.error_parsing_users), Toast.LENGTH_SHORT).show();
                }

            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }



}
