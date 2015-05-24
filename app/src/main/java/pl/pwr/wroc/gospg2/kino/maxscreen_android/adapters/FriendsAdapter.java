package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Movie;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenMovieInfoEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.OpenProfileEventBus;

/**
 * Created by Evil on 2015-03-31.
 */
public class FriendsAdapter extends BaseAdapter {

    private List<JSONObject> mItems;
    Context mContext;

    OnClickMovie onClickMovie;


    private DisplayImageOptions mDisplayImageOptions;

    public FriendsAdapter(Context context, ArrayList<JSONObject> items) {
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

            currentView = View.inflate(mContext, R.layout.profile_friends_item, null);

            itemViewHolder.profilePicture = (ProfilePictureView) currentView.findViewById(R.id.profilePicture);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.title);
            itemViewHolder.root = currentView.findViewById(R.id.root);

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }

        final JSONObject n = mItems.get(position);

        try {
            itemViewHolder.title.setText(n.getString("name"));
            itemViewHolder.profilePicture.setProfileId(n.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //MSData.getInstance().set
                try {
                    long fbId = Long.parseLong(n.getString("id"));
                    Log.d("parseLong", "fbId = " + fbId);
                    MaxScreen.getBus().post(new OpenProfileEventBus(fbId));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(onClickMovie!=null)
                    onClickMovie.onClick();
            }
        });


        return currentView;
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
        public ProfilePictureView profilePicture;
        public TextView title;
        public View root;
    }

    public interface OnClickMovie {
        public void onClick();
    }
}
