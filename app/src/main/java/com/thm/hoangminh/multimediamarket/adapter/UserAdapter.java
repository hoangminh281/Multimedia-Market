package com.thm.hoangminh.multimediamarket.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.RoleRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.ImageLoader;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.activity.ProfileActivity;
import com.thm.hoangminh.multimediamarket.view.activity.UserActivity;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.SingleItemRowHolder> {
    private Context context;
    private ArrayList<User> users;
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    public final static int ACTIVE_MENU_ID = 2222;
    public final static int INACTIVE_MENU_ID = 3333;
    public final static int ROLE_MENU_ID = 4444;

    public UserAdapter(Context context, ArrayList<User> users) {
        this.users = users;
        this.context = context;
        userRepository = new UserRepositoryImpl();
        roleRepository = new RoleRepositoryImpl();
    }

    @Override
    public SingleItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_layout, null);
        UserAdapter.SingleItemRowHolder mh = new UserAdapter.SingleItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final SingleItemRowHolder holder, int position) {
        final User user = users.get(position);
        holder.user = user;
        ImageLoader.loadImage(UserStorageRepository.class, context, holder.imgAvatar, user.getImage());
        holder.imgGender.setImageResource(Validate.validateGenderToMipmap(user.getSex()));
        holder.txtRole.setTextColor(context.getResources().getColor(Validate.validateRoleToColor(user.getRole())));
        roleRepository.findById(user.getRole(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    holder.txtRole.setText(dataSnapshot.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userRepository.findAndWatchStatus(user.getId(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int status = dataSnapshot.getValue(int.class);
                    switch (status) {
                        case Constants.UserEnable:
                            holder.imgStatus.setColorFilter(R.color.theme_app);
                            break;
                        case Constants.UserDisable:
                            holder.imgStatus.clearColorFilter();
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.txtUsername.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
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
                    Intent intent = new Intent(context, ProfileActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.UserIdKey, user.getId());
                    intent.putExtras(bundle);
                    ((Activity) context).startActivityForResult(intent, UserActivity.requestCode);
                }
            });
            itemView.setLongClickable(true);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, ROLE_MENU_ID, getAdapterPosition(), context.getResources().getString(R.string.menu_role));
            if (user.getStatus() == 0)
                contextMenu.add(0, ACTIVE_MENU_ID, getAdapterPosition(), context.getResources().getString(R.string.menu_active));
            else
                contextMenu.add(0, INACTIVE_MENU_ID, getAdapterPosition(), context.getResources().getString(R.string.menu_inactive));
        }
    }
}
