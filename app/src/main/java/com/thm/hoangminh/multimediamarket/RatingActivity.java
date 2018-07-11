package com.thm.hoangminh.multimediamarket;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.views.fragments.RatingFragment;

public class RatingActivity extends AppCompatActivity {
    RatingFragment ratingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_layout);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ratingFragment = new RatingFragment();
            ratingFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameRatingComment, ratingFragment);
            fragmentTransaction.commit();
        }
    }
}
