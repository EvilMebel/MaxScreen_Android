package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.old.FilmFrame;

/**
 * Created by Evil on 2015-04-01.
 */
public class CalendarListAdapter extends BaseAdapter {

    private ArrayList<FilmFrame> mItems;
    //ArrayList<News> mIdMap = new ArrayList<News>();
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

    public CalendarListAdapter(FragmentActivity fragmentActivity, ArrayList<FilmFrame> items) {
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


    @Override
    public View getView(int position, View currentView, ViewGroup viewGroup) {
        if(currentView==null) {
/*
            @InjectView(R.id.thumb)
            ImageView mThumb;

            @InjectView (R.id.title)
            TextView mTitle;

            @InjectView (R.id.grid)
            MyGridView mGrid;*/


            currentView = View.inflate(mContext, R.layout.calendar_item, null);
           /* ImageView thumbnailView = (ImageView) currentView.findViewById(R.id.thumbnail_view);
            TextView messageView = (TextView) currentView.findViewById(R.id.message_view);
            String text = mContext.getString(R.string.text);

            Display display = ((MainActivity) mContext).getWindowManager().getDefaultDisplay();
            FlowTextHelper.tryFlowText(text, thumbnailView, messageView, display);*/
        }

        return currentView;
    }

    public void remove(Object item) {
        mItems.remove(item);
    }
}
