package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.UserPresenter;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.RoleRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.UserView;

import java.util.ArrayList;

public class UserPresenterImpl implements UserPresenter {
    private UserView listener;
    private FirebaseUser currentUser;
    private UserRepository userRepository;
    private ValueEventListener eventListener;
    private RoleRepository roleRepository;

    public UserPresenterImpl(UserView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        roleRepository = new RoleRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void initDataUsers(Context context) {
        bidingCurrentUserRole(context);
        loadAllUser();
        loadAllRole();
    }

    private void bidingCurrentUserRole(final Context context) {
        eventListener = userRepository.findAndWatchRole(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Validate.validateCurrentUserRole(context, dataSnapshot.getValue(int.class),
                            new int[]{Constants.AdminRole});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadAllUser() {
        userRepository.findAndWatch(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<User> users = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        User user = item.getValue(User.class);
                        if (user.getId().equals(currentUser.getUid())) continue;
                        users.add(user);
                    }
                    listener.showUsers(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void loadAllRole() {
        roleRepository.findAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<String> roles = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        roles.add(item.getValue(String.class));
                    }
                    listener.showRoles(roles);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void activeOrDeactiveUser(String userId, boolean b) {
        userRepository.setStatus(userId, b ? Constants.UserEnable: Constants.UserDisable, null, null);
    }

    @Override
    public void updateUserRole(String userId, int roleId) {
        userRepository.setRole(userId, roleId, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.dissmissDialog();
            }
        }, null);
    }

    @Override
    public void removeListerner() {
        if (eventListener != null) {
            userRepository.removeFindAndWatchRoleListener(currentUser.getUid(), eventListener);
        }
    }
}
