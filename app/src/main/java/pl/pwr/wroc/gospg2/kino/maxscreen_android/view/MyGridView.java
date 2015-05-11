package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Halls;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenRoomEventBus;

public class MyGridView extends ViewGroup implements View.OnClickListener {
	private int pixelWidth;
	private int pihelHeight;
	
	private int colCount = 3;
	private int rowCount = 3;
	
	Drawable unfocused = null;
	List<Seance> seances;
	private int maxHeight;

	private boolean inAdapter = true;
	private String tag = "MyGridView";
	private Movie movie;

	public MyGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public MyGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyGridView(Context context) {
		super(context);
		init();
	}

	private void init() {
		
		int white = getResources().getColor(android.R.color.white);
		int black = Color.argb(255, 220, 220, 220);//getResources().getColor(android.R.color.black);
		int size = colCount *  rowCount;

		seances = new ArrayList<Seance>();
/*
		for (int j = 0; j < 4; j++) {
			Seance s = new Seance();
			if(!isInEditMode()) {
				s.setDate(new Date(System.currentTimeMillis()));// = new Time();
			}
			seances.add(s);
		}
		setSeances(seances);*/



/*
		for(int i = 0; i<size; i++ ) {
			View v = new View(getContext());
			if(i%2 == 1)
				v.setBackgroundColor(white);
			else
				v.setBackgroundColor(black);
			
			addView(v);
		}*/
		
		
		//prepare drawable for unfocused rects
		//unfocused = getResources().getbac
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		int mHeight = heightMeasureSpec;
		if(colCount!=0) {
			Log.d(tag,"size = " + MeasureSpec.getSize(widthMeasureSpec));
			int frameW = MeasureSpec.getSize(widthMeasureSpec)/colCount;
			int frameH = MeasureSpec.makeMeasureSpec(frameW/2,MeasureSpec.EXACTLY);

			int frameWMS = MeasureSpec.makeMeasureSpec(frameW,MeasureSpec.EXACTLY);


			int count = getChildCount();
			for(int i = 0; i<count; i++) {
				View v = getChildAt(i);
				v.measure(frameWMS, frameH);
				
			}

			mHeight = MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(frameH) * colCount,MeasureSpec.EXACTLY);
		}

		// = MeasureSpec.makeMeasureSpec()
		setMeasuredDimension(widthMeasureSpec, mHeight);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(colCount!=0) {
            int count = getChildCount();
            //find max height
            maxHeight = 0;
            for(int i = 0; i<count; i++) {
                View v = getChildAt(i);
                int h = v.getMeasuredHeight();
                if(h>maxHeight) {
                    maxHeight = h;
                }
            }


			int frameW = (r-l)/colCount;
			//int frameH = (b-t)/rowCount;
			int row = 0;
			int col = 0;
			

			for(int i = 0; i<count; i++) {
				View v = getChildAt(i);
				v.layout(col*frameW,row*maxHeight,
						(col+1)*frameW,(row+1)*maxHeight);
				
				//prepare position for next pixel
				col++; 
				if(col == colCount)
				{
					//next row
					row++;
					col = 0;
				}
			}
		}
	}
	
	/*public void setFrame(Frame f) {
		Log.d("screenView", "setFrame!");
		Iterator<Integer> pixels = f.iterator();
		//int c = f.getPixelAt(1, 1);
		
		int count = getChildCount();
		for(int i = 0 ; i < count; i++) {
			try {
				int color = pixels.next();
				
				View v = getChildAt(i);
//				if(c%2==1)
//					v.setBackgroundColor(color);//pixels.next());
//				else
//					v.setBackgroundColor(color2);//pixels.next());
				v.setBackgroundColor(color);
				
			} catch (Exception e){
				//e.printStackTrace();
				//Log.d("error", "null at frame's pixel in " + i+ " :(");
			}
		}
		invalidate();
	}*/

	public int getColCount() {
		return colCount;
	}

	public void setColCount(int colCount) {
		this.colCount = colCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	public int getPixelWidth() {
		return pixelWidth;
	}

	public int getPihelHeight() {
		return pihelHeight;
	}
	
	public void setScreenSize(int col, int row) {
		removeAllViews();
		colCount = col;
		rowCount = row;
		//refresh
		init();
	}

	public void showPosition(int x, int y) {
		int index = (y-1)*colCount + (x-1);
		View v = getChildAt(index);
		if(v!=null)
			v.setBackgroundColor(Color.BLUE);
		else
			Toast.makeText(getContext(), "show position null exception! :(", Toast.LENGTH_SHORT).show();
		
	}

	public void setSeances(List<Seance> seances, boolean fullInfo) {
		this.seances.clear();
		//clear all childrens
		removeAllViews();
		removeAllViewsInLayout();
		removeViewsCustom();

		if(seances!=null) {
			this.seances = seances;
			Log.d(tag, "new seansec for movie:" + seances.size());

			int textHeight = getResources().getDimensionPixelSize(R.dimen.calendar_seance_text);
			int viewHeight = getResources().getDimensionPixelSize(R.dimen.calendar_seance_height);

			int size = seances.size();
			for (int i = 0; i < size; i++) {
				TextView v = new TextView(getContext());
				//todo nicer text!

				if(fullInfo) {//print all data in movie info!

				} else {
					if (isInEditMode()) {
						v.setText(i + ":30");
					} else {
						v.setText(seances.get(i).getDateString());
						v.setText(i + ":30");
					}
				}

				//set view
				v.setTextSize(TypedValue.COMPLEX_UNIT_PX, textHeight);
				LayoutParams lp = v.getLayoutParams();
				if (lp != null) {
					lp.height = viewHeight;
				} else {
					Log.e("setSeances", "lp is null");
				}
				v.setGravity(Gravity.CENTER);

				//set id to get position
				v.setId(i);
				v.setOnClickListener(this);
				addView(v);
			}
		}
		rowCount = (seances.size()+2)/colCount;
		//refresh view
		requestLayout();
	}

	private void removeViewsCustom() {
		int size = getChildCount();
		Log.d(tag,"remove custom");
		for(int i =size-1; i>=0; i--) {
			Log.d(tag,"remove custom i=" +i);
			removeView(getChildAt(i));
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint mPaint = new Paint();
		mPaint.setColor(Color.BLUE);
		canvas.drawRect(0, 0, 50, 50, mPaint);
	}

	@Override
	public void onClick(View v) {
		Log.d(tag,"click seanse " + v.getId());
		//copy important data to app
		Seance seance = seances.get(v.getId());
		MSData.getInstance().setCurrentMovie(movie);
		MSData.getInstance().setSeance(seance);
		Halls room = new Halls();
		room.setIdHall(seance.getHalls_idHall());//todo async in RoomFragment
		MSData.getInstance().setRoom(room);

		//open room fragment - id is index of seance
		MaxScreen.getBus().post(new OpenRoomEventBus(movie, seance));
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}
}
