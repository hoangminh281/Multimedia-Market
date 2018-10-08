package com.thm.hoangminh.multimediamarket.view.MainViews;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thm.hoangminh.multimediamarket.view.activity.BookmarkActivity;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.view.SigninActivity;
import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.MainPresenters.MainPresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.activity.CardActivity;
import com.thm.hoangminh.multimediamarket.view.ModifyProductViews.ModifyProductActivity;
import com.thm.hoangminh.multimediamarket.view.ProductViews.ProductActivity;
import com.thm.hoangminh.multimediamarket.view.ProfileViews.ProfileActivity;
import com.thm.hoangminh.multimediamarket.view.RechargeViews.RechargeActivity;
import com.thm.hoangminh.multimediamarket.view.SectionViews.SectionFragment;
import com.thm.hoangminh.multimediamarket.view.UserViews.UserActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private Button btnRecharge;
    private NavigationView navigationView;
    private de.hdodenhof.circleimageview.CircleImageView imgUser;
    private TextView txtUserName, txtBalance, txtRole;
    private ImageView imgSex;
    private MainPresenter presenter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MaterialSearchView searchView;

    public final static String HOME_KEY = "home";
    public static ArrayList<Category> categories;

    public final static String BUNDLE_KEY = "keyMode";
    private Map<String, String> suggestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        setControls();
        initPresenter();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setupNavigationView();

        presenter.LoadUserProfile();
        presenter.LoadCategory();
        presenter.LoadProductSuggestions();
    }


    public void initPresenter() {
        presenter = new MainPresenter(this);
    }

    @Override
    public void updateUI(User user) {
        if (user.getStatus() == 0) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(MainActivity.this, R.string.info_logout, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            Menu menu = navigationView.getMenu();
            switch (user.getRole()) {
                case User.ADMIN:
                    menu.findItem(R.id.menu_user_admin).setVisible(true);
                    menu.findItem(R.id.menu_card_admin).setVisible(true);
                    menu.findItem(R.id.menu_product_admin).setVisible(true);
                    menu.findItem(R.id.menu_upload).setVisible(true);
                    break;
                case User.MOD:
                    menu.findItem(R.id.menu_user_admin).setVisible(false);
                    menu.findItem(R.id.menu_card_admin).setVisible(false);
                    menu.findItem(R.id.menu_product_admin).setVisible(false);
                    menu.findItem(R.id.menu_upload).setVisible(true);
                    break;
                case User.USER:
                    menu.findItem(R.id.menu_user_admin).setVisible(false);
                    menu.findItem(R.id.menu_card_admin).setVisible(false);
                    menu.findItem(R.id.menu_product_admin).setVisible(false);
                    menu.findItem(R.id.menu_upload).setVisible(false);
                    break;
            }

            user.LoadUserImageView(imgUser, this);
            user.LoadUserRole(txtRole);
            user.LoadUserImageGender(imgSex);
            txtUserName.setText(user.getName());
            txtBalance.setText(Tools.FormatMoney(user.getBalance()));
        }
    }

    @Override
    public void showCategory(ArrayList<Category> categories) {
        MainActivity.categories = categories;
        // this must be below to avoid user click toggle when categories null
        navigationView.setNavigationItemSelectedListener(this);
        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        setEvents();
    }

    @Override
    public void showSuggestions(Map<String, String> suggestions) {
        this.suggestions = suggestions;
        searchView.setSuggestions(suggestions.values().toArray(new String[suggestions.size()]));
        searchView.showSuggestions();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.search_view:
                searchView.showSuggestions();
                searchView.showSearch();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_profile:
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_bookmark:
                intent = new Intent(MainActivity.this, BookmarkActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_upload:
                intent = new Intent(MainActivity.this, ModifyProductActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_user_admin:
                intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_card_admin:
                intent = new Intent(MainActivity.this, CardActivity.class);
                startActivity(intent);
                return true;
            case R.id.menu_product_admin:
                intent = new Intent(MainActivity.this, BookmarkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_KEY, "admin");
                intent.putExtras(bundle);
                startActivity(intent);
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                intent = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    private void setupNavigationView() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setItemIconTintList(null);
        View headerView = navigationView.inflateHeaderView(R.layout.header_drawer_layout);


        btnRecharge = headerView.findViewById(R.id.buttonRecharge);
        imgUser = headerView.findViewById(R.id.imageViewPhoto);
        txtUserName = headerView.findViewById(R.id.textviewName);
        imgSex = headerView.findViewById(R.id.imageViewSex);
        txtBalance = headerView.findViewById(R.id.textviewBalance);
        txtRole = headerView.findViewById(R.id.textViewRole);
    }

    void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY, HOME_KEY);
        SectionFragment homeFragment = new SectionFragment();
        homeFragment.setArguments(bundle);
        adapter.addFragment(homeFragment, "Home");

        for (Category category : categories) {
            bundle = new Bundle();
            bundle.putString(BUNDLE_KEY, category.getCate_id());
            SectionFragment fragment = new SectionFragment();
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, category.getName());
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(1 + categories.size());
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            if (i == tabLayout.getSelectedTabPosition()) continue;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                tabLayout.getTabAt(i).getIcon().setTint(getResources().getColor(R.color.separate_line));
            }
        }
    }

    private void setupTabIcons() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.getTabAt(0).setIcon(R.mipmap.ic_home).getIcon().setTint(getResources().getColor(R.color.theme_app));
            tabLayout.getTabAt(1).setIcon(R.mipmap.ic_game).getIcon().setTint(getResources().getColor(R.color.separate_line));
            tabLayout.getTabAt(2).setIcon(R.mipmap.ic_image).getIcon().setTint(getResources().getColor(R.color.separate_line));
            tabLayout.getTabAt(3).setIcon(R.mipmap.ic_video).getIcon().setTint(getResources().getColor(R.color.separate_line));
            tabLayout.getTabAt(4).setIcon(R.mipmap.ic_music).getIcon().setTint(getResources().getColor(R.color.separate_line));
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
        btnRecharge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RechargeActivity.class);
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                for (Fragment fragment : adapter.mFragmentList) {
                    ((SectionFragment) fragment).refresh();
                }
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                ArrayList<String> searchResults = new ArrayList<>();
                for (Map.Entry<String, String> row : suggestions.entrySet()) {
                    if (row.getValue().toLowerCase().startsWith(query.toString().toLowerCase())) {
                        searchResults.add(row.getKey());
                    }
                }
                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                intent.putExtra("searchResults", searchResults.toArray(new String[searchResults.size()]));
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    void setControls() {
        toolbar = findViewById(R.id.toolbarHome);
        tabLayout = findViewById(R.id.tablayoutHome);
        viewPager = findViewById(R.id.viewPagerHome);
        drawerLayout = findViewById(R.id.activity_main_drawer);
        navigationView = findViewById(R.id.navView);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        searchView = findViewById(R.id.search_view);
    }

}
