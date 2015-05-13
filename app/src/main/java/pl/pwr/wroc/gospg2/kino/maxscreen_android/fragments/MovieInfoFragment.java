package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.GregorianCalendar;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
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

    @InjectView (R.id.movie_premiere)
    TextView mPremiere;

    @InjectView (R.id.image)
    ImageView mImage;

    @InjectView (R.id.movie_seances)
    MyGridView mSeances;

    @InjectView (R.id.comments_container)
    LinearLayout mComments;

    @InjectView (R.id.add_comment)
    Button mAddComment;

    @InjectView (R.id.my_mark)
    View mMyMark;




    private DisplayImageOptions mDisplayImageOptions;

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

        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        loadData();
        setListeners();
    }

    private void setListeners() {

    }

    private void loadData() {

        mSeances.setSeances(MSData.getInstance().getCurrentMovie().getSeances(),true);
        Movie movie = MSData.getInstance().getCurrentMovie();
        if(movie!=null) {
            mTitle.setText(movie.getTitle());
            mDescription.setText(movie.getDescription());
            mDirector.setText(movie.getDirector());
            mDuration.setText(Integer.toString(movie.getMinutes()) + " min");
            mKind.setText(movie.getKind());
            mCast.setText(movie.getCast());
            setImage(movie,mImage);
            mScenario.setText(movie.getScript());
            mPremiere.setText(Converter.gregToString(movie.getPremiere()));


            //todo want to see this
            mMyMark.setVisibility(View.GONE);
//            private int WantToSeeThis;

        }

    }

    private void setImage(Movie movie, ImageView view) {
        //todo image field for coupon

        if(movie!=null && movie.getImages()!=null) {
            String url = movie.getImages();
            view.setImageBitmap(null);

            if (url != null) {
                ImageLoader.getInstance().displayImage(url, view, mDisplayImageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                });
            }
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
