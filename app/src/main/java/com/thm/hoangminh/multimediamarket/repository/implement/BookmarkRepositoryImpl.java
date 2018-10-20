package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.ProductBookmark;
import com.thm.hoangminh.multimediamarket.repository.BookmarkRepository;

public class BookmarkRepositoryImpl implements BookmarkRepository {
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(ProductBookmark item, OnSuccessListener successListener, OnFailureListener failureListener) {
        item.setValue(Constants.BookMarkEnable);
        update(item, successListener, failureListener);
    }

    @Override
    public void update(ProductBookmark item, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.BOOKMARK(item.getUserId(), item.getCateId(), item.getProductId())).setValue(item.getValue())
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void remove(ProductBookmark item, OnSuccessListener successListener, OnFailureListener failureListener) {
        item.setValue(Constants.BookMarkDisable);
        update(item, successListener, failureListener);
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
