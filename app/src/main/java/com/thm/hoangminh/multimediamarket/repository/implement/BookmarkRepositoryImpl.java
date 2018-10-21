package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.ProductBookmark;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;

public class BookmarkRepositoryImpl implements BookmarkRepository {
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(ProductBookmark productBookmark, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.BOOKMARK(productBookmark.getUserId(), productBookmark.getCateId(), productBookmark.getProductId()))
                .setValue(productBookmark)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void update(final ProductBookmark productBookmark, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(productBookmark, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(productBookmark, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(ProductBookmark productBookmark, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.BOOKMARK(productBookmark.getUserId(), productBookmark.getCateId(), productBookmark.getProductId()))
                .removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(ValueEventListener eventListener) {

    }

    @Override
    public void findById(String s, ValueEventListener eventListener) {

    }

    @Override
    public void findByProductBookmark(ProductBookmark productBookmark, ValueEventListener eventListener) {
        mRef.child(ROUTE.BOOKMARK(productBookmark.getUserId(), productBookmark.getCateId(), productBookmark.getProductId()))
                .addListenerForSingleValueEvent(eventListener);
    }
}
