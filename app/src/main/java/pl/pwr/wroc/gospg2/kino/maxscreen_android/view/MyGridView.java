package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import java.util.Iterator;

import android.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class MyGridView extends ViewGroup {
	private int pixelWidth;
	private int pihelHeight;
	
	private int colCount = 3;
	private int rowCount = 3;
	
	Drawable unfocused = null;

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
		
		int white = getResources().getColor(R.color.white);
		int black = Color.argb(255, 220, 220, 220);//getResources().getColor(android.R.color.black);
		int size = colCount *  rowCount;
		for(int i = 0; i<size; i++ ) {
			View v = new View(getContext());
			if(i%2 == 1)
				v.setBackgroundColor(white);
			else
				v.setBackgroundColor(black);
			
			addView(v);
		}
		
		
		//prepare drawable for unfocused rects
		//unfocused = getResources().getbac
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int mode = MeasureSpec.getMode(widthMeasureSpec);
		if(colCount!=0) {
			int frameW = MeasureSpec.getSize(widthMeasureSpec)/colCount;
			int frameH = MeasureSpec.makeMeasureSpec(10,MeasureSpec.UNSPECIFIED);


			int count = getChildCount();
			for(int i = 0; i<count; i++) {
				View v = getChildAt(i);
				v.measure(frameW, frameH);
				
			}
		}
		setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if(colCount!=0) {
            int count = getChildCount();
            //find max height
            int max = 0;
            for(int i = 0; i<count; i++) {
                View v = getChildAt(i);
                int h = v.getMeasuredHeight();
                if(h>max) {
                    max = h;
                }
            }


			int frameW = (r-l)/colCount;
			//int frameH = (b-t)/rowCount;
			int row = 0;
			int col = 0;
			

			for(int i = 0; i<count; i++) {
				View v = getChildAt(i);
				v.layout(col*frameW,row*max,
						(col+1)*frameW,(row+1)*max);
				
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
	
	
	

}
