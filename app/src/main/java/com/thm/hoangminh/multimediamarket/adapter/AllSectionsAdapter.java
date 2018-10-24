package com.thm.hoangminh.multimediamarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.SectionDataModel;
import com.thm.hoangminh.multimediamarket.presenter.SectionPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.SectionPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.activity.ProductActivity;
import com.thm.hoangminh.multimediamarket.view.callback.SectionView;

import java.util.ArrayList;

public class AllSectionsAdapter extends RecyclerView.Adapter<AllSectionsAdapter.ItemRowHolder>{
    private Context mContext;
    private SectionPresenter presenter;
    private ArrayList<SectionDataModel> dataList;
    private SectionListDataAdapter itemListDataAdapter;

    public AllSectionsAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.presenter = new SectionPresenterImpl(new SectionView() {
            @Override
            public void refreshAdapter() {
                itemListDataAdapter.notifyDataSetChanged();
            }

            @Override
            public void addSectionDataModelToCardview(SectionDataModel sectionDataModel) {

            }
        });
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_recycler_layout, null);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        final String cateId = dataList.get(i).getCateId();
        final String sectionId = dataList.get(i).getSectionId();
        final String sectionName = dataList.get(i).getHeaderTitle();
        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();
        itemRowHolder.itemTitle.setText(sectionName);
        itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);
        itemRowHolder.recyclerView.setHasFixedSize(true);
        itemRowHolder.recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recyclerView.setAdapter(itemListDataAdapter);
        itemRowHolder.recyclerView.setNestedScrollingEnabled(false);
        itemRowHolder.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                Boolean isBottomReached = !recyclerView.canScrollHorizontally(1);
                if (isBottomReached && i < dataList.size()) {
                    presenter.loadProductBySectionWithPaging(dataList.get(i));
                }
            }
        });

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constants.BundleOptionKey, Constants.SectionOption);
                bundle.putString(Constants.SectionIdKey, sectionId);
                bundle.putString(Constants.SectionTitleKey, sectionName);
                bundle.putString(Constants.CateIdKey, cateId);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {
        private TextView itemTitle;
        private RecyclerView recyclerView;
        private Button btnMore;

        private ItemRowHolder(View view) {
            super(view);
            this.itemTitle = view.findViewById(R.id.textViewTitleSection);
            this.recyclerView = view.findViewById(R.id.recyclerViewSection);
            this.btnMore = view.findViewById(R.id.buttonMoreSection);
        }

    }

}