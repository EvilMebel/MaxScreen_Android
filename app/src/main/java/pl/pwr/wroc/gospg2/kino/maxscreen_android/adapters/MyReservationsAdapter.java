package pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters;


import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.MaxScreen;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Reservation;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Seance;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.events.FinishReservationEventBus;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.utils.Converter;

/**
 * Created by Evil on 2015-03-31.
 */
public class MyReservationsAdapter extends BaseAdapter {

    private ArrayList<Reservation> mItems;
    Context mContext;
    private DisplayImageOptions mDisplayImageOptions;

    public MyReservationsAdapter(Context fragmentActivity, ArrayList<Reservation> items) {
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
            currentView = View.inflate(mContext, R.layout.my_reservations_item, null);

            itemViewHolder.nr = (TextView) currentView.findViewById(R.id.nr);
            itemViewHolder.root =  currentView.findViewById(R.id.root);
            itemViewHolder.movie = (TextView) currentView.findViewById(R.id.movie);
            itemViewHolder.date_info = (TextView) currentView.findViewById(R.id.date_info);
            itemViewHolder.more = (TextView) currentView.findViewById(R.id.more);
            //itemViewHolder.coupon_date = (TextView) currentView.findViewById(R.id.coupon_date);*/

            currentView.setTag(itemViewHolder);
        } else {
            itemViewHolder = (ItemViewHolder) currentView.getTag();
        }


        final Reservation r = mItems.get(position);


        itemViewHolder.nr
                .setText("Nr rezerwacji: " +
                        r.
                                getIdReservation());

        itemViewHolder.movie.setText(r.getSeanceEntity().getMovieEntity().getTitle());
        Seance s = r.getSeanceEntity();
        itemViewHolder.date_info.setText(Converter.gregToStringWithDay(s.getDate()) + " " + Converter.getHourFromGreCale(s.getDate()) + "\nSala " + s.getHallsEntity().getName_Hall());
        itemViewHolder.more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MaxScreen.getBus().post(new FinishReservationEventBus(r.getIdReservation(),null));
            }
        });

        //no image!
        //setImage(coupon_db,itemViewHolder);

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
        public TextView nr;
        public TextView movie;
        public TextView date_info;
        public View more;

        public View root;
        /*public TextView coupon_disc;
        public TextView coupon_nr;*/
    }

}
