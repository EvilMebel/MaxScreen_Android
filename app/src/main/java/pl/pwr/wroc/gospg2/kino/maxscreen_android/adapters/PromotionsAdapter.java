package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    public PromotionsAdapter(Context fragmentActivity, ArrayList<Coupon_DB> items) {
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
    public View getView(int position, View currentView, ViewGroup viewGroup) {
        if(currentView==null) {
            currentView = View.inflate(mContext, R.layout.promotion_item, null);
            ImageView thumbnailView = (ImageView) currentView.findViewById(R.id.thumbnail_view);
            TextView messageView = (TextView) currentView.findViewById(R.id.message_view);
            TextView titleView = (TextView) currentView.findViewById(R.id.title);

            Coupon_DB coupon_db = mItems.get(position);

            String text = coupon_db.getDescription();//todo text from REST
            String titleText = coupon_db.getID_Coupon() + " \n" + coupon_db.getVersion() + "\nDo " + Converter.gregToString(coupon_db.getDate());

            /*Display display = ((MainActivity) mContext).getWindowManager().getDefaultDisplay();
            FlowTextHelper.tryFlowText(text, thumbnailView, messageView, display);*/
            messageView.setText(text);
            titleView.setText(titleText);
        }

        return currentView;
    }

    public void remove(Object item) {
        mItems.remove(item);
    }
}
