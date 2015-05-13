package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MainActivity;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.*;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.MyGridView;

/**
 * Created by Evil on 2015-03-31.
 */
public class MainNewsAdapter extends BaseAdapter {

    private ArrayList<News> mItems;
    ArrayList<News> mIdMap = new ArrayList<News>();
    int layoutResId;
    Context mContext;
    private FragmentManager mFragmentManager;

    private DisplayImageOptions mDisplayImageOptions;

    public MainNewsAdapter(FragmentActivity fragmentActivity, ArrayList<News> items) {
        mContext = fragmentActivity;
        mItems = items;
        mFragmentManager = fragmentActivity.getSupportFragmentManager();


        mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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

        ItemViewHolder itemViewHolder = null;


        if(currentView==null) {
            itemViewHolder = new ItemViewHolder();

            currentView = View.inflate(mContext, R.layout.main_news_item, null);

            itemViewHolder.image = (ImageView) currentView.findViewById(R.id.thumbnail_view);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.message_view);

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }

        News n = mItems.get(position);

        String text = null;

        if(n.getText()==null) {
            text = mContext.getString(R.string.text);
        } else {
            text = n.getText();
        }

        Display display = ((MainActivity) mContext).getWindowManager().getDefaultDisplay();
        //FlowTextHelper.tryFlowText(text, itemViewHolder.image, itemViewHolder.title, display);
        itemViewHolder.title.setText(n.getTopic() + "\n" + Converter.gregToString(n.getDate()) + "\n" + text  );

        setImage(n, itemViewHolder);

        return currentView;
    }


    private void setImage(News news, ItemViewHolder item) {
        if(news!=null && news.getImage()!=null) {
            String url = news.getImage();
            item.image.setImageBitmap(null);

            if (url != null) {
                ImageLoader.getInstance().displayImage(url, item.image, mDisplayImageOptions, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {
                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {
                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {
                    }
                });
            }
        }
    }

    public void remove(Object item) {
        mItems.remove(item);
    }

    private class ItemViewHolder {
        public ImageView image;
        public TextView title;
    }
}
