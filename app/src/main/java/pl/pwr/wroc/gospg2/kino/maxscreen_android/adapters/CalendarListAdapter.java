package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
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

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenMovieInfoEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.view.MyGridView;

/**
 * Created by Evil on 2015-04-01.
 */
public class CalendarListAdapter extends BaseAdapter {

    private ArrayList<Movie> mItems;
    //ArrayList<News> mIdMap = new ArrayList<News>();
    int layoutResId;
    Context mContext;
    private FragmentManager mFragmentManager;
    private DisplayImageOptions mDisplayImageOptions;

    public CalendarListAdapter(FragmentActivity fragmentActivity, ArrayList<Movie> items) {
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

            currentView = View.inflate(mContext, R.layout.calendar_item, null);

            itemViewHolder.myGridView = (MyGridView) currentView.findViewById(R.id.grid);
            itemViewHolder.root = currentView.findViewById(R.id.root);
            itemViewHolder.image = (ImageView) currentView.findViewById(R.id.image);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.title);
            itemViewHolder.loading =  currentView.findViewById(R.id.loading);



            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }

        final Movie movie = mItems.get(position);


        //set data - grid has EventBus for clicking seances
        itemViewHolder.myGridView.setMovie(movie);
        itemViewHolder.myGridView.setSeances(movie.getSeances(), false);

        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxScreen.getBus().post(new OpenMovieInfoEventBus(-1, movie));
            }
        });

        itemViewHolder.title.setText("" + movie.getTitle());

        setImage(movie,itemViewHolder);


        return currentView;
    }

    private void setImage(Movie movie, ItemViewHolder item) {
        if(movie!=null && movie.getImages()!=null) {
            String url = movie.getImages();
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
        public  MyGridView myGridView;
        public View root;
        public ImageView image;
        public TextView title;
        public View loading;
    }
}
