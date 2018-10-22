package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;

public class ProductRepositoryImpl implements ProductRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void findAndWatchById(String productId, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT(productId)).addValueEventListener(event);
    }

    @Override
    public void findAndWatch(ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT()).addValueEventListener(event);
    }

    @Override
    public void add(Product product, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCT(product.getProductId())).setValue(product).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void update(final Product product, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(product, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(product, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(Product product, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCT(product.getProductId())).removeValue().addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String productId, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCT(productId)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void setRatingPoint(String productId, double ratingPoint, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCT_RATING(productId)).setValue(ratingPoint)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findPriceByProductId(String productId, ValueEventListener eventListener) {
        mRef.child(ROUTE.PRODUCT_PRICE(productId)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void setStatus(String productId, int status, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCT(productId)).setValue(status)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
