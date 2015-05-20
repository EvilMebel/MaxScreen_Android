package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Relief;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

/**
 * Created by Evil on 2015-03-31.
 */
public class ReliefsAdapter extends BaseAdapter {

    private ArrayList<Relief> mItems;
    Context mContext;
    OnClickRelief onClickRelief;

    public ReliefsAdapter(Context fragmentActivity, ArrayList<Relief> items) {
        mContext = fragmentActivity;
        mItems = items;
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
    public View getView(final int position, View currentView, ViewGroup viewGroup) {
        ItemViewHolder itemViewHolder;

        if(currentView==null) {
            itemViewHolder = new ItemViewHolder();
            currentView = View.inflate(mContext, R.layout.relief_item, null);

            itemViewHolder.info = (TextView) currentView.findViewById(R.id.info);
            itemViewHolder.title = (TextView) currentView.findViewById(R.id.title);
            itemViewHolder.root =  currentView.findViewById(R.id.root);

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }

        Relief relief = mItems.get(position);


        itemViewHolder.title.setText(relief.getName());
        itemViewHolder.info.setText("Znizka " + relief.getPercentDiscount() + " %");
        itemViewHolder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClickRelief!=null) {
                    onClickRelief.onClick(position);
                }
            }
        });


        return currentView;
    }

    public void remove(Object item) {
        mItems.remove(item);
    }

    public OnClickRelief getOnClickRelief() {
        return onClickRelief;
    }

    public void setOnClickRelief(OnClickRelief onClickRelief) {
        this.onClickRelief = onClickRelief;
    }

    private class ItemViewHolder {
        public TextView title;
        public TextView info;
        public View root;
    }

    public interface OnClickRelief {
        public void onClick(int position);
    }

}
