package pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.CommentedMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.FriendsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.ReliefsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Rate;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.ChoosedReliefEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreference;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
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

    @InjectView (R.id.empty_response)
    TextView mEmptyResponse;





    AdapterType adapterType;
    private ArrayList<Movie> movieItems;
    private ArrayList<Relief> reliefs;

    private String msgText;

    OnObjectReady onObjectReady;
    private Customers currentUser;

    public enum AdapterType {
        MOVIES,
        COMMENTED_MOVIES,
        FACEBOOK_FRIENDS,
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
        //getDialog().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getDialog().setCanceledOnTouchOutside(false);
        setCancelable(false);

        setAdapter();
        fixUI();
    }

    private void fixUI() {
        mList.setFastScrollEnabled(true);//bugfix

        mEmptyResponse.setVisibility(View.INVISIBLE);
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

                    mEmptyResponse.setText(getActivity().getString(R.string.no_wanted_movies));
                    if(currentUser!=null)
                        downloadMyWants(currentUser);

                    getDialog().setCanceledOnTouchOutside(true);
                    setCancelable(true);

                    break;
                case TICKETS_RELIEF:

                    downloadReliefs();
                    getDialog().setCanceledOnTouchOutside(true);
                    setCancelable(true);
                    break;
                case COMMENTED_MOVIES:

                    mEmptyResponse.setText(getActivity().getString(R.string.have_never_add_comment));
                    if(currentUser!=null)
                        downloadMyCommentedMovies(currentUser);

                    getDialog().setCanceledOnTouchOutside(true);
                    setCancelable(true);
                    break;
                case FACEBOOK_FRIENDS:
                    mEmptyResponse.setText(getActivity().getString(R.string.no_facebook_friends_forever_alone));
                    if(currentUser!=null)
                        getFacebookFriends(currentUser);

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

    public Customers getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(Customers currentUser) {
        this.currentUser = currentUser;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    /*
                            START FACEBOOK FRIENDS
     */

    private void getFacebookFriends(Customers customers) {
        HashSet<String> extraFields = new HashSet<String>();
        Log.d("FbFriends", "Get my Friends!");

        String userToFetch = customers.getFacebookId() + "";//;(userId != null) ? userId : "me";//todo?
        GraphRequest request = createRequest("/v2.3/" + userToFetch, extraFields);
        request.setCallback(new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse graphResponse) {
                Log.d("FbFriends", "Friends:" + graphResponse.toString());
                if (graphResponse.getError() == null) {
                    try {
                        JSONArray friends = graphResponse.getJSONObject().getJSONArray("data");
                        Log.d("FbFriends", "getRawResponse:" + friends);//friends?

                        ArrayList<JSONObject> friendsList = new ArrayList<JSONObject>();

                        for(int i = 0; i<friends.length(); i++) {
                            friendsList.add(friends.getJSONObject(i));
                        }

                        FriendsAdapter adapter = new FriendsAdapter(getActivity(), friendsList);
                        adapter.setOnClickMovie(new FriendsAdapter.OnClickMovie() {
                            @Override
                            public void onClick() {
                                dismiss();
                            }
                        });

                        mList.setAdapter(adapter);

                        if(friendsList.isEmpty()) {
                            mEmptyResponse.setVisibility(View.VISIBLE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    mEmptyResponse.setText(getActivity().getString(R.string.error_downloading_friends));
                    mEmptyResponse.setVisibility(View.VISIBLE);
                }
                mLoading.setVisibility(View.INVISIBLE);
            }
        });

        request.executeAsync();
    }

    private GraphRequest createRequest(String userID, Set<String> extraFields) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newGraphPathRequest(
                accessToken, userID + "/friends", null);

        Set<String> fields = new HashSet<String>(extraFields);
        String[] requiredFields = new String[]{
                "id",
                "name"
        };
        fields.addAll(Arrays.asList(requiredFields));

        Bundle parameters = request.getParameters();
        parameters.putString("fields", TextUtils.join(",", fields));
        request.setParameters(parameters);

        return request;
    }
    /*
                            END FACEBOOK FRIENDS
     */

    public void downloadMyWants(Customers currentUser){
        RequestParams params = new RequestParams();
        int customerId = this.currentUser.getIdCustomer();

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

                    if(items.isEmpty()) {
                        mEmptyResponse.setVisibility(View.VISIBLE);

                    }
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

    public void downloadMyCommentedMovies(Customers currentUser){
        RequestParams params = new RequestParams();
        int customerId = this.currentUser.getIdCustomer();

        // Show Progress Dialog
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/rate/user/"+customerId+"?";
        Log.d(getTag(),"GET!"+link);
        client.get(link, params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                Log.d(getTag(),"response:"+response);

                ArrayList<Rate> items = null;


                try {
                    // JSON Object
                    Log.d(getTag(), "response:" + response);
                    JSONArray obj = new JSONArray(response);

                    items = new ArrayList<Rate>();

                    int size = obj.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject item = obj.getJSONObject(i);

                        Rate n = Rate.parseEntity(item,true);
                        items.add(n);
                    }

                } catch (JSONException e) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error Occured [Server's JSON response might be invalid]!", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                if (items != null) {
                    CommentedMoviesAdapter moviesAdapter = new CommentedMoviesAdapter(getActivity(), items);
                    moviesAdapter.setOnClickMovie(new CommentedMoviesAdapter.OnClickMovie() {
                        @Override
                        public void onClick() {
                            dismiss();
                        }
                    });

                    mList.setAdapter(moviesAdapter);


                    if(items.isEmpty()) {
                        mEmptyResponse.setVisibility(View.VISIBLE);

                    }
                }

                mLoading.setVisibility(View.INVISIBLE);
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {

                mLoading.setVisibility(View.INVISIBLE);
                Utils.showAsyncError(getActivity(), statusCode, error, content);
            }
        });
    }

    public void downloadReliefs(){
        RequestParams params = new RequestParams();
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
