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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenNewsEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenProfileEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.preferences.ApplicationPreferences;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.inject.InjectView;

public class EditAccountFragment extends RoboEventFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    @InjectView (R.id.edit_name)EditText mName;
    @InjectView (R.id.edit_surname)EditText mSurname;
    @InjectView (R.id.edit_nick)EditText mNick;
    @InjectView (R.id.edit_phone)EditText mPhone;

    @InjectView (R.id.loading) View mLoading;
    @InjectView (R.id.edit_save)Button mSave;


    @InjectView (R.id.edit_avatar)EditText mAvatar;
    private Customers me;


    public static EditAccountFragment newInstance(String param1, String param2) {
        EditAccountFragment fragment = new EditAccountFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditAccountFragment() {
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
        return inflater.inflate(R.layout.fragment_edit_account, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
        setListeneres();
    }

    private void setListeneres() {
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (me != null)
                    saveData(me);
            }
        });
    }

    private void saveData(final Customers me) {
        lockViews(true);
        me.setName(mName.getText().toString());
        me.setSurname(mSurname.getText().toString());
        me.setNick(mNick.getText().toString());
        me.setTelefon(mPhone.getText().toString());

        JSONObject customer = me.getJSON();

        Log.d(getTag(),"PUTentity:"+customer);


        AsyncHttpClient client = new AsyncHttpClient();
        String link = Net.dbIp + "/customer/"+me.getIdCustomer();

        StringEntity entity = null;
        try {
            entity = new StringEntity(customer.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        client.put(getActivity(), link, entity, "application/json",

                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        mLoading.setVisibility(View.GONE);
                        ApplicationPreferences.getInstance().setCurrentCustomer(me);
                        MaxScreen.getBus().post(new OpenProfileEventBus(me.getIdCustomer()));

                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        lockViews(false);
                        Toast.makeText(getActivity(),getActivity().getString(R.string.cant_save_user_changes),Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void lockViews(boolean enable) {
        mName.setEnabled(enable);
        mSurname.setEnabled(enable);
        mNick.setEnabled(enable);
        mPhone.setEnabled(enable);
        mSave.setEnabled(enable);
        if(enable) {
            mSave.setVisibility(View.VISIBLE);
            mLoading.setVisibility(View.GONE);
        } else {
            mSave.setVisibility(View.GONE);
            mLoading.setVisibility(View.VISIBLE);
        }

    }

    private void loadData() {
        mLoading.setVisibility(View.GONE);

        me = ApplicationPreferences.getInstance().getCurrentCustomer();
        if(me!=null) {
            mName.setText(me.getName());
            mSurname.setText(me.getSurname());
            mPhone.setText(me.getTelefon());
            mNick.setText(me.getNick());
        } else {
            //error!
            MaxScreen.getBus().post(new OpenNewsEventBus());
        }
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
