package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.WantToSee;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToLoginBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
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


    @InjectView (R.id.want)
    View mWant;



    boolean wantMovie = false;



    private DisplayImageOptions mDisplayImageOptions;
    private int wantIndex;

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

        mWant.setVisibility(View.GONE);
        loadData();
        setListeners();
    }

    private void setListeners() {

    }

    private void loadData() {

        if (MSData.getInstance().getCurrentMovie().getSeances() != null) {
            mSeances.setSeances(MSData.getInstance().getCurrentMovie().getSeances(), true);
        } else {
            //todo load seances
            //loadSeances(movie);
        }


        Movie movie = MSData.getInstance().getCurrentMovie();
        if (movie != null) {
            mTitle.setText(movie.getTitle());
            mDescription.setText(movie.getDescription());
            mDirector.setText(getActivity().getString(R.string.directior) + " " + movie.getDirector());
            mDuration.setText(Integer.toString(movie.getMinutes()) + " min");
            mKind.setText("Gatunek: " + movie.getKind());
            mCast.setText("Obsada: " + movie.getCast());
            setImage(movie, mImage);
            mScenario.setText("Scenariusz: " + movie.getScript());
            mPremiere.setText(Converter.gregToString(movie.getPremiere()));


            //todo want to see this
            mMyMark.setVisibility(View.GONE);
//            private int WantToSeeThis;

            if(Utils.isIsLoggedIn())
                loadWantTo(movie);

            if(movie.getSeances()==null || movie.getSeances().isEmpty()) {
                loadSeances(movie);
            }


        }


    }

    private void loadWantTo(Movie movie) {
        mWant.setOnClickListener(null);
        RequestParams params = new RequestParams();
        params.add("movieId", Integer.toString(movie.getIdMove()));
        String customerId = "8";//todo ? :D
        int movieId = movie.getIdMove();
        params.add("customerId", customerId);//todo
        // Make RESTful webservice call using AsyncHttpClient object
            AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/wanttosee/want/"+customerId + "/" + movieId + "?";
        Log.d(getTag(), "GET!" + link);
            client.get(link, params, new AsyncHttpResponseHandler() {


                // When the response returned by REST has Http response code '200'
                @Override
                public void onSuccess(String response) {
                    // Hide Progress Dialog
                    //todfokjfgnbfkjn
                    Log.d(getTag(), "response:" + response);

                    int index = -1;
                    String msg = "";

                    try {
                        JSONObject object = new JSONObject(response);
                        index = object.getInt("wantId");

                    } catch (JSONException e) {
                        e.printStackTrace();
                        index = -1;
                    }

                    //-1 is not found!
                    wantMovie = index!=-1;
                    wantIndex = index;

                    mWant.setVisibility(View.VISIBLE);
                    refreshWantUI();

                }

                // When the response returned by REST has Http response code other than '200'
                @Override
                public void onFailure(int statusCode, Throwable error,
                                      String content) {
                    Utils.showAsyncError(getActivity(),statusCode,error,content);
                }
            });
        }

    private void loadSeances(Movie movie) {
        mWant.setOnClickListener(null);
        RequestParams params = new RequestParams();
        params.add("movieId", Integer.toString(movie.getIdMove()));


        String customerId = "8";//todo ? :D
        int movieId = movie.getIdMove();
        params.add("customerId", customerId);//todo
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/seance/seances/"+movie.getIdMove() + "?";
        Log.d(getTag(), "GET!" + link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //todfokjfgnbfkjn
                Log.d(getTag(), "response:" + response);
                List<Seance> seanceList = new ArrayList<Seance>();

                try {
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i<array.length(); i++) {
                        JSONObject obj = (JSONObject) array.get(i);
                        Seance s = Seance.parseEntity(obj,false);

                        seanceList.add(s);
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                    seanceList = null;
                }


                if(seanceList!=null) {
                    mSeances.setSeances(seanceList,true);
                } else {
                    Toast.makeText(getActivity(), "Blad pobierania seansow dla filmu!", Toast.LENGTH_SHORT).show();
                }

            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                Utils.showAsyncError(getActivity(),statusCode,error,content);
            }
        });
    }


    private void addWantTo(Movie movie) {
        mWant.setOnClickListener(null);
        RequestParams params = new RequestParams();

        String customerId = "8";//todo ? :D
        int movieId = movie.getIdMove();
/*
        params.add("movieId", Integer.toString(movie.getIdMove()));
        params.add("customerId", customerId);//todo*/
        /*String link = Net.dbIp + "/wanttosee";


        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();


        JSONObject jsonParams = new JSONObject();
        try {
            jsonParams.put(WantToSee.CUSTOMERS_IDCUSTOMER, customerId);
            jsonParams.put(WantToSee.MOVIE_IDMOVE,movieId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log("POST!" + jsonParams.toString());
        client.post(getActivity(), link, entity, "application/json",*/

        String link = Net.dbIp + "/wanttosee/post/"+customerId+"/"+movieId;

        log("POST!" + link);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(link, params,


                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //todfokjfgnbfkjn
                        Log.d(getTag(), "response:" + response);

                        try {
                            wantIndex = Integer.parseInt(response);


                        } catch (Exception e) {
                            e.printStackTrace();
                            wantIndex = -1;
                        }


                        if (wantIndex!=-1) {
                            //udalo sie!
                            wantMovie = true;
                            mWant.setVisibility(View.VISIBLE);
                            refreshWantUI();
                        } else {
                            Toast.makeText(getActivity(), getActivity().getString(R.string.error), Toast.LENGTH_SHORT).show();
                            mWant.setVisibility(View.INVISIBLE);
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

    private void removeWantTo() {
        mWant.setOnClickListener(null);

        RequestParams params = new RequestParams();
        String link = Net.dbIp + "/wanttosee/"+wantIndex;

        log("DELETE!" + link);
        AsyncHttpClient client = new AsyncHttpClient();
        client.delete(link,
                new AsyncHttpResponseHandler() {
                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //todfokjfgnbfkjn
                        Log.d(getTag(), "response:" + response);

                        //udalo sie!
                        wantMovie = false;
                        mWant.setVisibility(View.VISIBLE);
                        refreshWantUI();

                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        Utils.showAsyncError(getActivity(), statusCode, error, content);
                    }
                });
    }

    private void log(String s) {
        Log.d(getTag(),s);
    }

    private void refreshWantUI() {
        if(wantMovie) {
            mWant.setBackgroundResource(R.color.com_facebook_blue);
            mWant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    removeWantTo();
                }
            });
        } else {
            mWant.setBackgroundResource(R.color.button_background);
            mWant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addWantTo(MSData.getInstance().getCurrentMovie());
                }
            });
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
