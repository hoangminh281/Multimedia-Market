package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface RatingRepository extends Repository<ProductRating, String> {
    void findByUserId(String uid, String productId, ValueEventListener eventListener);
}
