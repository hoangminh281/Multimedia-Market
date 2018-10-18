package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.adapter.UserAdapter;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.implement.UserPresenter;
import com.thm.hoangminh.multimediamarket.view.callback.UserView;

import java.util.ArrayList;

public class UserActivity extends AppCompatActivity implements UserView {
    private UserAdapter adapter;
    private RecyclerView myRecyclerView;
    private ArrayList<User> users;
    private UserPresenter presenter;
    private AlertDialog dialog;
    private ArrayList<String> roles;
    private Toolbar toolbar;

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

        presenter.findCurrentUserRole();

        presenter.findAll();

        presenter.findRoles();

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
        presenter = new UserPresenter(this);
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
                    EditRoles(user.getId());
                break;
            case UserAdapter.ACTIVE_MENU_ID:
                presenter.activeUser(user.getId(), 1);
                break;
            case UserAdapter.INACTIVE_MENU_ID:
                presenter.activeUser(user.getId(), 0);
                break;
        }
        return super.onContextItemSelected(item);
    }

    public void EditRoles(final String user_id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Set the dialog title
        builder.setTitle(R.string.menu_role)
                // Specify the list array, the items to be selected by default (null for none),
                // and the listener through which to receive callbacks when items are selected
                .setItems(roles.toArray(new CharSequence[roles.size()]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        presenter.setRoles(user_id, i + 1);
                    }
                });
        dialog = builder.create();
        dialog.show();
    }

    @Override
    public void ShowUserList(ArrayList<User> users) {
        this.users = users;
        initAdapter();
    }

    @Override
    public void dissmissDialog() {
        if (dialog.isShowing()) dialog.dismiss();
    }

    @Override
    public void showRoles(ArrayList<String> value) {
        this.roles = value;
    }

    @Override
    public void bindingUserRole(Integer value) {
        if (value != User.ADMIN) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(UserActivity.this, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == this.requestCode && resultCode == RESULT_OK) {
            if (data.getExtras().getInt("result") == 1)
                presenter.findAll();
        }
    }

    private void setControls() {
        myRecyclerView = findViewById(R.id.recycleView);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.RemoveListener();
    }
}
