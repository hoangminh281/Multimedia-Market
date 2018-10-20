package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;

public class RatingRepositoryImpl implements RatingRepository {
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(ProductRating item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(ProductRating item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(ProductRating item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String productId, ValueEventListener eventListener) {
        mRef.child(ROUTE.RATING(productId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void findAndWatchByUserId(String uid, String productId, ValueEventListener eventListener) {
        mRef.child(ROUTE.RATING(uid, productId)).addValueEventListener(eventListener);
    }
}
