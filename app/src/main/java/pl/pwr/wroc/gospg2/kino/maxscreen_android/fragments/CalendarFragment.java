package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.CalendarListAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
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


        ArrayList<Movie> items = new ArrayList<Movie>();
        //todo load data online
        for(int i =0; i<4 ; i++) {
            ArrayList<Seance> hours = new ArrayList<Seance>();
            Movie f = new Movie();
            for (int j = 0; j < 4; j++) {
                Seance s = new Seance();
                s.setDate(new Date(System.currentTimeMillis()));// = new Time();
                hours.add(s);
            }
            f.setSeances(hours);
            items.add(f);

        }
        mList.setAdapter(new CalendarListAdapter(getActivity(),items));
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
