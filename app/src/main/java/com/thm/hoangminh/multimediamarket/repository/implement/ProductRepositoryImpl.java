package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;
import com.thm.hoangminh.multimediamarket.models.Product;

import java.util.ArrayList;

public class ProductRepositoryImpl implements ProductRepository<Product, String, ValueEventListener> {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void findAndWatchById(String s, ValueEventListener event) {
    }

    @Override
    public void findAndWatch(ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT()).addValueEventListener(event);
    }

    @Override
    public void add(Product item) {

    }

    @Override
    public void update(Product item) {

    }

    @Override
    public void remove(Product item) {

    }

    @Override
    public ArrayList<Product> findAll(ValueEventListener event) {
        return null;
    }

    @Override
    public void findById(String id, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT(id)).addListenerForSingleValueEvent(event);
    }
}
