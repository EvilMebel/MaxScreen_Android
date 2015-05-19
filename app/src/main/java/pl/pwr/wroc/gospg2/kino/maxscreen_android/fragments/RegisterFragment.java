package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs.LoadingDialogFragment;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.GoToLoginBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import roboguice.inject.InjectView;

public class RegisterFragment extends RoboEventFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @InjectView(R.id.name)
    EditText mName;

    @InjectView(R.id.surname)
    EditText mSurname;

    @InjectView(R.id.email)
    EditText mEmailInput;

    @InjectView (R.id.pass)
    EditText mPasswordInput;

    @InjectView (R.id.pass2)
    EditText mPasswordRepInput;

    @InjectView (R.id.register)
    Button mRegister;

    @InjectView (R.id.login)
    Button mLogin;

    @InjectView (R.id.fbLogin)
    Button mFbRegister;


    private LoadingDialogFragment mLoadingDialogFragment;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RegisterFragment() {
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
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
    }

    private void setListeners() {
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxScreen.getBus().post(new GoToLoginBus());
            }
        });

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    tryRegister();
                }
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

    private void hideLoadingDialog() {
        if(mLoadingDialogFragment!=null)
            mLoadingDialogFragment.dismiss();
    }

    private boolean validateInputs() {
        isntEmpty(mSurname);
        isntEmpty(mName);
        isntEmpty(mEmailInput);
        isntEmpty(mPasswordRepInput);
        isntEmpty(mPasswordInput);
        boolean ok =  isntEmpty(mSurname) && isntEmpty(mName) && isntEmpty(mEmailInput);
        return ok && checkPassword();
    }

    private boolean checkPassword() {
        boolean isOk = isntEmpty(mPasswordInput) && isntEmpty(mPasswordRepInput);

        if (isOk) {
            if (mPasswordInput.getText().toString().length() < 5) {
                isOk = false;
                Toast.makeText(getActivity(),"Haslo jest za krotkie!",Toast.LENGTH_SHORT).show();
            } else if(!mPasswordInput.getText().toString().equals(mPasswordRepInput.getText().toString())) {
                isOk = false;
                Toast.makeText(getActivity(),"Hasla nie sa takie same!",Toast.LENGTH_SHORT).show();
            } else if(isValidEmail(mPasswordInput.getText())) {
                isOk = false;
                Toast.makeText(getActivity(),"Nieprawidlowy adres email!",Toast.LENGTH_SHORT).show();
            }
        }

        setField(mPasswordInput, isOk);
        setField(mPasswordRepInput, isOk);
        return isOk;
    }

    private boolean isntEmpty(TextView tv) {
        if (tv.getText().toString().matches("")) {
            setFieldError(tv);
            return false;
        } else {
            setFieldGood(tv);
            return true;
        }
    }

    private void setField(TextView tv, boolean isOk) {
        if (isOk) {
            setFieldGood(tv);
        } else {
            setFieldError(tv);
        }
    }

    private void setFieldError(TextView tv) {
        tv.setBackgroundResource(R.color.input_background_error);
        tv.setTextColor(Color.WHITE);
    }

    private void setFieldGood(TextView tv) {
        tv.setBackgroundResource(R.color.input_background);
        tv.setTextColor(Color.BLACK);
    }

    public boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    public void tryRegister(){
        RequestParams params = new RequestParams();
        params.add(Customers.NAME,mName.getText().toString());
        params.add(Customers.SURNAME,mSurname.getText().toString());
        params.add(Customers.E_MAIL, mEmailInput.getText().toString());
        params.add(Customers.PASSMD5, Utils.MD5(mPasswordInput.getText().toString()));
        // Show Progress Dialog
        showLoadingDialog();
        // Make RESTful webservice call using AsyncHttpClient object
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Net.oldDbIp + "/register", params, new AsyncHttpResponseHandler() {


            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //todfokjfgnbfkjn
                Log.d(getTag(), "response:" + response);

                boolean status = true;
                String msg = "";

                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getBoolean("status");

                    if(!status) {
                        msg = object.getString("error_msg");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if(status) {
                    msg = "Rejestracja powiodla sie!";

                }
                //todo autologin?

                Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();

                hideLoadingDialog();

                if(status) {
                    MaxScreen.getBus().post(new GoToLoginBus());
                }
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

}
