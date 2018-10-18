package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void findAndWatchById(String s, ValueEventListener event) {
    }

    @Override
    public void findAndWatch(ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT()).addValueEventListener(event);
    }

    @Override
    public void add(Product item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(Product item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(Product item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String productId, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT(productId)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void setRatingPoint(String productId, double ratingPoint) {

    }

    @Override
    public void findPriceByProductId(String productId, ValueEventListener eventListener) {

    }
}
