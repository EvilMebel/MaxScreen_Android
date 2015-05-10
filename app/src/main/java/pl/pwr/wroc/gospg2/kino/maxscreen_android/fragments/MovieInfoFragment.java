package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.MyGridView;
import roboguice.inject.InjectView;


public class MovieInfoFragment extends RoboEventFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @InjectView (R.id.dateTitle)
    TextView mDateTitle;

    @InjectView (R.id.title)
    TextView mTitle;

    @InjectView (R.id.movie_cast)
    TextView mCast;

    @InjectView (R.id.movie_description)
    TextView mDescription;

    @InjectView (R.id.movie_director)
    TextView mDirector;

    @InjectView (R.id.movie_duration)
    TextView mDuration;

    @InjectView (R.id.movie_kind)
    TextView mKind;

    //TODO mark stars
    @InjectView (R.id.movie_mark)
    View mMark;

    @InjectView (R.id.movie_scenario)
    TextView mScenario;

    @InjectView (R.id.image)
    ImageView mImage;

    @InjectView (R.id.movie_seances)
    MyGridView mSeances;

    @InjectView (R.id.comments_container)
    LinearLayout mComments;

    @InjectView (R.id.add_comment)
    Button mAddComment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieInfoFragment newInstance(String param1, String param2) {
        MovieInfoFragment fragment = new MovieInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        setListeners();
    }

    private void setListeners() {

    }

    private void loadData() {

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
