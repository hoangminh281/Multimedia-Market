package com.thm.hoangminh.multimediamarket.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.ProductAdapter;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.presenters.ProductPresenters.ProductPresenter;
import com.thm.hoangminh.multimediamarket.views.ProductDetailViews.ProductDetailActivity;
import com.thm.hoangminh.multimediamarket.views.ProductViews.ProductView;

import java.util.ArrayList;

public class ProductFragment extends Fragment implements ProductView {
    private ArrayList<Product> productsList;
    private GridView gridView;
    private ProductPresenter presenter;
    private ProductAdapter myAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_layout, container, false);

        setControls(view);
        initPresenter();
        initAdapter();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.getString("cate_id")!=null && bundle.getString("section_id") != null) {
                presenter.LoadProductBySectionPaging(bundle.getString("section_id"), bundle.getString("section_id"));
            } else if (bundle.getString("user_id") != null) {
                presenter.LoadProductByUserPaging(bundle.getString("user_id"));
            } else if (bundle.getString("bookmark_cate_id") !=null) {
                presenter.LoadProductByBookmarkCateIdPaging(bundle.getString("bookmark_cate_id"));
            }
        }

        setEvents();
    }

    private void setControls(View view) {
        productsList = new ArrayList<>();
        gridView = view.findViewById(R.id.gridView);
    }

    private void initPresenter() {
        presenter = new ProductPresenter(this);
    }

    private void initAdapter() {
        this.myAdapter = new ProductAdapter(getContext(), R.layout.cell_view_layout, productsList);
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
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("product_object", productsList.get(i));
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
