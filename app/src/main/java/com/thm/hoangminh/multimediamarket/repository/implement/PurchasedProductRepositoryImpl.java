package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.PurchasedProduct;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;

public class PurchasedProductRepositoryImpl implements PurchasedProductRepository {

    @Override
    public void add(PurchasedProduct item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(PurchasedProduct item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(PurchasedProduct item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }

    @Override
    public void findAll(String cateId, String userId, ValueEventListener eventListener) {

    }

    @Override
    public void findAndWatch(String uid, String cateId, String productId, ValueEventListener eventListener) {

    }
}
