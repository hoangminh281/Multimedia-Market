package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.UserAdapter;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.UserPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.UserPresenterImpl;
import com.thm.hoangminh.multimediamarket.view.callback.UserView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements UserView {
    private Toolbar toolbar;
    private AlertDialog dialog;
    private RecyclerView myRecyclerView;

    private UserAdapter adapter;
    private ArrayList<User> users;
    private UserPresenter presenter;
    private ArrayList<String> roles;

    public final static int requestCode = 2222;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycleview_layout);
        setControls();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.menu_user_admin) + "");
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);

        initPresenter();
        presenter.initDataUsers(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }

    private void initPresenter() {
        presenter = new UserPresenterImpl(this);
    }

    @Override
    public void showUsers(ArrayList<User> users) {
        this.users = users;
        initAdapter();
    }

    private void initAdapter() {
        myRecyclerView.setHasFixedSize(true);
        adapter = new UserAdapter(this, users);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        myRecyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = item.getOrder();
        User user = users.get(position);
        switch (item.getItemId()) {
            case UserAdapter.ROLE_MENU_ID:
                if (roles != null)
                    editRoles(user.getId());
                break;
            case UserAdapter.ACTIVE_MENU_ID:
                presenter.activeOrDeactiveUser(user.getId(), true);
                break;
            case UserAdapter.INACTIVE_MENU_ID:
                presenter.activeOrDeactiveUser(user.getId(), false);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void editRoles(final String user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(R.string.menu_role)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setItems(roles.toArray(new CharSequence[roles.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.updateUserRole(user_id, i + 1);
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void dissmissDialog() {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void showRoles(ArrayList<String> value) {
        this.roles = value;
    }

    private void setControls() {
        myRecyclerView = findViewById(R.id.recycleView);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.removeListerner();
    }
}
