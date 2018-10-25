package com.thm.hoangminh.multimediamarket.view.fragment;

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

import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.ProductAdapter;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.presenter.ProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProductPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.activity.ProductDetailActivity;
import com.thm.hoangminh.multimediamarket.view.callback.ProductView;

import java.util.ArrayList;

public class ProductFragment extends Fragment implements ProductView {
    private GridView gridView;
    private ProductAdapter myAdapter;
    private ProductPresenter presenter;
    private ArrayList<Product> productsList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gridview_fragment_layout, container, false);
        setControls(view);
        initPresenter();
        initAdapter();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        presenter.extractBundle(bundle);
        setEvents();
    }

    @Override
    public void setTitle(String title) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(title);
    }

    @Override
    public void setTitle(int titleId) {
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titleId);
    }

    private void initPresenter() {
        presenter = new ProductPresenterImpl(this);
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
                    presenter.loadProductPaging();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.CateIdKey, productsList.get(i).getCateId());
                bundle.putString(Constants.ProductIdKey, productsList.get(i).getProductId());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void addProductIntoAdapter(Product product) {
        productsList.add(product);
    }

    @Override
    public void refreshAdapter() {
        myAdapter.notifyDataSetChanged();
    }

    private void setControls(View view) {
        productsList = new ArrayList<>();
        gridView = view.findViewById(R.id.gridView);
    }
}
