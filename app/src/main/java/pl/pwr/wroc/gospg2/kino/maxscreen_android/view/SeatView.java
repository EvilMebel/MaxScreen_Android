package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;

/**
 * Created by Evil on 2015-05-08.
 */
public class SeatView extends TextView {

    public static int STANDARD_WIDTH = -1;
    public static int STANDARD_HEIGHT = -1;
    public static int STANDARD_TEXT_SIZE = -1;

    private int seatPositionX =0;
    private int seatPositionY =0;

    private int seatRow =1;
    private int seatCol =1;

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
            scaledWidth = STANDARD_WIDTH = getResources().getDimensionPixelSize(R.dimen.seat_width);
            scaledHeight = STANDARD_HEIGHT = getResources().getDimensionPixelSize(R.dimen.seat_height);
            STANDARD_TEXT_SIZE = getResources().getDimensionPixelSize(R.dimen.seat_text_size);
        }

        setTextSize(TypedValue.COMPLEX_UNIT_PX, STANDARD_TEXT_SIZE);
        setTextColor(android.R.color.white);

        setBackgroundResource(android.R.color.black);
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
}
