package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.SeatClickEventBus;

/**
 * Created by Evil on 2015-05-08.
 */
public class SeatView extends TextView {
    public enum SeatStat {
        FREE,
        MY_CHOICE,
        TAKEN,
        //UI FOR DISPLAYING ROW
        ONLY_VISUALIZATION
    }

    private SeatStat status = SeatStat.FREE;


    public static int STANDARD_WIDTH = -1;
    public static int STANDARD_HEIGHT = -1;
    public static int STANDARD_TEXT_SIZE = -1;
    public static int STANDARD_PADDING = -1;

    private int seatPositionX =0;
    private int seatPositionY =0;

    private int seatRow;
    private int seatCol;

    private int scaledWidth;
    private int scaledHeight;

    public SeatView(Context context) {
        super(context);
        init(null);
    }

    public SeatView(Context context, int seatPositionX, int seatPositionY, int seatRow, int seatCol) {
        super(context);
        this.seatPositionX = seatPositionX;
        this.seatPositionY = seatPositionY;

        this.seatRow = seatRow;
        this.seatCol = seatCol;
        init(null);

        setText("" + seatCol);

        Log.d("read", "add SEAT row=" + seatRow + " col=" + seatCol);

        //setOwnClickListener();
    }

    public SeatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeatView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SeatView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setGravity(Gravity.CENTER);

        if(STANDARD_WIDTH ==-1) {
            STANDARD_WIDTH = getResources().getDimensionPixelSize(R.dimen.seat_width);
            STANDARD_HEIGHT = getResources().getDimensionPixelSize(R.dimen.seat_height);
            STANDARD_TEXT_SIZE = getResources().getDimensionPixelSize(R.dimen.seat_text_size);
            STANDARD_PADDING = getResources().getDimensionPixelSize(R.dimen.padding_tiny);
        }

        scaledWidth = STANDARD_WIDTH;
        scaledHeight = STANDARD_HEIGHT;


        setTextSize(TypedValue.COMPLEX_UNIT_PX, STANDARD_TEXT_SIZE);
        setTextColor(Color.BLACK);

        setBackgroundByStatus();

    }

    private void setBackgroundByStatus() {
        int bgRes;
        switch (status) {
            default:
            case FREE:
                bgRes = R.drawable.seat_free;
                break;

            case MY_CHOICE:
                bgRes = R.drawable.seat_my_choose;
                break;

            case TAKEN:
                bgRes = R.drawable.seat_taken;
                setText("");
                break;

            case ONLY_VISUALIZATION:
                bgRes = android.R.color.transparent;
                break;
        }

        setBackgroundResource(bgRes);
    }

    public void setScale(float scale) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        scaledHeight = lp.height = (int) (STANDARD_HEIGHT * scale);
        scaledWidth = lp.width = (int) (STANDARD_WIDTH * scale);
        setTextSize(TypedValue.COMPLEX_UNIT_PX, STANDARD_TEXT_SIZE * scale);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.makeMeasureSpec(scaledWidth, MeasureSpec.EXACTLY);
        int height = MeasureSpec.makeMeasureSpec(scaledHeight, MeasureSpec.EXACTLY);

        super.onMeasure(width, height);
    }

    public int getSeatPositionX() {
        return seatPositionX;
    }

    public void setSeatPositionX(int seatPositionX) {
        this.seatPositionX = seatPositionX;
    }

    public int getSeatPositionY() {
        return seatPositionY;
    }

    public void setSeatPositionY(int seatPositionY) {
        this.seatPositionY = seatPositionY;
    }

    public int getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(int seatRow) {
        this.seatRow = seatRow;
    }

    public int getSeatCol() {
        return seatCol;
    }

    public void setSeatCol(int seatCol) {
        this.seatCol = seatCol;
    }

    public int getScaledWidth() {
        return scaledWidth;
    }

    public void setScaledWidth(int scaledWidth) {
        this.scaledWidth = scaledWidth;
    }

    public int getScaledHeight() {
        return scaledHeight;
    }

    public void setScaledHeight(int scaledHeight) {
        this.scaledHeight = scaledHeight;
    }

    public SeatStat getStatus() {
        return status;
    }

    public void setStatus(SeatStat status) {
        this.status = status;

        setBackgroundByStatus();
        if(getStatus()==SeatStat.ONLY_VISUALIZATION) {
            setText("" + getSeatRow());
        } else if(getStatus()== SeatStat.TAKEN) {
            setText("");
        } else  {
            setText("" + getSeatCol());
        }
    }
}
