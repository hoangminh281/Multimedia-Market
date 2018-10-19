package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface RatingRepository extends Repository<ProductRating, String> {
    void add(String uid, ProductRating productRating, OnSuccessListener<Void> onSuccessListener, Object o);
}
