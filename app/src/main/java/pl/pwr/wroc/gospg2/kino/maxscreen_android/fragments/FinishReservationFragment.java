package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Reservation;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Tickets;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import roboguice.inject.InjectView;

public class FinishReservationFragment extends RoboEventFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String RESERVATION_NUMBER = "param1";
    private static final String TICKETS_TEXT = "param2";

    private int mReservationId;
    private String mTicketsText;


    @InjectView (R.id.title)
    TextView mTitleType;

    @InjectView (R.id.date)
    TextView mDate;

    @InjectView (R.id.hour)
    TextView mHour;

    @InjectView (R.id.hall)
    TextView mHall;


    @InjectView (R.id.tickets_list)
    LinearLayout mTicketList;



    @InjectView (R.id.tickets_text_test)
    TextView mTicketsTest;










    @InjectView (R.id.reservation_number)
    TextView mReservationNumber;

     /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FinishReservationFragment.
     */
    public static FinishReservationFragment newInstance(int param1, String param2) {
        FinishReservationFragment fragment = new FinishReservationFragment();
        Bundle args = new Bundle();
        args.putInt(RESERVATION_NUMBER, param1);
        args.putString(TICKETS_TEXT, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public FinishReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReservationId = getArguments().getInt(RESERVATION_NUMBER);
            mTicketsText = getArguments().getString(TICKETS_TEXT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_finish_reservation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        setListeners();

    }

    private void loadData() {

        mReservationNumber.setText("" + mReservationId);
        mTicketsTest.setText("" + mTicketsText);
        downloadReservation();
        downloadTickets();
    }

    private void setListeners() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void downloadReservation(){
        RequestParams params = new RequestParams();

        // Show Progress Dialog
        //mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/reservation/" + mReservationId +"?";
        Log.d("RoomReserv", "link:" + link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                Reservation r = null;

                try {
                    // JSON Object
                    Log.e(getTag(), "response:" + response);
                    JSONObject obj = new JSONObject(response);
                    r = Reservation.parseEntity(obj, true);

                    Log.e(getTag(), "Reservation :" + r);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (r != null) {
                    Log.d(getTag(), "WYPARSOWANE! :D");
                    /*hall = h;
                    mRoom.loadRoom(hall);
                    fixViews();
                    mRoom.setVisibility(View.VISIBLE);
                    downloadTakenSeats();*/

                    mTitleType.setText(r.getSeanceEntity()
                            .getMovieEntity()
                            .getTitle() + " (" +
                            r
                                    .getSeanceEntity()
                                    .getType() + ")");
                    mDate.setText(Converter.gregToStringWithDay(
                            r
                                    .getSeanceEntity().getDate()));
                    mHour.setText(Converter.getHourFromGreCale(
                            r.
                                    getSeanceEntity()
                                    .getDate()));
                    mHall.setText(r.getSeanceEntity().getHallsEntity().getName_Hall());
                } else {
                    //todo exit!! D:
                    Log.e(getTag(), "NULL D:");
                }

                //mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //mLoading.setVisibility(View.INVISIBLE);
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
                    Log.e(getTag(), "ERROR:" + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), statusCode + "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void downloadTickets(){
        RequestParams params = new RequestParams();

        // Show Progress Dialog
        //mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/ticket/byreservation/" + mReservationId +"?";
        Log.d("RoomReserv", "link:" + link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                List<Tickets> tickets = new ArrayList<Tickets>();

                try {
                    // JSON Object
                    Log.e(getTag(), "response:" + response);
                    JSONArray array = new JSONArray(response);
                    for(int i = 0; i< array.length(); i++) {
                        JSONObject obj = (JSONObject) array.get(i);

                        Tickets t = Tickets.parseEntityWRelief(obj);

                        tickets.add(t);
                    }



                    Log.e(getTag(), "tickets :" + tickets);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (tickets != null) {
                    Log.d(getTag(), "WYPARSOWANE TICKETS! :D");

                    for(int i =0; i<tickets.size(); i++) {
                        Tickets t = tickets.get(i);
                        TextView tv = new TextView(getActivity());
                        tv.setText("Miejsce " + t.getLine()+" "+ getActivity().getString(R.string.row) +" "+  t.getRow() + getActivity().getString(R.string.relief) + " " + t.
                                getReliefEntity()
                                .getName());


                        mTicketList.addView(tv);
                    }


                    /*hall = h;
                    mRoom.loadRoom(hall);
                    fixViews();
                    mRoom.setVisibility(View.VISIBLE);
                    downloadTakenSeats();*/

                } else {
                    //todo exit!! D:
                    Log.e(getTag(), "NULL D:");
                }

                //mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //mLoading.setVisibility(View.INVISIBLE);
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
                    Log.e(getTag(), "ERROR:" + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), statusCode + "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
