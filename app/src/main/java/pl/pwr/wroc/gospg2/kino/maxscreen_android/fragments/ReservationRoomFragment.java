package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.os.Bundle;
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
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.ListDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Tickets;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.ChoosedReliefEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.FinishReservationEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToLoginBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenCalendarEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.SeatClickEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.RoomView;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.SeatView;
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

    private List<Tickets> tickets;

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

        tickets = new ArrayList<Tickets>();
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

        mNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tickets.size() > 0) {
                    Customers c = ApplicationPreferences.getInstance().getCurrentCustomer();
                    if (c != null) {
                        tryMakeReservation(c);
                    } else {
                        Toast.makeText(getActivity(), getActivity().getString(R.string.log_in_for_reservation), Toast.LENGTH_LONG).show();
                        MaxScreen.getBus().post(new GoToLoginBus());
                    }
                } else {
                    Toast.makeText(getActivity(), getActivity().getString(R.string.no_tickets_chosen), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        Seance seance = MSData.getInstance().getSeance();

        if(seance!=null) {
            this.seance = seance;
            downloadHall();
        } else {
            //error - exit from hall
            MaxScreen.getBus().post(new OpenCalendarEventBus());
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
        final SeatView seatView = bus.getSeatView();
        String s = " Wybrano miejsce. Rzad " + seatView.getSeatRow() + " miejsce " + seatView.getSeatCol();



        Date now = new Date(System.currentTimeMillis());
        GregorianCalendar n = new GregorianCalendar();
        n.setTime(now);
        n.add(Calendar.MONTH, 1);
        n.add(Calendar.MINUTE,30);
        Log.d("Now!", "now=" + Converter.gregToMySQLformat(n) + " h:" + Converter.getHourFromGreCale(n));
        Log.d("Now!","seance="+ Converter.gregToMySQLformat(seance.getDate()) + " h:" + Converter.getHourFromGreCale(seance.getDate()));

        if(!seance.getDate().after(n)) {
            Toast.makeText(getActivity(),getActivity().getString(R.string.cant_buy_ticket_for_old_seance), Toast.LENGTH_SHORT).show();
        } else {

            if (seatView.getStatus() == SeatView.SeatStat.FREE) {

                //Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                ListDialogFragment dialogFragment = new ListDialogFragment();
                dialogFragment.setMsgText(s + "\nWybierz rodzaj ulgi.");
                dialogFragment.setAdapterType(ListDialogFragment.AdapterType.TICKETS_RELIEF);
                dialogFragment.setOnObjectReady(new ListDialogFragment.OnObjectReady() {
                    @Override
                    public void returnObject(Object object) {
                        if (object != null && object instanceof Relief) {
                            Tickets t = new Tickets();
                            t.setRow(seatView.getSeatRow());
                            t.setLine(seatView.getSeatCol());
                            t.setReliefEntity((Relief) object);

                            tickets.add(t);
                            Toast.makeText(getActivity(), "sizeTickets=" + tickets.size(), Toast.LENGTH_SHORT).show();
                            mRoom.clickSeat(seatView);
                        }
                    }
                });
                dialogFragment.show(getFragmentManager(), null);
            } else {
                //remove from list ;(

                for (int i = tickets.size() - 1; i >= 0; i--) {
                    Tickets t = tickets.get(i);
                    if (t.getRow() == seatView.getSeatRow() && t.getLine() == seatView.getSeatCol()) {
                        tickets.remove(t);
                        Toast.makeText(getActivity(), getActivity().getString(R.string.ticket_has_been_deleted), Toast.LENGTH_SHORT).show();
                        mRoom.clickSeat(seatView);
                    }
                }

            }
        }

    }

    @Subscribe
    public void choosedRelief(ChoosedReliefEventBus bus) {
        //ldfng
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
                    downloadTakenSeats();
                } else {
                    Toast.makeText(getActivity(),getActivity().getString(R.string.error_downloading_hall),Toast.LENGTH_SHORT).show();
                    MaxScreen.getBus().post(new OpenCalendarEventBus());
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

        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/ticket/taken/" + seance.getIdSeance() +"?";
        Log.d("RoomReserv","GET!"+link);
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                List<Tickets> seats = new ArrayList<Tickets>();

                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray array = new JSONArray(response);

                    Log.d(getTag(),"seats.Count:" + array.length());

                    for(int i = 0; i<array.length(); i++){
                        JSONObject obj = array.getJSONObject(i);
                        Tickets t = Tickets.parseEntitySimple(obj);


                        seats.add(t);
                    }


                    Log.d(getTag(), "seats :" + seats);

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                    seats = null;

                }
                if(seats!=null) {
                    mRoom.setTakenSeats(seats);
                    mRoom.setVisibility(View.VISIBLE);
                    /*hall = h;
                    mRoom.loadRoom(hall);
                    fixViews();
                    mRoom.setVisibility(View.VISIBLE);*/
                } else {
                    Toast.makeText(getActivity(),getActivity().getString(R.string.error_downloading_taken_seants),Toast.LENGTH_SHORT).show();
                    MaxScreen.getBus().post(new OpenCalendarEventBus());
                }

                mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                mLoading.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),getActivity().getString(R.string.error_downloading_taken_seants),Toast.LENGTH_SHORT).show();
                Utils.showAsyncError(getActivity(),statusCode,error,content);
            }
        });
    }

    public void tryMakeReservation(Customers c){
        RequestParams params = new RequestParams();

        int customerId = c.getIdCustomer();
        int seanceId = seance.getIdSeance();
        String ticketsStr = "";
        for(int i = 0; i<this.tickets.size(); i++) {
            Tickets t = this.tickets.get(i);
            ticketsStr+= t.getRow() + "," + t.getLine() + "," + t.getReliefEntity().getIdRelief();

            if(i<this.tickets.size()-1) {
                ticketsStr+="X";
            }
        }
        Log.d("RoomReserv","data:"+ticketsStr);



        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient task = new AsyncHttpClient();
        String link = Net.dbIp + "/reservation/insert/"+ticketsStr+"/"+customerId+"/"+ seanceId+"?";
        Log.d("RoomReserv","link:"+link);
        final String finalTicketsStr = ticketsStr;
        task.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog

                int success = Integer.parseInt(response);


                if (success != -1) {
                    Toast.makeText(getActivity(),"Dokonano Rezerwacji! : D id:"+success,Toast.LENGTH_SHORT).show();
                    MaxScreen.getBus().post(new FinishReservationEventBus(success, finalTicketsStr));
                } else {
                    Toast.makeText(getActivity(),getActivity().getString(R.string.cant_book_reservation_so_refresh),Toast.LENGTH_SHORT).show();
                    mRoom.refreshRoom();
                    mLoading.setVisibility(View.VISIBLE);
                    mRoom.setVisibility(View.INVISIBLE);
                    tickets.clear();
                    downloadTakenSeats();
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


}
