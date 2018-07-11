package com.thm.hoangminh.multimediamarket;

import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;
import com.thm.hoangminh.multimediamarket.views.fragments.ProductFragment;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        setControls();

        showCategoriesTabLayout();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);


        setEvents();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showCategoriesTabLayout() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        for (Category category : MainActivity.categories) {
            Bundle bundle = new Bundle();
            bundle.putString("bookmark_cate_id", category.getCate_id());
            ProductFragment productFragment = new ProductFragment();
            productFragment.setArguments(bundle);
            adapter.addFragment(productFragment, category.getName());
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(MainActivity.categories.size());

        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.getTabAt(0).setIcon(R.mipmap.ic_game).getIcon().setTint(getResources().getColor(R.color.theme_app));
            tabLayout.getTabAt(1).setIcon(R.mipmap.ic_image).getIcon().setTint(getResources().getColor(R.color.separate_line));
            tabLayout.getTabAt(2).setIcon(R.mipmap.ic_video).getIcon().setTint(getResources().getColor(R.color.separate_line));
            tabLayout.getTabAt(3).setIcon(R.mipmap.ic_music).getIcon().setTint(getResources().getColor(R.color.separate_line));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tab.getIcon().setTint(getResources().getColor(R.color.theme_app));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tab.getIcon().setTint(getResources().getColor(R.color.separate_line));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

    }

    private void setEvents() {
    }

    private void setControls() {
        toolbar = findViewById(R.id.toolbarHome);
        tabLayout = findViewById(R.id.tablayoutHome);
        viewPager = findViewById(R.id.viewPagerHome);
    }
}
