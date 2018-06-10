package com.thm.hoangminh.multimediamarket.adapters;

import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;

public class ViewPagerAdapter extends PagerAdapter {

    private ArrayList<String> itemsList;
    private Context mContext;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context, ArrayList<String> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemsList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = inflater.inflate(R.layout.image_view_layout, container, false);

        assert view != null;
        final ImageView img = view.findViewById(R.id.imageView);

        Picasso.with(mContext)
                .load(itemsList.get(position))
                .fit()
                .placeholder(R.drawable.icon_app_2)
                .into(img);

        container.addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

}
