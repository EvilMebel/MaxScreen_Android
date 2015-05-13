package pl.pwr.wroc.gospg2.kino.maxscreen_android.dialogs;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import roboguice.fragment.RoboDialogFragment;


public class LoadingDialogFragment extends RoboDialogFragment {
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
        return inflater.inflate(R.layout.dialog_fragment_loading, container, false);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if(mOnDialogListener != null) {
            mOnDialogListener.onDismiss();
        }
    }
}
