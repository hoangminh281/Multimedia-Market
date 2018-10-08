package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;

import java.util.ArrayList;

public class UserRepositoryImpl implements UserRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(User user) {
        mRef.child(ROUTE.USER(user.getId())).setValue(user);
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }

    @Override
    public ArrayList<User> findAll(ValueEventListener event) {
        return null;
    }

    @Override
    public void findById(String id, ValueEventListener event) {
        mRef.child(ROUTE.USER(id)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findAndWatchById(String id, ValueEventListener event) {
        mRef.child(ROUTE.USER(id)).addValueEventListener(event);
    }

    @Override
    public void findAndWatch(ValueEventListener event) {

    }
}
