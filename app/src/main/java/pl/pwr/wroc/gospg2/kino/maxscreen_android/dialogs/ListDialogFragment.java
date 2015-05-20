package pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.ReliefsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.ChoosedReliefEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;


public class ListDialogFragment extends RoboDialogFragment {
    @InjectView (R.id.loading)
    View mLoading;

    @InjectView (R.id.list)
    ListView mList;

    @InjectView (R.id.msg)
    TextView mMsg;


    AdapterType adapterType;
    private ArrayList<Movie> movieItems;
    private ArrayList<Relief> reliefs;

    private String msgText;

    OnObjectReady onObjectReady;

    public enum AdapterType {
        MOVIES,
        TICKETS_RELIEF
    }

    public interface onDialogListener {
        void onDismiss();
        void onShow();
    }

    private onDialogListener mOnDialogListener;

    public void setOnDialogListener(onDialogListener listener) {
        mOnDialogListener = listener;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            getDialog().getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().setDimAmount(0);

        getDialog().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        setAdapter();
        fixUI();
    }

    private void fixUI() {
        if(msgText!=null) {
            mMsg.setText(msgText);
        } else {
            mMsg.setVisibility(View.GONE);
        }
    }

    private void setAdapter() {
        if(adapterType!=null) {
            switch (adapterType) {
                default:
                    break;
                case MOVIES:

                    downloadMyWants();
                    getDialog().setCanceledOnTouchOutside(true);
                    setCancelable(true);

                    break;
                case TICKETS_RELIEF:

                    downloadReliefs();
                    getDialog().setCanceledOnTouchOutside(true);
                    setCancelable(true);
            }
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);

        if(mOnDialogListener != null) {
            mOnDialogListener.onShow();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fragment_list, container, false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mOnDialogListener != null) {
            mOnDialogListener.onDismiss();
        }
    }

    public AdapterType getAdapterType() {
        return adapterType;
    }

    public void setAdapterType(AdapterType adapterType) {
        this.adapterType = adapterType;
    }

    public onDialogListener getmOnDialogListener() {
        return mOnDialogListener;
    }

    public void setmOnDialogListener(onDialogListener mOnDialogListener) {
        this.mOnDialogListener = mOnDialogListener;
    }

    public OnObjectReady getOnObjectReady() {
        return onObjectReady;
    }

    public void setOnObjectReady(OnObjectReady onObjectReady) {
        this.onObjectReady = onObjectReady;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public void downloadMyWants(){
        RequestParams params = new RequestParams();
        String customerId = "8";
        params.add("customerId", customerId);//todo
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/movie/want/"+customerId+"?";
        Log.d(getTag(),"GET!"+link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                Log.d(getTag(),"response:"+response);

                ArrayList<Movie> items = null;


                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<Movie>();

                    int size = obj.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = obj.getJSONObject(i);

                        Movie n = Movie.parseEntity(item);
                        items.add(n);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (items != null) {
                    WantMoviesAdapter moviesAdapter = new WantMoviesAdapter(getActivity(), items);
                    moviesAdapter.setOnClickMovie(new WantMoviesAdapter.OnClickMovie() {
                        @Override
                        public void onClick() {
                            dismiss();
                        }
                    });

                    mList.setAdapter(moviesAdapter);
                    mLoading.setVisibility(View.INVISIBLE);
                }

                //hideLoadingDialog();

                Log.d("WantToSee","Movies:"+items);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }

    public void downloadReliefs(){
        RequestParams params = new RequestParams();
        String customerId = "8";
        params.add("customerId", customerId);//todo
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/relief";
        Log.d(getTag(),"GET!"+link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                Log.d(getTag(),"response:"+response);

                ArrayList<Relief> items = null;


                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<Relief>();

                    int size = obj.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = obj.getJSONObject(i);

                        Relief n = Relief.parseEntity(item);
                        items.add(n);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (items != null) {
                    ReliefsAdapter moviesAdapter = new ReliefsAdapter(getActivity(), items);
                    final ArrayList<Relief> finalItems = items;
                    moviesAdapter.setOnClickRelief(new ReliefsAdapter.OnClickRelief() {
                        @Override
                        public void onClick(int position) {
                            Relief r = finalItems.get(position);

                            //show RoomFragment chosen relief
                            if(onObjectReady!=null)
                                onObjectReady.returnObject(r);
                            //MaxScreen.getBus().post(new ChoosedReliefEventBus(r));
                            dismiss();
                        }
                    });

                    mList.setAdapter(moviesAdapter);
                    mLoading.setVisibility(View.INVISIBLE);
                }

                //hideLoadingDialog();

                Log.d("Reliefs","Reliefs:"+items);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }

    public interface OnObjectReady {
        public void returnObject(Object object);
    }

}
