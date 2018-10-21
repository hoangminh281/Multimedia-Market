package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.PurchasedProduct;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;

public class PurchasedProductRepositoryImpl implements PurchasedProductRepository {
    DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(PurchasedProduct purchasedProduct, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PURCHASEDPRODUCT(purchasedProduct.getUserId(), purchasedProduct.getCateId(), purchasedProduct.getProductId()))
                .setValue(purchasedProduct)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void update(final PurchasedProduct purchasedProduct, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(purchasedProduct, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(purchasedProduct, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(PurchasedProduct purchasedProduct, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PURCHASEDPRODUCT(purchasedProduct.getUserId(), purchasedProduct.getCateId(), purchasedProduct.getProductId()))
                .removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
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
        mRef.child(ROUTE.PURCHASEDPRODUCT(uid, cateId, productId))
                .addValueEventListener(eventListener);
    }
}
