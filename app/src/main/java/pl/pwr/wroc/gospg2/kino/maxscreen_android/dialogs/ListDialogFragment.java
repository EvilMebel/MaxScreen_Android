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
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;


public class ListDialogFragment extends RoboDialogFragment {
    @InjectView (R.id.loading)
    View mLoading;

    @InjectView (R.id.list)
    ListView mList;

    AdapterType adapterType;
    private ArrayList<Movie> movieItems;

    public enum AdapterType {
        MOVIES
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
    }

    private void setAdapter() {
        if(adapterType!=null) {
            switch (adapterType) {
                default:
                    break;
                case MOVIES:

                    downloadMyWants();


                    break;
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

    public void downloadMyWants(){
        RequestParams params = new RequestParams();
        String customerId = "8";
        params.add("customerId", customerId);//todo
        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Net.dbIp + "/want/mywants", params, new AsyncHttpResponseHandler() {


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
}
