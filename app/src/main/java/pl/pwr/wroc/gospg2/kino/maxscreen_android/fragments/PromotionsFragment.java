package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.PromotionsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupons;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.inject.InjectView;

public class PromotionsFragment extends RoboEventFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @InjectView (R.id.list)
    ListView mList;

    @InjectView (R.id.loading)
    View mLoading;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionsFragment.
     */
    public static PromotionsFragment newInstance(String param1, String param2) {
        PromotionsFragment fragment = new PromotionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PromotionsFragment() {
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
        return inflater.inflate(R.layout.fragment_promotions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    private void loadData() {

        downloadCoupons();




    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void downloadCoupons(){
        RequestParams params = new RequestParams();
        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Net.dbIp + "/coupondb", params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                ArrayList<Coupon_DB> items = null;


                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<Coupon_DB>();

                    int size = obj.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = obj.getJSONObject(i);

                        Coupon_DB n = Coupon_DB.parseEntity(item);
                        items.add(n);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (items != null) {
                    mList.setAdapter(new PromotionsAdapter(getActivity(), items));
                }

                mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                mLoading.setVisibility(View.INVISIBLE);
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }

}
