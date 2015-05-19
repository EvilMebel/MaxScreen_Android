package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.common.eventbus.Subscribe;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.CalendarListAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.SeatClickEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.RoomView;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.SeatView;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class ReservationRoomFragment extends RoboEventFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @InjectView (R.id.zoom_in)
    View mZoomIn;

    @InjectView (R.id.zoom_out)
    View mZoomOut;

    @InjectView (R.id.next)
    View mNext;

    @InjectView (R.id.room)
    RoomView mRoom;

    @InjectView (R.id.loading)
    View mLoading;

    Halls hall;
    private Seance seance;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RoomFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationRoomFragment newInstance(String param1, String param2) {
        ReservationRoomFragment fragment = new ReservationRoomFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ReservationRoomFragment() {
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
        return inflater.inflate(R.layout.fragment_room, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fixViews();
        loadData();
        setListeners();
    }

    private void fixViews() {
        DisplayMetrics metrics = Utils.getDeviceMetrics(getActivity());
        mRoom.fitRoomToScreen(metrics.widthPixels);
    }

    private void setListeners() {
        mZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoom.zoomIn();
            }
        });

        mZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRoom.zoomOut();
            }
        });
    }

    private void loadData() {

        //todo REST!
        Seance seance = MSData.getInstance().getSeance();

        if(seance!=null) {
            this.seance = seance;
            downloadHall();
        } else {
            //todo exit!! D:
        }

        mLoading.setVisibility(View.INVISIBLE);

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Subscribe
    public void seatClicked(SeatClickEventBus bus) {
        SeatView seatView = bus.getSeatView();
        String s = " Wybrano miejsce. Rzad " + seatView.getSeatRow() + " miejsce " + seatView.getSeatCol();
        mRoom.clickSeat(seatView);

        Toast.makeText(getActivity(),s,Toast.LENGTH_SHORT).show();
    }


    public void downloadHall(){
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(seance.getHalls_idHall()));
        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/hall/" + seance.getHalls_idHall() +"?";
        Log.d("RoomReserv","link:"+link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                Halls h = null;

                try {
                    // JSON Object
                    Log.e(getTag(), "response:" + response);
                    JSONObject obj = new JSONObject(response);
                    h = Halls.parseEntity(obj);

                    Log.e(getTag(), "hall :" + h);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (h != null) {
                    hall = h;
                    mRoom.loadRoom(hall);
                    fixViews();
                    mRoom.setVisibility(View.VISIBLE);
                } else {
                    //todo exit!! D:
                    Log.e(getTag(), "NULL D:");
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
                    Log.e(getTag(), "ERROR:" + error.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), statusCode + "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void downloadTakenSeats(){
        RequestParams params = new RequestParams();
        params.add("id", String.valueOf(seance.getHalls_idHall()));
        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/hall/" + seance.getHalls_idHall() +"?";//todo
        Log.d("RoomReserv","link:"+link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                Halls h = null;

                try {
                    // JSON Object
                    Log.e(getTag(),"response:" + response);
                    JSONObject obj = new JSONObject(response);
                    h = Halls.parseEntity(obj);

                    Log.e(getTag(),"hall :" + h);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if(h!=null) {
                    hall = h;
                    mRoom.loadRoom(hall);
                    fixViews();
                    mRoom.setVisibility(View.VISIBLE);
                } else {
                    //todo exit!! D:
                    Log.e(getTag(),"NULL D:");
                }

                mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                mLoading.setVisibility(View.INVISIBLE);
                Utils.showAsyncError(getActivity(),statusCode,error,content);
            }
        });
    }


}
