package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.RoomFileReader;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Tickets;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.SeatClickEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Utils;

/**
 * Created by Evil on 2015-05-08.
 */
public class RoomView extends ViewGroup implements View.OnTouchListener {

    GestureDetector gestureDetector;
    private int STANDARD_PADDING = -1;

    private int seatsCountX;
    private int seatsCountY;

    private float scale = 1f;


    private int startTouchX;
    private int startTouchY;


    private int lastOffsetX;
    private int lastOffsetY;

    private int currentOffsetXPlus;
    private int currentOffsetYPlus;

    private boolean roomLoaded = false;


    private boolean TEST_MODE = false;
    private String tag = "RoomView";


    private int maxX;
    private int maxY;
    private float startScale = 1;


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
        if(STANDARD_PADDING==-1) {
            STANDARD_PADDING = getResources().getDimensionPixelSize(R.dimen.padding_tiny);
        }

        seatsCountX = 10;
        seatsCountY = 5;

        if(TEST_MODE && !isInEditMode()) {
            initTest();
            //RoomFileReader.openFile(this);
        }

        setOnTouchListener(this);
        gestureDetector = new GestureDetector(getContext(), new GestureListener());
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

        int width = (int) (SeatView.STANDARD_WIDTH * scale);
        int height = (int) (SeatView.STANDARD_HEIGHT * scale);
        STANDARD_PADDING = (int) (width * 0.05f);
        for(int i = 0; i<count; i++) {
            SeatView view = (SeatView) getChildAt(i);

            left = (int) (view.getSeatPositionX() * width + lastOffsetX + currentOffsetXPlus) + STANDARD_PADDING;
            top = (int) (view.getSeatPositionY() * height + lastOffsetY + currentOffsetYPlus) + STANDARD_PADDING;
            right = left + view.getScaledWidth() - STANDARD_PADDING * 2;
            bottom = top + view.getScaledHeight() - STANDARD_PADDING * 2;
            //Log.d(tag,"Seat:l="+left +" t=" + top +" r=" +right + " b=" +bottom);
            view.layout(left,top,right,bottom);

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean doubleTap =gestureDetector.onTouchEvent(event);

        if(true) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startTouchX = (int) event.getX();
                    startTouchY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_UP:
                    lastOffsetY += currentOffsetYPlus;
                    lastOffsetX += currentOffsetXPlus;

                    currentOffsetYPlus = 0;
                    currentOffsetXPlus = 0;

                    startTouchY = 0;
                    startTouchX = 0;
                    break;
                case MotionEvent.ACTION_MOVE:
                    currentOffsetXPlus = (int) (event.getX() - startTouchX);
                    currentOffsetYPlus = (int) (event.getY() - startTouchY);
                    break;
            }


            int offX = currentOffsetXPlus + lastOffsetX;
            int offY = currentOffsetYPlus + lastOffsetY;
            //Log.d(tag, "offset:" + offX + "x" + offY);
            requestLayout();
        }

        return true;
    }

    public void setMaxValues(int maxX, int maxY) {
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public void fitRoomToScreen(int screenWidth) {
        float scale = (float)screenWidth/(maxX*SeatView.STANDARD_WIDTH);
        /*float scale = (float)screenWidth/maxX;
        scale = SeatView.STANDARD_WIDTH/scale;*/
        startScale = scale;
        Log.d(tag,"setFitScale=" + scale);
        setScale(scale);
    }

    public void setScale(float scale) {
        this.scale = scale;
        int size = getChildCount();
        for(int i =0; i<size; i++) {
            SeatView seatView = (SeatView) getChildAt(i);
            seatView.setScale(scale);

        }

    }

    public void zoomIn() {
        scale *= 1.2;
        if(scale>2.5f)
            scale = 2.5f;

        setScale(scale);
        requestLayout();
    }

    public void zoomOut() {
        scale *= 0.8;
        if(scale<startScale)
            scale = startScale;

        setScale(scale);
        requestLayout();
    }

    public SeatView getSeatClicked(MotionEvent e) {
        int count = getChildCount();
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        int x = (int) e.getX();
        int y = (int) e.getY();

        int width = (int) (SeatView.STANDARD_WIDTH * scale);
        int height = (int) (SeatView.STANDARD_HEIGHT * scale);
        STANDARD_PADDING = (int) (width * 0.05f);
        for(int i = 0; i<count; i++) {
            SeatView view = (SeatView) getChildAt(i);
/*

            left = (int) (view.getSeatPositionX() * width + lastOffsetX + currentOffsetXPlus) + STANDARD_PADDING;
            top = (int) (view.getSeatPositionY() * height + lastOffsetY + currentOffsetYPlus) + STANDARD_PADDING;
            right = left + view.getScaledWidth() - STANDARD_PADDING * 2;
            bottom = top + view.getScaledHeight() - STANDARD_PADDING * 2;
*/

            //user cant click row number
            //if(view.getStatus()!= SeatView.SeatStat.ONLY_VISUALIZATION) {

            //gtrgtr
                left = view.getLeft();
                top = view.getTop();
                right = view.getRight();
                bottom = view.getBottom();
                //Log.d(tag, "Seat:l=" + left + " t=" + top + " r=" + right + " b=" + bottom);

                if (y > top && y < bottom && x > left && x < right) {
                    Log.d(tag, "clicked view with index =" + i);
                    return view;
                }
            //}
        }

        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isInEditMode()) {
            canvas.drawColor(Color.CYAN);
        }

        super.onDraw(canvas);
    }

    public void loadRoom(Halls hall) {
        RoomFileReader.openFile(this, hall.getStructureFile());
    }

    public void clickSeat(SeatView seatView) {
        switch (seatView.getStatus()) {
            case MY_CHOICE:
                seatView.setStatus(SeatView.SeatStat.FREE);
                break;
            case FREE:
                seatView.setStatus(SeatView.SeatStat.MY_CHOICE);
                break;
        }
    }

    public void setTakenSeats(List<Tickets> seats) {

        for(int i = 0; i<seats.size(); i++) {
            Tickets t = seats.get(i);

            int count = getChildCount();
            for(int j = 0; j<count; j++) {
                SeatView s = (SeatView) getChildAt(j);

                if(s.getSeatRow()== t.getRow() && s.getSeatCol()==t.getLine()) {
                    s.setStatus(SeatView.SeatStat.TAKEN);
                    Log.d("Room","Taken seat at " + t.getRow() + " line:" + t.getLine());
                    //found!
                    j = count;
                }
            }

        }
    }

    public void refreshRoom() {
        int count = getChildCount();
        for(int i =0; i<count; i++) {
            SeatView seatView = (SeatView) getChildAt(i);
            if(seatView.getStatus()!= SeatView.SeatStat.ONLY_VISUALIZATION)
                seatView.setStatus(SeatView.SeatStat.FREE);
        }
    }


    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        /*
        onup
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
*/
        // event when double tap occurs
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            zoomIn();

            Log.d("Double Tap", "Tapped at: (" + x + "," + y + ")");

            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            Log.d("Double Tap", "Single tap at: (" + x + "," + y + ")");

            SeatView seatView = getSeatClicked(e);

            if(seatView!=null && seatView.getStatus()!= SeatView.SeatStat.TAKEN) {
                Log.d(tag,"view clicked " + seatView);
                MaxScreen.getBus().post(new SeatClickEventBus(seatView));
            } else {
                Log.d(tag,"no action on single tap");
            }

            return true;
        }
    }

}
