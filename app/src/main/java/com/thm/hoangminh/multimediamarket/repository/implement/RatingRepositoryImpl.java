package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;

public class RatingRepositoryImpl implements RatingRepository {
    @Override
    public void add(String uid, ProductRating productRating, OnSuccessListener<Void> onSuccessListener, Object o) {

    }

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
    public void findById(String s, ValueEventListener event) {

    }
}
