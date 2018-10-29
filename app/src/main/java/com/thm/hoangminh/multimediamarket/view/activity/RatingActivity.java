package com.thm.hoangminh.multimediamarket.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.view.fragment.RatingFragment;

public class RatingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RatingFragment ratingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_layout);
        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

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

    private void setControls() {
        toolbar = findViewById(R.id.toolbar);
    }
}
