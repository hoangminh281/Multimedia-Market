package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;

import java.util.ArrayList;

public class RoleRepositoryImpl implements RoleRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(String item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(String item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(String item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {
        mRef.child(ROUTE.ROLE()).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findById(Integer RID, ValueEventListener event) {
        mRef.child(ROUTE.ROLE(RID)).addListenerForSingleValueEvent(event);
    }
}
