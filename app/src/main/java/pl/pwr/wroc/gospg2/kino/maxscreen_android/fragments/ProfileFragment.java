package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.inject.InjectView;

public class ProfileFragment extends RoboEventFragment {
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
    }

    private void getData() {
        if(mMyProfle) {
            if(Utils.isLoggedInFacebook()) {
                Profile profile = Profile.getCurrentProfile();
                //load avatar etc
                mAvatar.setProfileId(profile.getId());
                mUsername.setText(profile.getFirstName() + " " + profile.getLastName());

            } else {

            }
        } else {
            //todo get user data by Id - REST
        }


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
