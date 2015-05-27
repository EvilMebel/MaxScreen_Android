package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MyReservationsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Reservation;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenCalendarEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreference;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
import roboguice.inject.InjectView;


public class MyReservationFragment extends RoboEventFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @InjectView (R.id.no_reservations) View mNoReservations;


    @InjectView (R.id.reservation_list) View mReservationsFrame;
    @InjectView (R.id.list) ListView mList;
    @InjectView (R.id.loading) View mLoading;


    @InjectView (R.id.goto_calendar) Button mCalendarButton;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyReservationFragment.
     */
    public static MyReservationFragment newInstance(String param1, String param2) {
        MyReservationFragment fragment = new MyReservationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MyReservationFragment() {
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
        return inflater.inflate(R.layout.fragment_my_reservation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        setListeners();
    }

    private void setListeners() {
        mCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxScreen.getBus().post(new OpenCalendarEventBus());
            }
        });

    }

    private void loadData() {
        Customers user = ApplicationPreferences.getInstance().getCurrentCustomer();

        if(user!=null) {
            downloadReservations(user);
        }
    }

    private void downloadReservations(Customers user) {

        mNoReservations.setVisibility(View.GONE);
        mLoading.setVisibility(View.VISIBLE);
        mReservationsFrame.setVisibility(View.VISIBLE);
        //todo valley
        // Tag used to cancel the request
        String tag_json_obj = "news";


        String url = Net.dbIp + "/reservation/user/" + user.getIdCustomer();
        Log.d(getTag(),"GET!"+url);
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(getTag(), response.toString());

                        ArrayList<Reservation> items = new ArrayList<Reservation>();
                        try {
                            int size = response.length();
                            for(int i = 0; i<size; i++) {
                                JSONObject item = response.getJSONObject(i);

                                Reservation n = Reservation.parseEntity(item,true);
                                items.add(n);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        if(items!=null) {
                            mList.setAdapter(new MyReservationsAdapter(getActivity(), items));

                            if(items.isEmpty()) {
                                mReservationsFrame.setVisibility(View.GONE);
                                mNoReservations.setVisibility(View.VISIBLE);
                            }
                        }

                        mLoading.setVisibility(View.GONE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(getTag(), "Error: " + error.getMessage());
                mReservationsFrame.setVisibility(View.GONE);
                mNoReservations.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.INVISIBLE);
            }
        });


        // Adding request to request queue
        MaxScreen.getInstance().addToRequestQueue(req, tag_json_obj);
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
