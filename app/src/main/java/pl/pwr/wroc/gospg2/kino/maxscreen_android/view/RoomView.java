package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Evil on 2015-05-08.
 */
public class RoomView extends ViewGroup {
    private int seatsCountX;
    private int seatsCountY;

    private float scale = 1f;

    private int offsetX;
    private int offsetY;

    private boolean roomLoaded = false;


    private boolean TEST_MODE = true;
    private String tag = "RoomView";


    public RoomView(Context context) {
        super(context);
        init(null);
    }

    public RoomView(Context context, int seatsCountX, int seatsCountY) {
        super(context);
        this.seatsCountX = seatsCountX;
        this.seatsCountY = seatsCountY;
        init(null);
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RoomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if(TEST_MODE) {
            initTest();
        }
    }

    private void initTest() {
        seatsCountX = 10;
        seatsCountY = 5;
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int count = getChildCount();
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        int width = SeatView.STANDARD_WIDTH;
        int height = SeatView.STANDARD_HEIGHT;
        for(int i = 0; i<count; i++) {
            SeatView view = (SeatView) getChildAt(i);

            left = (int) (view.getSeatPositionX() * width + offsetX);
            top = (int) (view.getSeatPositionY() * height + offsetY);
            right = left + view.getScaledWidth();
            bottom = top + view.getScaledHeight();
            Log.d(tag,"Seat:l="+left +" t=" + top +" r=" +right + " b=" +bottom);
            view.layout(left,top,right,bottom);

        }
    }
}
