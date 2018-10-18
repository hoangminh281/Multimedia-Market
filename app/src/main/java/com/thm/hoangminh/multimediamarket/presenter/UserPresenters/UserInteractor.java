package com.thm.hoangminh.multimediamarket.presenter.UserPresenters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.callback.UserListener;

import java.util.ArrayList;

public class UserInteractor {
    private UserListener listener;
    private DatabaseReference mRef;
    private FirebaseUser currentUser;
    private ValueEventListener mListener;

    public UserInteractor(UserListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void findAll() {
        mRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<User> users = new ArrayList<>();
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        if (item.child("role").getValue(int.class) == User.ADMIN
                                || item.getKey().equals(currentUser.getUid()))
                            continue;
                        users.add(item.getValue(User.class));
                    }
                    listener.onFindAllSuccess(users);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void active(String user_id, int i) {
        mRef.child("users/" + user_id + "/status").setValue(i);
    }

    public void findRoles() {
        mRef.child("roles").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ArrayList<String> roleNames = (ArrayList<String>) dataSnapshot.getValue();
                    roleNames.remove(0);
                    listener.onFindRolesSuccess(roleNames);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateRoleById(String user_id, int role) {
        mRef.child("users/" + user_id + "/role").setValue(role).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onUpdateRoleByIdSuccess();
            }
        });
    }

    public void findCurrentUserRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListener = mRef.child("users/" + currentUser.getUid() + "/role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                listener.onFindCurrentUserRoleSuccess(dataSnapshot.getValue(int.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void RemoveListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("users/" + currentUser.getUid() + "/role").removeEventListener(mListener);
    }
}
