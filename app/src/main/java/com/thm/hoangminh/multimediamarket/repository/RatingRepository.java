package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface RatingRepository extends Repository<ProductRating, String> {
    void findByUserId(String uid, String productId, ValueEventListener eventListener);

    void findLikedRatingByUserId(String currentUserId, ProductRating productRating, ValueEventListener eventListener);

    void setValueLikedList(String currentUserId, ProductRating productRating, int value, OnSuccessListener successListener, OnFailureListener failureListener);
}
