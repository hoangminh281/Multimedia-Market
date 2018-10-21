package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.base.WatchingRepository;

public interface ProductRepository extends WatchingRepository<Product, String> {
    void setRatingPoint(String productId, double ratingPoint, OnSuccessListener successListener, OnFailureListener failureListener);

    void findPriceByProductId(String productId, ValueEventListener eventListener);

    void setStatus(String productId, int status, OnSuccessListener successListener, OnFailureListener failureListener);
}
