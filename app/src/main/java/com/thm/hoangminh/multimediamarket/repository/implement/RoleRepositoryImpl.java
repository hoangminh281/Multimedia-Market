package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;

import java.util.ArrayList;

public class RoleRepositoryImpl implements RoleRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(String item) {

    }

    @Override
    public void update(String item) {

    }

    @Override
    public void remove(String item) {

    }

    @Override
    public ArrayList<String> findAll(ValueEventListener event) {
        return null;
    }

    @Override
    public void findById(Integer RID, ValueEventListener event) {
        mRef.child(ROUTE.ROLE(RID)).addListenerForSingleValueEvent(event);
    }
}
