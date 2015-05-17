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

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MSData;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MainActivity;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenMovieInfoEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

/**
 * Created by Evil on 2015-03-31.
 */
public class WantMoviesAdapter extends BaseAdapter {

    private ArrayList<Movie> mItems;
    ArrayList<News> mIdMap = new ArrayList<News>();
    int layoutResId;
    Context mContext;

    OnClickMovie onClickMovie;

    private DisplayImageOptions mDisplayImageOptions;

    public WantMoviesAdapter(Context context, ArrayList<Movie> items) {
        mContext = context;
        mItems = items;


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

            currentView = View.inflate(mContext, R.layout.profile_want_movie_item, null);

            itemViewHolder.image = (ImageView) currentView.findViewById(R.id.image);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.title);
            itemViewHolder.root = currentView.findViewById(R.id.root);

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }

        final Movie n = mItems.get(position);

        itemViewHolder.title.setText(n.getTitle());

        setImage(n, itemViewHolder);

        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickMovie!=null)
                    onClickMovie.onClick();
                //MSData.getInstance().set
                MaxScreen.getBus().post(new OpenMovieInfoEventBus(n.getIdMove(), n));
            }
        });


        return currentView;
    }


    private void setImage(Movie news, ItemViewHolder item) {
        if(news!=null && news.getImages()!=null) {
            String url = news.getImages();
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

    public OnClickMovie getOnClickMovie() {
        return onClickMovie;
    }

    public void setOnClickMovie(OnClickMovie onClickMovie) {
        this.onClickMovie = onClickMovie;
    }

    private class ItemViewHolder {
        public ImageView image;
        public TextView title;
        public View root;
    }

    public interface OnClickMovie {
        public void onClick();
    }
}
