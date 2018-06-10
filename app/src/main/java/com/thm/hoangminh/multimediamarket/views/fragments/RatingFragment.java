package com.thm.hoangminh.multimediamarket.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.RatingAdapter;
import com.thm.hoangminh.multimediamarket.models.RatingContent;

import java.util.ArrayList;

public class RatingFragment extends Fragment {
    private RatingAdapter myAdapter;
    private ArrayList<RatingContent> data;
    private ListView listView;
    private TextView txtRatingOverview, txtRatingSumOverview;
    private RatingBar rtbOverview;
    private ProgressBar[] pgbRatingList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
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

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            setRatingOverview(getArguments().getDouble("rating_point"));
            setRatingOverviewDetail(getArguments().<RatingContent>getParcelableArrayList("ratingList"));
        }
    }

    private void setRatingOverview(double rating_point) {
        txtRatingOverview.setText(rating_point + "");
        rtbOverview.setRating((float) rating_point);
    }

    private void setRatingOverviewDetail(ArrayList<RatingContent> ratingList) {
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
        setRatingContentListview(ratingList);
    }

    private void setRatingContentListview(ArrayList<RatingContent> data) {
        this.data = data;
        listView.setAdapter(new RatingAdapter(getContext(), R.layout.rating_comment_row_layout, data));
    }
}
