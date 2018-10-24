package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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

import com.google.firebase.auth.FirebaseAuth;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.presenter.MainPresenter;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.view.callback.MainView;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.implement.MainPresenterImpl;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.fragment.SectionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {
    private Toolbar toolbar;
    private ImageView imgSex;
    private Button btnRecharge;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private DrawerLayout drawerLayout;
    private MaterialSearchView searchView;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private TextView txtUserName, txtBalance, txtRole;
    private SwipeRefreshLayout swipeRefreshLayout;
    private de.hdodenhof.circleimageview.CircleImageView imgUser;

    private MainPresenter presenter;
    private ViewPagerAdapter adapter;
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

        presenter.loadUserProfile();
        presenter.loadCategory();
        presenter.loadProductSuggestions();
    }


    public void initPresenter() {
        presenter = new MainPresenterImpl(this, this);
    }

    @Override
    public void updateUserUI(User user) {
        txtUserName.setText(user.getName());
        txtBalance.setText(Tools.FormatMoney(user.getBalance()));
    }

    @Override
    public void setVisibleItemMenu(int itemId, boolean b) {
        navigationView.getMenu().findItem(itemId).setVisible(b);
    }

    @Override
    public void loadUserAvatar(Uri uri) {
        ImageLoader.loadImageByUri(this, imgUser, uri);
    }

    @Override
    public void showUserRole(String roleName) {
        txtRole.setText(roleName);
    }

    @Override
    public void showUserGenderImage(int imageId) {
        imgSex.setImageResource(imageId);
    }

    @Override
    public void setNavigationItemSelectedListener() {
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void initViewPager() {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Category.getInstance().add(0, new Category(Constants.HomeKey, Constants.Home));
        for (Category category : Category.getInstance()) {
            Bundle bundle = new Bundle();
            bundle.putString(Constants.BundleKey, category.getCateId());
            SectionFragment fragment = new SectionFragment();
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, category.getName());
        }

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(Category.getInstance().size());
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void setProductSuggestions(Map<String, String> suggestions) {
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
                startActivity(ProfileActivity.class);
                break;
            case R.id.menu_bookmark:
                startActivity(BookmarkActivity.class);
                break;
            case R.id.menu_upload:
                startActivity(ModifyProductActivity.class);
                break;
            case R.id.menu_user_admin:
                startActivity(UserActivity.class);
                break;
            case R.id.menu_card_admin:
                startActivity(CardActivity.class);
                break;
            case R.id.menu_product_admin:
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleOptionKey, Constants.AdminOption);
                startActivity(BookmarkActivity.class, bundle);
                break;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(SigninActivity.class);
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

    @Override
    public void setupTabIcons() {
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

    @Override
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    @Override
    public void startActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtras(bundle);
        startActivity(intent);
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

    @Override
    public void setEvents() {
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
                    if (row.getValue().toLowerCase().startsWith(query.toLowerCase())) {
                        searchResults.add(row.getKey());
                    }
                }
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleOptionKey, Constants.SearchOption);
                bundle.putStringArrayList(Constants.SearchResults, searchResults);
                startActivity(ProductActivity.class, bundle);
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
