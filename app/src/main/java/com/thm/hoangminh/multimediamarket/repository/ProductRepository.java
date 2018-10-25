package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.base.WatchingRepository;

public interface ProductRepository extends WatchingRepository<Product, String> {
    void setRatingPoint(String productId, double ratingPoint, OnSuccessListener successListener, OnFailureListener failureListener);

    void findPriceByProductId(String productId, ValueEventListener eventListener);

    void setStatus(String productId, int status, OnSuccessListener successListener, OnFailureListener failureListener);

    DatabaseReference createDataRef();

    void addByDataRef(DatabaseReference dataRef, Product clazz, OnSuccessListener successListener, OnFailureListener failureListener);
}
