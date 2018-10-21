package com.thm.hoangminh.multimediamarket.adapter;

import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;

public class ViewPagerAdapter extends PagerAdapter {
    private ArrayList<Uri> imageUriList;
    private Context context;
    private LayoutInflater inflater;

    public ViewPagerAdapter(Context context, ArrayList<Uri> imageUriList) {
        this.imageUriList = imageUriList;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageUriList.size();
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
        ImageLoader.loadImageByUriToFit(context, img, imageUriList.get(position));
        container.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
