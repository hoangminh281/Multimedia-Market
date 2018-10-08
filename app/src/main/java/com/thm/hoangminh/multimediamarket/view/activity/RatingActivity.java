package com.thm.hoangminh.multimediamarket;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thm.hoangminh.multimediamarket.view.fragments.RatingFragment;

public class RatingActivity extends AppCompatActivity {
    private RatingFragment ratingFragment;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_layout);
        toolbar = findViewById(R.id.toolbar);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ratingFragment = new RatingFragment();
            ratingFragment.setArguments(bundle);
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frameRatingComment, ratingFragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
