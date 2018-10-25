package com.thm.hoangminh.multimediamarket.repository;

import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.PurchasedProduct;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface PurchasedProductRepository extends Repository<PurchasedProduct, String> {
    void findAll(String userId, String cateId, ValueEventListener eventListener);

    void findAndWatch(String userId, String cateId, String productId, ValueEventListener eventListener);
}
