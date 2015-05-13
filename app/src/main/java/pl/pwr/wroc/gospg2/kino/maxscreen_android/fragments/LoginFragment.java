package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.CalendarListAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.LoadingDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToRegistrationBus;
import roboguice.fragment.RoboFragment;
import roboguice.inject.InjectView;

public class LoginFragment extends RoboEventFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView (R.id.email)
    EditText mEmailInput;

    @InjectView (R.id.pass)
    EditText mPasswordInput;

    //@InjectView (R.id.login)
    //Button mLogin;

    //@InjectView (R.id.fbLogin)
    //Button mFbLogin;

    @InjectView (R.id.register)
    Button mRegister;


    private LoadingDialogFragment mLoadingDialogFragment;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
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
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxScreen.getBus().post(new GoToRegistrationBus());
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void showLoadingDialog() {
        mLoadingDialogFragment = new LoadingDialogFragment();
        mLoadingDialogFragment.show(getFragmentManager(), null);
    }


    class BgAsyncTask extends AsyncTask<String, String, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showLoadingDialog();
        }

        protected ArrayList<Movie> doInBackground(String... args) {

            //todo

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(ArrayList<Movie> items) {
            /*if(items!=null) {
                mList.setAdapter(new CalendarListAdapter(getActivity(), items));
            } else {
                //else return empty
                mList.setAdapter(new CalendarListAdapter(getActivity(), new ArrayList<Movie>()));
                Toast.makeText(getActivity(), "Blad podczas wczytywania.\nPrzepraszamy :(", Toast.LENGTH_SHORT).show();
            }*/

            mLoadingDialogFragment.dismiss();


        }
    }




    private void hideLoadingDialog() {
        if(mLoadingDialogFragment!=null)
            mLoadingDialogFragment.dismiss();
    }


}
