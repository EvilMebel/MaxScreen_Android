package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;

/**
 * TODO: document your custom view class.
 */
public class DateButton extends Button {

    private TextPaint mTextPaint;
    private float mTextWidth;
    private float mTextHeight;

    public DateButton(Context context) {
        super(context);
        init(null, 0);
    }

    public DateButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public DateButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.DateButton, defStyle, 0);


        a.recycle();

        // Set up a default TextPaint object
        mTextPaint = new TextPaint();
        mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextAlign(Paint.Align.LEFT);

        // Update TextPaint and text measurements from attributes
        invalidateTextPaintAndMeasurements();

        setDate(Calendar.getInstance());
        setBackgroundResource(R.color.calendar_unselected);
        setTextColor(getResources().getColor(R.color.calendar_text));
        setPadding(0,0,0,0);
    }

    private void invalidateTextPaintAndMeasurements() {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = fontMetrics.bottom;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
    }

    public void setDate(Calendar c) {
        /*String day = c.get(Calendar.)
        String mounth c.get(DateFormat.Field.MONTH);
        String dayOfWeek;*/
        setText("PN\n1 Kwie");
    }
}
