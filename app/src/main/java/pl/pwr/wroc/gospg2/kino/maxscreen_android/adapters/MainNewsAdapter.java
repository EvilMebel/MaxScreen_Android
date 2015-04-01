package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MainActivity;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.*;

/**
 * Created by Evil on 2015-03-31.
 */
public class MainNewsAdapter extends BaseAdapter {

    private ArrayList<News> mItems;
    ArrayList<News> mIdMap = new ArrayList<News>();
    int layoutResId;
    Context mContext;
    private FragmentManager mFragmentManager;

    /*public ResultListAdapter(Context context, FragmentManager mFragmentManager, int layoutResId,
                              List<String> objects, View.OnTouchListener listener) {
        super(context, layoutResId, objects);
        this.mFragmentManager = mFragmentManager;
        this.mContext = context;
        this.layoutResId = layoutResId;
        mTouchListener = listener;
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), i);
        }
    }*/

    public MainNewsAdapter(FragmentActivity fragmentActivity, ArrayList<News> items) {
        mContext = fragmentActivity;
        mItems = items;
        mFragmentManager = fragmentActivity.getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }


    /*@Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //View view = super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.result_item, parent,
                false);
        //view.requestLayout();
        Log.d("rla","item id:" + position);
        ViewPager vp = (ViewPager) view.findViewById(R.id.image_pager);

        vp.setAdapter(new ResItemPagerAdapter(getContext()));
        vp.setId(position);
        vp.setCurrentItem(1,false);

        if (view != convertView) {
            // Add touch listener to every new view to track swipe motion
            view.setOnTouchListener(mTouchListener);
        }
        return view;
    }*/
    @Override
    public View getView(int position, View currentView, ViewGroup viewGroup) {
        if(currentView==null) {
            currentView = View.inflate(mContext, R.layout.main_news_item, null);
            ImageView thumbnailView = (ImageView) currentView.findViewById(R.id.thumbnail_view);
            TextView messageView = (TextView) currentView.findViewById(R.id.message_view);
            String text = mContext.getString(R.string.text);

            Display display = ((MainActivity) mContext).getWindowManager().getDefaultDisplay();
            FlowTextHelper.tryFlowText(text, thumbnailView, messageView, display);
        }

        return currentView;
    }

    public void remove(Object item) {
        mItems.remove(item);
    }
}
