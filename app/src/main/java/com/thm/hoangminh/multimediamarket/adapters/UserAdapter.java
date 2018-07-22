package com.thm.hoangminh.multimediamarket.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.ProfileViews.ProfileActivity;
import com.thm.hoangminh.multimediamarket.views.UserViews.UserActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.SingleItemRowHolder> {
    private ArrayList<User> itemsList;
    private Context mContext;

    public final static int ACTIVE_MENU_ID = 2222;
    public final static int INACTIVE_MENU_ID = 3333;
    public final static int ROLE_MENU_ID = 4444;

    public UserAdapter(Context context, ArrayList<User> itemsList) {
        this.itemsList = itemsList;
        this.mContext = context;
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_layout, null);
        UserAdapter.SingleItemRowHolder mh = new UserAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(SingleItemRowHolder holder, int position) {
        User user = itemsList.get(position);
        holder.user = user;
        user.LoadUserImageView(holder.imgAvatar, mContext);
        user.LoadUserImageGender(holder.imgGender);
        holder.txtUsername.setText(user.getName());
        user.LoadUserRoleWithColor(holder.txtRole, mContext);
        holder.txtEmail.setText(user.getEmail());
        user.LoadUserStatus(holder.imgStatus);
    }

    @Override
    public int getItemCount() {
        return itemsList == null ? 0 : itemsList.size();
    }

    public class SingleItemRowHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView txtUsername, txtRole, txtEmail;
        private ImageView imgAvatar, imgGender, imgStatus;
        private User user;

        public SingleItemRowHolder(View itemView) {
            super(itemView);
            txtUsername = itemView.findViewById(R.id.textViewUsername);
            txtRole = itemView.findViewById(R.id.textViewRole);
            txtEmail = itemView.findViewById(R.id.textViewEmail);
            imgAvatar = itemView.findViewById(R.id.imageViewAvatar);
            imgGender = itemView.findViewById(R.id.imageViewSex);
            imgStatus = itemView.findViewById(R.id.imageViewStatus);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", user.getId());
                    intent.putExtras(bundle);
                    ((Activity)mContext).startActivityForResult(intent, UserActivity.requestCode);
                }
            });
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, ROLE_MENU_ID, getAdapterPosition(), mContext.getResources().getString(R.string.menu_role));

            if (user.getStatus() == 0)
                contextMenu.add(0, ACTIVE_MENU_ID, getAdapterPosition(), mContext.getResources().getString(R.string.menu_active));
            else
                contextMenu.add(0, INACTIVE_MENU_ID, getAdapterPosition(), mContext.getResources().getString(R.string.menu_inactive));
        }
    }
}
