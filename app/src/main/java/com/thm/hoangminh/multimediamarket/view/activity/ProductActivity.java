package com.thm.hoangminh.multimediamarket.view.ProductViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.ProductAdapter;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.presenters.ProductPresenters.ProductPresenter;
import com.thm.hoangminh.multimediamarket.view.ProductDetailViews.ProductDetailActivity;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity implements ProductView {
    private ArrayList<Product> productsList;
    private GridView gridView;
    private ProductPresenter presenter;
    private ProductAdapter myAdapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gridview_layout);
        setControls();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);
        initPresenter();
        initAdapter();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.getString("cate_id") != null && bundle.getString("section_id") != null && bundle.getString("sectionTitle") != null) {

                getSupportActionBar().setTitle(bundle.getString("sectionTitle"));
                presenter.LoadProductBySectionPaging(bundle.getString("cate_id"), bundle.getString("section_id"));

            } else if (bundle.getString("user_id") != null && bundle.getString("cateProduct") != null && bundle.getString("cateTitle") != null) {

                getSupportActionBar().setTitle(bundle.getString("cateTitle"));
                presenter.LoadProductByUserPaging(bundle.getString("user_id"), bundle.getString("cateProduct"));

            } else if (bundle.getStringArray("searchResults") != null) {

                getSupportActionBar().setTitle(getResources().getString(R.string.txt_search) + "");
                presenter.LoadProductByKeys(bundle.getStringArray("searchResults"));

            }
        }

        setEvents();
    }

    private void setControls() {
        productsList = new ArrayList<>();
        gridView = findViewById(R.id.gridView);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPresenter() {
        presenter = new ProductPresenter(this);
    }

    private void initAdapter() {
        this.myAdapter = new ProductAdapter(ProductActivity.this, R.layout.cell_view_layout, productsList);
        gridView.setAdapter(myAdapter);
    }

    private void setEvents() {
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                Boolean isBottomReached = !gridView.canScrollVertically(1);
                if (isBottomReached) {
                    presenter.LoadProductNexttoScroll();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ProductActivity.this, ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("cate_id", productsList.get(i).getCate_id());
                bundle.putString("product_id", productsList.get(i).getProduct_id());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void addProducttoAdapter(Product product) {
        productsList.add(product);
    }

    @Override
    public void refreshAdapter() {
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBottomProgressbar() {

    }

    @Override
    public void hideBottomProgressbar() {

    }
}
