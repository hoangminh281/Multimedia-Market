package com.thm.hoangminh.multimediamarket.adapters;

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
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.presenters.SectionPresenters.SectionPresenter;
import com.thm.hoangminh.multimediamarket.views.ProductViews.ProductActivity;
import com.thm.hoangminh.multimediamarket.views.SectionViews.SectionView;

import java.util.ArrayList;

public class AllSectionsAdapter extends RecyclerView.Adapter<AllSectionsAdapter.ItemRowHolder> implements SectionView {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private SectionPresenter presenter;
    private SectionListDataAdapter itemListDataAdapter;

    public AllSectionsAdapter(Context context, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = context;
        this.presenter = new SectionPresenter(this);
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.section_recycler_layout, null);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, final int i) {
        final String cateId = dataList.get(i).getCate_id();

        final String sectionId = dataList.get(i).getSection_id();

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);

        itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);
        itemRowHolder.recycler_view_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                if (isBottomReached) {
                    presenter.LoadProductsBySectionPaging(dataList.get(i));
                }
            }
        });

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent moveToProductActivity = new Intent(mContext, ProductActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("section_id", sectionId);
                bundle.putString("cate_id", cateId);
                moveToProductActivity.putExtras(bundle);
                mContext.startActivity(moveToProductActivity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    @Override
    public void addSectionCardview(ArrayList<Section> sectionArr) {

    }

    @Override
    public void refreshAdapter() {
        itemListDataAdapter.notifyDataSetChanged();
    }

    @Override
    public void showBottomProgressbar() {

    }

    @Override
    public void hideBottomProgressbar() {

    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle;
        private RecyclerView recycler_view_list;
        private Button btnMore;

        private ItemRowHolder(View view) {
            super(view);
            this.itemTitle = view.findViewById(R.id.textViewTitleSection);
            this.recycler_view_list = view.findViewById(R.id.recyclerViewSection);
            this.btnMore = view.findViewById(R.id.buttonMoreSection);
        }

    }

}