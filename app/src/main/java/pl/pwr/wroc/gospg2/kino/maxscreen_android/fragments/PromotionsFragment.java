package pl.pwr.wroc.gospg2.kino.maxscreen_android.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import pl.pwr.wroc.gospg2.kino.maxscreen_android.R;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.adapters.PromotionsAdapter;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupon_DB;
import pl.pwr.wroc.gospg2.kino.maxscreen_android.entities.Coupons;
import roboguice.inject.InjectView;

public class PromotionsFragment extends RoboEventFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    @InjectView (R.id.list)
    ListView mList;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PromotionsFragment.
     */
    public static PromotionsFragment newInstance(String param1, String param2) {
        PromotionsFragment fragment = new PromotionsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PromotionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotions, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    private void loadData() {
        //todo load seriously...
        ArrayList<Coupon_DB> couponsList = new ArrayList<Coupon_DB>();

        for(int i = 0; i<5; i++) {
            Coupon_DB c = new Coupon_DB();
            c.setDate(new GregorianCalendar(2015, 5, 15));
            c.setDescription("Ale promocja!!!");
            c.setID_Coupon("ABC4546FBF");
            c.setDiscount(20);
            c.setVersion("znizka procentowa");

            couponsList.add(c);
        }

        mList.setAdapter(new PromotionsAdapter(getActivity(),couponsList));

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}
