package com.thm.hoangminh.multimediamarket.view.SectionViews;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.AllSectionsAdapter;
import com.thm.hoangminh.multimediamarket.models.Section;
import com.thm.hoangminh.multimediamarket.models.SectionDataModel;
import com.thm.hoangminh.multimediamarket.presenters.SectionPresenters.SectionPresenter;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;

import java.util.ArrayList;

public class SectionFragment extends Fragment implements SectionView {
    private ArrayList<SectionDataModel> allSampleData;
    private SectionPresenter presenter;
    private AllSectionsAdapter adapter;
    private RecyclerView myRecyclerView;
    private String keyMode;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, null);
        createDummyData();
        setControls(view);
        initPresenter();
        initAdapter();
        if (getArguments() != null) {
            keyMode = getArguments().getString(MainActivity.BUNDLE_KEY);
            presenter.LoadSectionPaging(keyMode);
        }
        setEvents();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initPresenter() {
        presenter = new SectionPresenter(this);
    }

    private void initAdapter() {
        myRecyclerView.setHasFixedSize(true);
        adapter = new AllSectionsAdapter(getContext(), allSampleData);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setAdapter(adapter);
    }

    public void createDummyData() {
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        /*mRef.child("sections/home").removeValue();
        for (Section section : Section.initializeData()) {
            DatabaseReference reference = mRef.child("sections/home").push();
            String id = reference.getKey();
            section.setSection_id(id);
            reference.setValue(section);
        }*/
        /*mRef.child("sections/home").removeValue();
        for (Section section : Section.initializeHomeSection()) {
            DatabaseReference reference = mRef.child("sections/home").push();
            String id = reference.getKey();
            section.setSection_id(id);
            reference.setValue(section);
        }
        mRef.child("sections/-LGAQqsSdhMt2bTudm1L").removeValue();
        for (Section section : Section.initializeGameSection()) {
            DatabaseReference reference = mRef.child("sections/-LGAQqsSdhMt2bTudm1L").push();
            String id = reference.getKey();
            section.setSection_id(id);
            reference.setValue(section);
        }
        mRef.child("sections/-LGAQqsSdhMt2bTudm1M").removeValue();
        for (Section section : Section.initializeImageSection()) {
            DatabaseReference reference = mRef.child("sections/-LGAQqsSdhMt2bTudm1M").push();
            String id = reference.getKey();
            section.setSection_id(id);
            reference.setValue(section);
        }*/
        /*for (Product product : Product.initializeGame()) {
            mRef.child("products/" + product.getProduct_id()).setValue(product);
        }*/
        /*mRef.child("categories").removeValue();
        for (Category category : Category.initializeData()) {
            DatabaseReference reference = mRef.child("categories").push();
            String id = reference.getKey();
            category.setCate_id(id);
            reference.setValue(category);
        }*/
        /*for (Product product : Product.initializeGame()) {
            mRef.child("products/" + product.getProduct_id()).setValue(product);
        }*/
        /*for (ProductDetail productDetail : ProductDetail.initializeGame()) {
            mRef.child("product_detail/" + productDetail.getId()).setValue(productDetail);
        }

        HashMap<String, String> list = new HashMap<>();
        list.put("-LDlhVwesbhJsBsFVmEt", "20150902172241.png");
        list.put("-LDlhVwesbhJsBsFVmE2", "406x228bb.png");
        list.put("-LDlhVwesbhJsBsFVmE3", "images (1).png");
        list.put("-LDlhVwesbhJsBsFVmE4", "images.png");
        list.put("-LDlhVwesbhJsBsFVmE5", "maxresdefault.png");
        mRef.child("product_detail/-LDlhVwX9fzrFxtdewJo/imageList").setValue(list);*/
        //mRef.child("cards/0/0").push().setValue(new Card(Tools.md5("123456"),"seri123",0));
    }

    @Override
    public void addSectionCardview(ArrayList<Section> sectionArr) {
        for (Section section : sectionArr) {

            SectionDataModel dm = new SectionDataModel();

            dm.setCate_id(keyMode);

            dm.setSection_id(section.getSection_id());

            dm.setHeaderTitle(section.getTitle());

            if (section.getProduct_id() != null) {

                dm.setProduct_id_arr(Section.getProductId_ListString(section.getProduct_id()));

                presenter.LoadProductsBySectionPaging(dm);

            }

            allSampleData.add(dm);
            refreshAdapter();
        }
    }

    @Override
    public void refreshAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showBottomProgressbar() {
        /* pgbBottom.setVisibility(View.VISIBLE);*/
    }

    @Override
    public void hideBottomProgressbar() {
        /*pgbBottom.setVisibility(View.GONE);*/
    }

    public void refresh() {
        if (keyMode != null) {
            allSampleData.clear();
            initPresenter();
            presenter.LoadSectionPaging(keyMode);
        }
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
                    presenter.LoadSectionPaging(keyMode);
                }
            }
        });
    }

    private void setControls(View view) {
        allSampleData = new ArrayList<>();
        myRecyclerView = view.findViewById(R.id.recyclerViewHome);
    }
}