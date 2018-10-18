package com.thm.hoangminh.multimediamarket.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.thm.hoangminh.multimediamarket.view.activity.BookmarkActivity;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.ProductAdapter;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.presenter.implement.ProductPresenter;
import com.thm.hoangminh.multimediamarket.view.activity.ProductDetailActivity;
import com.thm.hoangminh.multimediamarket.view.callback.ProductView;

import java.util.ArrayList;

public class ProductFragment extends Fragment implements ProductView {
    private ArrayList<Product> productsList;
    private GridView gridView;
    private ProductPresenter presenter;
    private ProductAdapter myAdapter;

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
        if (bundle != null) {
            if (bundle.getString(BookmarkActivity.bookmarkKey) != null) {
                presenter.LoadProductByBookmarkCateIdPaging(bundle.getString(BookmarkActivity.bookmarkKey));
            }
            else if (bundle.getString(BookmarkActivity.productAdminKey) != null) {
                presenter.LoadProductByAdmin(bundle.getString(BookmarkActivity.productAdminKey));
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
