package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.CardAdapter;
import com.thm.hoangminh.multimediamarket.adapter.UserAdapter;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.CardPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.CardPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.callback.CardView;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity implements CardView {
    private Toolbar toolbar;
    private RecyclerView myRecyclerView;

    private CardAdapter adapter;
    private ArrayList<Card> cards;
    private CardPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_layout);

        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_card_admin) + "");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        initPresenter();
        presenter.loadCardList();
        presenter.bidingCurrentUser(this);
    }

    private void initPresenter() {
        presenter = new CardPresenterImpl(this);
    }

    @Override
    public void showCardList(ArrayList<Card> cards) {
        if (this.cards != null) {
            this.cards.clear();
        }
        this.cards = cards;
        initAdapter();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.card_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_addcard:
                Intent intent = new Intent(CardActivity.this, AddUpdateCardActivity.class);
                startActivityForResult(intent, Constants.CardRequestCode);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.CardRequestCode && resultCode == RESULT_OK) {
            if (data.getExtras().getInt("result") == 1)
                presenter.loadCardList();
        }
    }

    private void initAdapter() {
        myRecyclerView.setHasFixedSize(true);
        adapter = new CardAdapter(this, cards);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getOrder();
        Card card = cards.get(position);
        switch (item.getItemId()) {
            case UserAdapter.ACTIVE_MENU_ID:
                presenter.activeOrDeactiveCard(card, Constants.CardActive);
                break;
            case UserAdapter.INACTIVE_MENU_ID:
                presenter.activeOrDeactiveCard(card, Constants.CardDeactive);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeListener();
    }

    private void setControls() {
        myRecyclerView = findViewById(R.id.recycleView);
        toolbar = findViewById(R.id.toolbar);
    }
}
