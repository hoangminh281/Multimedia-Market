package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.base.WatchingRepository;

public interface ProductRepository extends WatchingRepository<Product, String, ValueEventListener> {
    void setRatingPoint(String productId, double ratingPoint);

    void findPriceByProductId(String productId, ValueEventListener eventListener);
}
