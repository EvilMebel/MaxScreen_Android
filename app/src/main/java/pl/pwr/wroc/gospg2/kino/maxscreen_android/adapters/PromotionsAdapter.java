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
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.News;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.FlowTextHelper;

/**
 * Created by Evil on 2015-03-31.
 */
public class PromotionsAdapter extends BaseAdapter {

    private ArrayList<Coupon_DB> mItems;
    Context mContext;
    private DisplayImageOptions mDisplayImageOptions;

    public PromotionsAdapter(Context fragmentActivity, ArrayList<Coupon_DB> items) {
        mContext = fragmentActivity;
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
        ItemViewHolder itemViewHolder;

        if(currentView==null) {
            itemViewHolder = new ItemViewHolder();
            currentView = View.inflate(mContext, R.layout.promotion_item, null);

            itemViewHolder.image = (ImageView) currentView.findViewById(R.id.thumbnail_view);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.title);
            itemViewHolder.msg = (TextView) currentView.findViewById(R.id.message_view);

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }


        Coupon_DB coupon_db = mItems.get(position);

        String text = coupon_db.getDescription();//todo text from REST

        //todo string buffer?
        String titleText = coupon_db.getID_Coupon() + " \n" + coupon_db.getVersion() + "\nDo " + Converter.gregToString(coupon_db.getDate());

            /*Display display = ((MainActivity) mContext).getWindowManager().getDefaultDisplay();
            FlowTextHelper.tryFlowText(text, thumbnailView, messageView, display);*/
        itemViewHolder.msg.setText(text);
        itemViewHolder.title.setText(titleText);

        setImage(coupon_db,itemViewHolder);

        return currentView;
    }

    public void remove(Object item) {
        mItems.remove(item);
    }

    private void setImage(Coupon_DB coupon, ItemViewHolder item) {
        //todo image field for coupon
        /*
        if(coupon!=null && coupon.get!=null) {
            String url = coupon.getImage();
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
        }*/
    }

    private class ItemViewHolder {
        public ImageView image;
        public TextView title;
        public TextView msg;
    }

}
