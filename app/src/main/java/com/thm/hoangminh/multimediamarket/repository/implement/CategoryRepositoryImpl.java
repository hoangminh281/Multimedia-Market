package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.repository.CategoryRepository;

import java.util.ArrayList;

public class CategoryRepositoryImpl implements CategoryRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(Category item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(Category item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(Category item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {
        mRef.child(ROUTE.CATEGORY()).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }
}
