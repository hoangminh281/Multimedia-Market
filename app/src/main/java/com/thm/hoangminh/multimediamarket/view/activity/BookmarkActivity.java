package com.thm.hoangminh.multimediamarket.viewImpl.BookmarkViews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.BookmarkPresenters.BookmarkPresenter;
import com.thm.hoangminh.multimediamarket.viewImpl.MainViews.MainActivity;
import com.thm.hoangminh.multimediamarket.viewImpl.fragments.ProductFragment;

import java.util.ArrayList;
import java.util.List;

public class BookmarkActivity extends AppCompatActivity implements BookmarkView {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private Toolbar toolbar;
    private BookmarkPresenter presenter;

    public final static String bookmarkKey = "bookmark_cate_id";
    public final static String productAdminKey = "productAdmin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);
        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            initPresenter();
            showCategoriesTabLayout(Constants.Admin);
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_product_admin) + "");
            initPresenter();
            presenter.findCurrentUserRole();
        } else {
            showCategoriesTabLayout(bookmarkKey);
            getSupportActionBar().setTitle(getResources().getString(R.string.menu_bookmark) + "");
        }
        setEvents();
    }

    private void initPresenter() {
        presenter = new BookmarkPresenter(this);
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

    public void showCategoriesTabLayout(String key) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        for (Category category : MainActivity.categories) {
            Bundle bundle = new Bundle();
            bundle.putString(key, category.getCate_id());
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

    @Override
    public void bindingUserRole(Integer role_id) {
        if (role_id != User.ADMIN) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BookmarkActivity.this, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.RemoveListener();
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
        tabLayout = findViewById(R.id.tablayoutHome);
        viewPager = findViewById(R.id.viewPagerHome);
        toolbar = findViewById(R.id.toolbar);
    }
}
