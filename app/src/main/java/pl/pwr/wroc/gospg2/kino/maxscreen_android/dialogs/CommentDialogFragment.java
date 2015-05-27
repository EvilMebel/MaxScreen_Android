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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.ReliefsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.WantMoviesAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Customers;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Rate;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.net.Net;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.LevelBar;
import roboguice.fragment.RoboDialogFragment;
import roboguice.inject.InjectView;


public class CommentDialogFragment extends RoboDialogFragment {
    @InjectView (R.id.my_mark_stars)
    LevelBar mMarkStars;
    @InjectView (R.id.my_comment)
    EditText mMyComment;

    @InjectView (R.id.add_comment)
    Button mAddComment;

    @InjectView (R.id.loading)
    View mLoading;




    Rate mMyRate;
    CommentMethod commentMethod;
    OnRateReady onRateReady;


    public enum CommentMethod {
        UPDATE_PUT,
        CREATE_POST
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
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

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

        getDialog().setCanceledOnTouchOutside(true);
        setCancelable(true);

        mMarkStars.setCurrentLevel(10);
        fixUI();
    }

    private void fixUI() {
        if(mMarkStars!=null) {
            mMyComment.setText(mMyRate.getComment());
            mMarkStars.setCurrentLevel(mMyRate.getRate());
            mMarkStars.setEnabled(true);
            mMarkStars.setOnStarSelected(new LevelBar.OnStarSelected() {
                @Override
                public void onStarSelected(int rate) {
                    mMyRate.setRate(rate);
                    mMarkStars.setCurrentLevel(rate);

                }
            });
            mAddComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(commentMethod==CommentMethod.CREATE_POST) {
                        setLoadingMode();
                        createComment(mMyRate);
                    } else if (commentMethod==CommentMethod.UPDATE_PUT) {
                        setLoadingMode();
                        updateComment(mMyRate);
                    } else {
                        dismiss();
                    }

                }
            });
        }

        if(commentMethod!=null && commentMethod==CommentMethod.UPDATE_PUT){
            mAddComment.setText(getActivity().getString(R.string.edit_rate));
        }
    }

    private void setLoadingMode() {
        mLoading.setVisibility(View.VISIBLE);
        mMyComment.setEnabled(false);
        mAddComment.setEnabled(false);
        mAddComment.setVisibility(View.GONE);
        mMarkStars.setEnabled(false);
    }

    private void createComment(Rate rate) {
        RequestParams params = new RequestParams();


        JSONObject jsonParams = prepareJSON();

        AsyncHttpClient client = new AsyncHttpClient();


        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String link = Net.dbIp + "/rate";
        Log.d("CommentFragment", "POST!" + link);
        Log.d("CommentFragment", "POST!entity" + jsonParams.toString());
        Log.d("CommentFragment", "POST!entity:" + entity);
        client.post(getActivity(), link, entity, "application/json",

                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //todfokjfgnbfkjn
                        Log.d(getTag(), "response:" + response);
                        Toast.makeText(getActivity(), "Komentarz dodany!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        Utils.showAsyncError(getActivity(), statusCode, error, content);
                        mMyRate = null;
                        dismiss();
                    }
                });
    }

    private void updateComment(Rate rate) {
        JSONObject jsonParams = prepareJSON();
        try {
            jsonParams.put(Rate.IDRATE,rate.getIdRate());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();


        StringEntity entity = null;
        try {
            entity = new StringEntity(jsonParams.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String link = Net.dbIp + "/rate/"+rate.getIdRate();
        Log.d("CommentFragment","PUT!" + link);
        Log.d("CommentFragment","PUT!entity" + jsonParams.toString());
        Log.d("CommentFragment","PUT!entity:" + entity);
        client.put(getActivity(), link, entity, "application/json",

                new AsyncHttpResponseHandler() {


                    // When the response returned by REST has Http response code '200'
                    @Override
                    public void onSuccess(String response) {
                        // Hide Progress Dialog
                        //todfokjfgnbfkjn
                        Log.d(getTag(), "response:" + response);
                        Toast.makeText(getActivity(), "Komentarz zaktualizowany!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                    // When the response returned by REST has Http response code other than '200'
                    @Override
                    public void onFailure(int statusCode, Throwable error,
                                          String content) {
                        Utils.showAsyncError(getActivity(), statusCode, error, content);
                        mMyRate = null;
                        dismiss();
                    }
                });
    }

    private JSONObject prepareJSON() {
        mMyRate.setComment(mMyComment.getText().toString());
        mMyRate.setRate((int)mMarkStars.getCurrentLevel());

        Rate aRate = mMyRate;
        JSONObject rate = new JSONObject();
        JSONObject customer = new JSONObject();
        JSONObject movie = new JSONObject();

        try {
            customer.put(Customers.IDCUSTOMER,aRate.getCustomers_idCustomer());
            movie.put(Movie.IDMOVIE,aRate.getMovie_idMove());

            rate.put(Rate.MOVIE_IDMOVE,movie);
            rate.put(Rate.CUSTOMERS_IDCUSTOMER,customer);

            rate.put(Rate.COMMENT,aRate.getComment());
            rate.put(Rate.RATE,aRate.getRate());
        } catch (JSONException e) {
            e.printStackTrace();
            rate = null;
        }

        return rate;
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
        return inflater.inflate(R.layout.dialog_fragment_comment, container, false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(onRateReady!=null) {
            onRateReady.returnRate(mMyRate, commentMethod);
        }

        if(mOnDialogListener != null) {
            mOnDialogListener.onDismiss();
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        mMyRate = null;
    }

    public onDialogListener getmOnDialogListener() {
        return mOnDialogListener;
    }

    public void setmOnDialogListener(onDialogListener mOnDialogListener) {
        this.mOnDialogListener = mOnDialogListener;
    }

    public OnRateReady getOnRateReady() {
        return onRateReady;
    }

    public void setOnRateReady(OnRateReady onRateReady) {
        this.onRateReady = onRateReady;
    }

    public CommentMethod getCommentMethod() {
        return commentMethod;
    }

    public void setCommentMethod(CommentMethod commentMethod) {
        this.commentMethod = commentMethod;
    }

    public Rate getmMyRate() {
        return mMyRate;
    }

    public void setmMyRate(Rate mMyRate) {
        this.mMyRate = mMyRate;
    }

    public interface OnRateReady {
        public void returnRate(Rate rate, CommentMethod commentMethod);
    }

}
