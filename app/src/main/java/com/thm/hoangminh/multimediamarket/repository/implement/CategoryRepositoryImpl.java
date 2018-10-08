package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;

import java.util.ArrayList;

public class CategoryRepositoryImpl implements CategoryRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(Category item) {

    }

    @Override
    public void update(Category item) {

    }

    @Override
    public void remove(Category item) {

    }

    @Override
    public ArrayList<Category> findAll(ValueEventListener event) {
        mRef.child(ROUTE.CATEGORY()).addListenerForSingleValueEvent(event);
        return null;
    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }
}
