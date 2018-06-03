package com.thm.hoangminh.multimediamarket.views.GameViews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.GridViewAdapter;
import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.presenters.GamePresenters.GamePresenter;
import com.thm.hoangminh.multimediamarket.views.GameDetailViews.GameDetailActivity;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity implements GameView {
    private ArrayList<Game> gamesList;
    private GridView gridView;
    private GamePresenter presenter;
    private GridViewAdapter myAdapter;
    private String section_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gridview_layout);
        setControls();
        initPresenter();
        initAdapter();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            section_id = bundle.getString("section_id");

            presenter.LoadGameBySectionPaging(section_id);
        }

        setEvents();
    }

    private void setControls() {
        gamesList = new ArrayList<>();
        gridView = findViewById(R.id.gridView);
    }

    private void initPresenter() {
        presenter = new GamePresenter(this);
    }

    private void initAdapter() {
        this.myAdapter = new GridViewAdapter(GameActivity.this, R.layout.cell_view_layout, gamesList);
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
                    presenter.LoadGameNexttoScroll();
                }
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GameActivity.this, GameDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("game_object", gamesList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public void addGametoAdapter(Game game) {
        gamesList.add(game);
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
