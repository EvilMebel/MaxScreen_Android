package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;


public class LevelBar extends View implements View.OnTouchListener {

	private static boolean drawableInited = false;
	private static Drawable starFull;
	private static Drawable starHalf;
	private static Drawable starEmpty;

	private int maxLevel = 10;
	private float currentLevel = 7.5f;
	
	private int padding = getResources().getDimensionPixelSize(R.dimen.padding_medium);
	private Rect r;
	
	private int mHeight;
	private int mWidth;
	OnStarSelected onStarSelected;

	public LevelBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public LevelBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public LevelBar(Context context) {
		super(context);
		init();
	}
	
	private void init() {
		if(!drawableInited) {
			starEmpty = getResources().getDrawable(R.drawable.ic_star_empty);
			starHalf= getResources().getDrawable(R.drawable.ic_star_half);
			starFull= getResources().getDrawable(R.drawable.ic_star);
		}

		mHeight = getResources().getDimensionPixelSize(R.dimen.stars_height);
		padding = getResources().getDimensionPixelSize(R.dimen.padding_tiny);
		mWidth = mHeight*10 + padding * 2;

		setOnTouchListener(this);
		r = new Rect();
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int MesHei = MeasureSpec.makeMeasureSpec(mHeight + 2*padding, MeasureSpec.AT_MOST);
		int MesWid = MeasureSpec.makeMeasureSpec(mWidth, MeasureSpec.AT_MOST);
		super.onMeasure(MesWid, MesHei);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int top = padding;
		int left = padding;
		for(int i = 0; i<maxLevel; i++) {

			int mark = i+1;
			Drawable d = mark <= currentLevel ? starFull : starEmpty;

			if(currentLevel>mark && currentLevel<mark+1) {
				d = starHalf;
			}
			r.set(left, top, left + mHeight, top + mHeight);
			d.setBounds(r);
			d.draw(canvas);

			left+=mHeight;
		}
	}

	public void addLevel() {
		if(currentLevel<maxLevel)
			currentLevel++;
		requestLayout();
	}

	public void setLevel(int level) {
		currentLevel = level;
		if(currentLevel>maxLevel)
			currentLevel = maxLevel;
		requestLayout();
	}

	public int getMaxLevel() {
		return maxLevel;
	}

	public void setMaxLevel(int maxLevel) {
		this.maxLevel = maxLevel;
	}

	public float getCurrentLevel() {
		return currentLevel;
	}

	public void setCurrentLevel(float currentLevel) {
		this.currentLevel = currentLevel;
		requestLayout();
		invalidate();
	}

	public OnStarSelected getOnStarSelected() {
		return onStarSelected;
	}

	public void setOnStarSelected(OnStarSelected onStarSelected) {
		this.onStarSelected = onStarSelected;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		if (onStarSelected != null && isEnabled()) {
			int mark = (int) ((event.getX() - padding) / mHeight);
			currentLevel = mark + 1;


			if (currentLevel < 1)
				currentLevel = 1;

			if (currentLevel > 10)
				currentLevel = 10;

			setCurrentLevel(currentLevel);
			onStarSelected.onStarSelected((int) currentLevel);
		}


		return false;
	}

	public interface OnStarSelected {
		public void onStarSelected(int rate);
	}
}
