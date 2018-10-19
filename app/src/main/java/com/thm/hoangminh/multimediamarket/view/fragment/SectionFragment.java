package com.thm.hoangminh.multimediamarket.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.AllSectionsAdapter;
import com.thm.hoangminh.multimediamarket.model.SectionDataModel;
import com.thm.hoangminh.multimediamarket.presenter.SectionPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.SectionPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.callback.SectionView;

import java.util.ArrayList;

public class SectionFragment extends Fragment implements SectionView {
    private RecyclerView myRecyclerView;

    private SectionPresenter presenter;
    private AllSectionsAdapter adapter;
    private ArrayList<SectionDataModel> allSampleData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, null);
        setControls(view);
        initPresenter();
        initAdapter();
        if (getArguments() != null) {
            presenter.extractBundle(getArguments());
            setEvents();
        }
        return view;
    }

    private void initPresenter() {
        presenter = new SectionPresenterImpl(this);
    }

    private void initAdapter() {
        myRecyclerView.setHasFixedSize(true);
        adapter = new AllSectionsAdapter(getContext(), allSampleData);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public void addSectionDataModelToCardview(SectionDataModel sectionDataModel) {
        if (sectionDataModel.getProductIdArr() != null) {
            presenter.loadProductBySectionWithPaging(sectionDataModel);
        }
        allSampleData.add(sectionDataModel);
    }

    @Override
    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void refresh() {
        allSampleData.clear();
        presenter.reset();
        presenter.loadSectionWithPaging();
    }

    private void setEvents() {
        myRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            //Listener handle dragging bottom of list's event
            //1: for down
            //-1: for up
            //0: always return false
            //SCROLL_STATE_IDLE: No scrolling is done.
            //SCROLL_STATE_DRAGGING: The user is dragging his finger on the screen (or it is being done programatically.
            //SCROLL_STATE_SETTLING: User has lifted his finger, and the animation is now slowing down.
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Boolean isBottomReached = recyclerView.canScrollVertically(1);
                if (!isBottomReached) {
                    presenter.loadSectionWithPaging();
                }
            }
        });
    }

    private void setControls(View view) {
        allSampleData = new ArrayList<>();
        myRecyclerView = view.findViewById(R.id.recyclerViewHome);
    }
}