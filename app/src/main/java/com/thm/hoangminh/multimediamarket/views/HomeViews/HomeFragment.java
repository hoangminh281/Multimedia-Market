package com.thm.hoangminh.multimediamarket.views.HomeViews;

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
import com.thm.hoangminh.multimediamarket.presenters.HomePresenters.HomePresenter;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment implements HomeView {
    private ArrayList<SectionDataModel> allSampleData;
    private HomePresenter presenter;
    private AllSectionsAdapter adapter;
    private RecyclerView myRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_layout, null);

        //createDummyData();

        setControls(view);
        initPresenter();
        initAdapter();
        setEvents();

        presenter.LoadHomeSectionPaging();
        return view;
    }

    private void setControls(View view) {
        allSampleData = new ArrayList<>();
        myRecyclerView = view.findViewById(R.id.recyclerViewHome);
    }

    private void initPresenter() {
        presenter = new HomePresenter(this);
    }

    private void initAdapter() {
        myRecyclerView.setHasFixedSize(true);
        adapter = new AllSectionsAdapter(getContext(), allSampleData);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setAdapter(adapter);
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
                Boolean isBottomReached = !recyclerView.canScrollVertically(1);
                if (isBottomReached) {
                    presenter.LoadHomeSectionPaging();
                }
            }
        });
    }

    public void createDummyData() {
        /*mRef.child("sections").removeValue();
        for (Section section : Section.initializeData()) {
            DatabaseReference reference = mRef.child("sections").push();
            String id = reference.getKey();
            section.setSection_id(id);
            reference.setValue(section);
        }*/
        /*for (Game game : Game.initializeData()) {
            mRef.child("games/" + game.getGame_id()).setValue(game);
        }*/
        /*for (int i = 0; i < 30; i++)
            mRef.child("sections/-LDCT0g97ifg4RhZKzO1/game_id").push().setValue("-LDCT0gFJfJierreY8vh");*/
        /*for (GameDetail gameDetail : GameDetail.initializeData()) {
            mRef.child("game_detail/" +gameDetail.getId()).setValue(gameDetail);
        }*/
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> list = new HashMap<>();
        list.put("-LDlhVwesbhJsBsFVmEt", "20150902172241.png");
        list.put("-LDlhVwesbhJsBsFVmE2", "406x228bb.png");
        list.put("-LDlhVwesbhJsBsFVmE3", "images (1).png");
        list.put("-LDlhVwesbhJsBsFVmE4", "images.png");
        list.put("-LDlhVwesbhJsBsFVmE5", "maxresdefault.png");
        mRef.child("game_detail/-LDlhVwX9fzrFxtdewJo/imageList").setValue(list);
    }

    @Override
    public void addSectionCardview(ArrayList<Section> sectionArr) {
        for (Section section : sectionArr) {

            SectionDataModel dm = new SectionDataModel();

            dm.setSection_id(section.getSection_id());

            dm.setHeaderTitle(section.getTitle());

            if (section.getGame_id() != null) {

                dm.setGame_id_arr(Section.getGameId_ListString(section.getGame_id()));

                presenter.LoadGamesBySectionPaging(dm);

            }

            allSampleData.add(dm);
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
}