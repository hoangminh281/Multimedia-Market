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
    public void add(ProductRating productRating, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.RATING(productRating.getUserId(), productRating.getProductId())).setValue(productRating)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void update(final ProductRating productRating, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(productRating, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(productRating, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(ProductRating productRating, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.RATING(productRating.getUserId(), productRating.getProductId())).removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(ValueEventListener event) {
        mRef.child(ROUTE.RATING()).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findById(String productId, ValueEventListener eventListener) {
        mRef.child(ROUTE.RATING(productId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void findByUserId(String uid, String productId, ValueEventListener eventListener) {
        mRef.child(ROUTE.RATING(uid, productId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void findLikedRatingByUserId(String currentUserId, ProductRating productRating, ValueEventListener eventListener) {
        mRef.child(ROUTE.RATING_LIKEDLIST(currentUserId, productRating.getUserId(), productRating.getProductId()))
                .addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void setValueLikedList(String currentUserId, ProductRating productRating, int value, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.RATING_LIKEDLIST(currentUserId, productRating.getUserId(), productRating.getProductId()))
                .setValue(value)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
