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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.ListDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.HorizontalListView;
import roboguice.inject.InjectView;


public class ProfileFragment extends RoboEventFragment {

    private static final String ID = "id";
    private static final String NAME = "name";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_MY_PROFILE = "ARG_PARAM_MY_PROFILE";
    private static final String ARG_PARAM_USER_ID = "ARG_PARAM_USER_ID";

    // TODO: Rename and change types of parameters
    private boolean mMyProfle;
    private String mUserId;


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






    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param isMyProfile Parameter 1.
     * @param userId Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(boolean isMyProfile, String userId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM_MY_PROFILE, isMyProfile);
        args.putString(ARG_PARAM_USER_ID, userId);
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
            mUserId = getArguments().getString(ARG_PARAM_USER_ID);
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
                dialog.show(getFragmentManager(),null);
            }
        });
    }

    private void getData() {
        if(mMyProfle) {
            if(Utils.isLoggedInFacebook()) {
                Profile profile = Profile.getCurrentProfile();
                //load avatar etc
                mAvatar.setProfileId(profile.getId());
                mUsername.setText(profile.getFirstName() + " " + profile.getLastName());
                //get user by facebook id

                getFacebookFriends();


            } else {
                mFriends.setVisibility(View.GONE);

            }
        } else {
            //todo get user data by Id - REST

            //get user by id
        }


    }

    private void getFacebookFriends() {
        HashSet<String> extraFields = new HashSet<String>();
        //todo nothing more than id and name?
/*

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest.newMyFriendsRequest(accessToken, new GraphRequest.GraphJSONArrayCallback() {
            @Override
            public void onCompleted(JSONArray jsonArray, GraphResponse graphResponse) {
                Log.d("FbFriends","Friends:" + graphResponse.toString());
                Log.d("FbFriends","Friends jsonArray:" + jsonArray.toString());
                if(graphResponse.getError() == null) {
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getRawResponse());//friends?
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getRequest().toString());
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getJSONArray());
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getJSONObject());//friends?
                }

            }
        }).executeAsync();
*/


        Log.d("FbFriends", "Get my Friends!");

        String userToFetch = "me";//;(userId != null) ? userId : "me";//todo?
        GraphRequest request = createRequest("/v2.1/" + userToFetch, extraFields);
        request.setCallback(new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.d("FbFriends", "Friends:" + graphResponse.toString());
                if (graphResponse.getError() == null) {
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getRawResponse());//friends?
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getRequest().toString());
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getJSONArray());
                    Log.d("FbFriends", "getRawResponse:" + graphResponse.getJSONObject());//friends?
                }
            }
        });

        request.executeAsync();
    }

    private GraphRequest createRequest(String userID, Set<String> extraFields) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, userID + "/friends", null);

        Set<String> fields = new HashSet<String>(extraFields);
        String[] requiredFields = new String[]{
                ID,
                NAME
        };
        fields.addAll(Arrays.asList(requiredFields));


        //todo add picture field?
        /*ViewGroup.LayoutParams layoutParams = mAvatar.getLayoutParams();
        String pictureField = String.format(Locale.US, "picture.height(%d).width(%d)", layoutParams.height, layoutParams.width);
        if (pictureField != null) {
            fields.add(pictureField);
        }*/

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        return request;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }





}
