package com.thm.hoangminh.multimediamarket.views.CardViews;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapters.CardAdapter;
import com.thm.hoangminh.multimediamarket.adapters.UserAdapter;
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenters.CardPresenters.CardPresenter;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;
import com.thm.hoangminh.multimediamarket.views.ModifyCardViews.ModifyCardActivity;
import com.thm.hoangminh.multimediamarket.views.UserViews.UserActivity;

import java.util.ArrayList;

public class CardActivity extends AppCompatActivity implements CardView {
    private CardAdapter adapter;
    private RecyclerView myRecyclerView;
    private ArrayList<Card> cards;
    private CardPresenter presenter;
    private Toolbar toolbar;

    public final static String requestCode = "cardObj";
    public final static int cardRequestCode = 1111;

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
        presenter.findCurrentUserRole();
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
                Intent intent = new Intent(CardActivity.this, ModifyCardActivity.class);
                startActivityForResult(intent, cardRequestCode);
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.cardRequestCode && resultCode == RESULT_OK) {
            if (data.getExtras().getInt("result") == 1)
                presenter.loadCardList();
        }
    }

    private void initPresenter() {
        presenter = new CardPresenter(this);
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
                presenter.activeCard(card.getCategory(), card.getValue(), card.getId(), 1);
                break;
            case UserAdapter.INACTIVE_MENU_ID:
                presenter.activeCard(card.getCategory(), card.getValue(), card.getId(), 0);
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void showCardList(ArrayList<Card> cards) {
        this.cards = cards;
        initAdapter();
    }

    @Override
    public void bindingUserRole(Integer role_id) {
        if (role_id != User.ADMIN) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CardActivity.this, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.RemoveListener();
    }

    private void setControls() {
        myRecyclerView = findViewById(R.id.recycleView);
        toolbar = findViewById(R.id.toolbar);
    }
}
