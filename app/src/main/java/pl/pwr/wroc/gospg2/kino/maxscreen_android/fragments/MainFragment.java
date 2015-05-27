package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.MainNewsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.inject.InjectView;

public class MainFragment extends RoboEventFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView (R.id.list)
    private ListView mList;

    @InjectView (R.id.empty_response)
    TextView mEmptyResponse;

    @InjectView (R.id.loading)
    View mLoading;

    private MainNewsAdapter mAdapter;
    private String TAG = "DownloadNews";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
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
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        //downloadNewsAsync();
        downloadNewsValley();

    }

    private void downloadNewsValley() {
        // Tag used to cancel the request
        String tag_json_obj = "news";

        mEmptyResponse.setVisibility(View.INVISIBLE);
        mLoading.setVisibility(View.VISIBLE);

        String url = Net.dbIp + "/news";
        JsonArrayRequest req = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());

                        ArrayList<News> items = new ArrayList<News>();
                        try {
                            int size = response.length();
                            for(int i = 0; i<size; i++) {
                                JSONObject item = response.getJSONObject(i);

                                News n = News.parseEntity(item);
                                items.add(n);
                            }

                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        if(items!=null) {
                            mList.setAdapter(new MainNewsAdapter(getActivity(), items));

                            if(items.isEmpty()) {
                                mEmptyResponse.setText(getActivity().getString(R.string.no_news_on_site));
                                mEmptyResponse.setVisibility(View.VISIBLE);
                            }
                        }

                        hideLoadingDialog();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                mEmptyResponse.setText(getActivity().getString(R.string.news_error_downloading));
                mEmptyResponse.setVisibility(View.VISIBLE);
                mLoading.setVisibility(View.INVISIBLE);
                hideLoadingDialog();
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



    public void downloadNewsAsync(){
        RequestParams params = new RequestParams();
        // Show Progress Dialog
        mLoading.setVisibility(View.VISIBLE);
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Net.dbIp + "/news", params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                ArrayList<News> items = null;


                try {
                    // JSON Object
                    Log.d(getTag(),"response:"+response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<News>();

                    int size = obj.length();
                    for(int i = 0; i<size; i++) {
                        JSONObject item = obj.getJSONObject(i);

                        News n = News.parseEntity(item);
                        items.add(n);
                    }

                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if(items!=null) {
                    mList.setAdapter(new MainNewsAdapter(getActivity(), items));
                }

                hideLoadingDialog();
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                hideLoadingDialog();
                Utils.showAsyncError(getActivity(),statusCode,error,content);
            }
        });
    }

    private void hideLoadingDialog() {
        mLoading.setVisibility(View.INVISIBLE);
    }

}
