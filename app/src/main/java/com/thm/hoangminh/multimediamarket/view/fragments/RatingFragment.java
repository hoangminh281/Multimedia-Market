package com.thm.hoangminh.multimediamarket.viewImpl.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.RatingActivity;
import com.thm.hoangminh.multimediamarket.adapters.RatingAdapter;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.references.Tools;

import java.util.ArrayList;

public class RatingFragment extends Fragment {
    private ArrayList<RatingContent> ratingList;
    private ListView listView;
    private TextView txtRatingOverview, txtRatingSumOverview;
    private RatingBar rtbOverview;
    private ProgressBar[] pgbRatingList;
    private double rating_point;
    private int limit;
    private LinearLayout layoutRatingOverview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rating_fragment_layout, container, false);

        listView = view.findViewById(R.id.listView);
        txtRatingOverview = view.findViewById(R.id.textViewRatingOverview);
        rtbOverview = view.findViewById(R.id.ratingBarOverview);
        txtRatingSumOverview = view.findViewById(R.id.textViewRatingSumOverview);
        pgbRatingList = new ProgressBar[5];
        pgbRatingList[0] = view.findViewById(R.id.progressBarRating1);
        pgbRatingList[1] = view.findViewById(R.id.progressBarRating2);
        pgbRatingList[2] = view.findViewById(R.id.progressBarRating3);
        pgbRatingList[3] = view.findViewById(R.id.progressBarRating4);
        pgbRatingList[4] = view.findViewById(R.id.progressBarRating5);
        layoutRatingOverview = view.findViewById(R.id.layoutRatingOverView);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            rating_point = getArguments().getDouble("rating_point");
            ratingList = getArguments().getParcelableArrayList("ratingList");
            limit = getArguments().getInt("limit");
            setRatingOverview();
            setRatingOverviewDetail();
            setRatingContentListview();
        }
    }

    private void setRatingOverview() {
        txtRatingOverview.setText(rating_point + "");
        rtbOverview.setRating((float) rating_point);
    }

    private void setRatingOverviewDetail() {
        int[] ratingArr = {0, 0, 0, 0, 0};
        for (RatingContent item : ratingList) {
            ratingArr[item.getPoint() - 1]++;
        }
        txtRatingSumOverview.setText(ratingList.size() + "");
        int max = -1;
        for (int i : ratingArr) {
            max = max < i ? i : max;
        }
        int count = 4;
        for (ProgressBar pgb : pgbRatingList) {
            pgb.setMax(max);
            pgb.setProgress(ratingArr[count--]);
        }
    }

    private void setRatingContentListview() {
        if (limit > 0) {
            listView.setAdapter(new RatingAdapter(getContext(), R.layout.rating_comment_row_layout
                    , new ArrayList<>(ratingList.subList(0, limit < ratingList.size() ? limit : ratingList.size())), limit));
            Tools.setListViewHeightBasedOnChildren(listView);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getActivity(), RatingActivity.class);
                    intent.putExtras(getBundleRating());
                    startActivity(intent);
                }
            });
            layoutRatingOverview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), RatingActivity.class);
                    intent.putExtras(getBundleRating());
                    startActivity(intent);
                }
            });
        } else if (limit == -1) {
            listView.setAdapter(new RatingAdapter(getContext(), R.layout.rating_comment_row_layout, ratingList, limit));
        }
    }

    private Bundle getBundleRating() {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("ratingList", ratingList);
        bundle.putDouble("rating_point", rating_point);
        bundle.putInt("limit", -1);
        return bundle;
    }

}
