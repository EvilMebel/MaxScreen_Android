package pl.pwr.wroc.gospg2.kino.maxscreen_android.view;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

/**
 * Created by Evil on 2015-05-10.
 */
public class DateViewPager extends ViewPager {
    private GregorianCalendar calendar;
    private DatePagerAdapter adapter;

    public DateViewPager(Context context) {
        super(context);
        init();
    }

    public DateViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {

        calendar = new GregorianCalendar();
        adapter = new DatePagerAdapter(getContext());
        setAdapter(adapter);

        setCurrentItem(1, true);
        setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        prevDay();
                        break;
                    case 2:
                        nextDay();
                        break;

                }
                setCurrentItem(1,false);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public void nextDay() {
        calendar.add(GregorianCalendar.DAY_OF_WEEK, 7);
        prepareDateString();
    }

    public void prevDay() {
        calendar.add(GregorianCalendar.DAY_OF_WEEK, -7);
        prepareDateString();
    }

    private void prepareDateString() {
        GregorianCalendar end = (GregorianCalendar) calendar.clone();
        end.add(GregorianCalendar.DAY_OF_WEEK, 6);

        String s = Converter.gregToString(calendar) + " - "
                + Converter.gregToString(end);
        //todo ? doci_date.setText(s);
    }





    /*
                    adapter

     */

    private class DatePagerAdapter extends PagerAdapter {
        private String tag = "DatePagerAdapter";
        private Context mContext;

        public DatePagerAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Log.d(tag,"instantiateItem");
            TextView view = new TextView(mContext);

            view.setGravity(Gravity.CENTER);
            //view.setFont(mContext, 4);
            view.setTextColor(Color.BLACK);
            //view.setBackgroundColorRes(R.color.want_to_button_background_alpha);

            GregorianCalendar cal;
            cal = new GregorianCalendar();
            cal.setTime(calendar.getTime());

            switch (position) {
                case 0:
                    cal.add(GregorianCalendar.DAY_OF_WEEK, 7);
                    break;
                case 1:
                    break;
                case 2: //next day
                    cal.add(GregorianCalendar.DAY_OF_WEEK, -7);
                    break;
            }
            view.setText(Converter.gregToMySQLformat(calendar));

            //view.setText("lol2");
            container.addView(view);

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }
    }

}
