package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.CalendarListAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.DateViewPager;
import roboguice.inject.InjectView;

public class CalendarFragment extends RoboEventFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView (R.id.list)
    ListView mList;

    @InjectView (R.id.calendar_pager)
    DateViewPager mDatePager;

    @InjectView (R.id.prev_week)
    View mPrevWeek;

    @InjectView (R.id.next_week)
    View mNextWeek;

    @InjectView (R.id.loading)
    View mLoading;


    private AsyncHttpClient  task;
    private GregorianCalendar calendar;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CalendarFragment() {
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
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListeners();


        calendar = mDatePager.getCalendar();
        //task = new BgAsyncTask();
        //new DoctorShiftsAsyncTask().execute();
        //task.execute();
        downloadCalendar();


    }

    private void setListeners() {
        mPrevWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePager.prevDaySmooth();
            }
        });

        mNextWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePager.nextDaySmooth();
            }
        });

        mDatePager.setListener(new DateViewPager.OnDateChanged() {
            @Override
            public void onDateChanged(GregorianCalendar cal) {
                //todo test
                mList.setAdapter(null);
                if(task!=null)
                    task.cancelRequests(getActivity(),true);

                calendar = cal;
                /*task = new BgAsyncTask();
                task.execute();*/
                downloadCalendar();


            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void downloadCalendar(){
        RequestParams params = new RequestParams();
        params.add("date", Converter.gregToMySQLformat(calendar));//YYYY-MM-DD
        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        task = new AsyncHttpClient();
        String link = Net.oldDbIp + "/calendar/get";
        Log.d(getTag(), "GET!" + link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                ArrayList<Movie> items = null;

                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<Movie>();

                    int size = obj.length();
                    for(int i = 0; i<size; i++) {
                        JSONObject item = obj.getJSONObject(i);
                        Log.d(getTag(),"Item:" + item.toString());

                        Movie movie = Movie.parseEntityOld(item.getJSONObject("movie"));
                        List<Seance> seances = new ArrayList<Seance>();
                        JSONArray seancesJSON = item.getJSONArray("seances");

                        for(int j = 0; j<seancesJSON.length(); j++) {
                            Seance s = Seance.parseEntityOld(seancesJSON.getJSONObject(j));
                            seances.add(s);
                        }
                        if(movie!=null && seances!=null) {
                            movie.setSeances(seances);
                            items.add(movie);
                        }
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if(items!=null) {
                    mList.setAdapter(new CalendarListAdapter(getActivity(), items));
                }

                mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                mLoading.setVisibility(View.INVISIBLE);
                // When Http response code is '404'
                if (statusCode == 404) {
                    Toast.makeText(getActivity().getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    Log.e(getTag(),"ERROR:" + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), statusCode + "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
