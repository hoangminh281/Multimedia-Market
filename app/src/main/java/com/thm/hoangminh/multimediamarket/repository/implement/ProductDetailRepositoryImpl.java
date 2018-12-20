package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;

public class ProductDetailRepositoryImpl implements ProductDetailRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void findAndWatchById(String productId, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCTDETAIL(productId)).addValueEventListener(event);
    }

    @Override
    public void findAndWatch(ValueEventListener event) {

    }

    @Override
    public void add(ProductDetail productDetail, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL(productDetail.getId())).setValue(productDetail)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void update(final ProductDetail productDetail, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(productDetail, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(productDetail, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(ProductDetail productDetail, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL(productDetail.getId())).removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(ValueEventListener event) {
        mRef.child(ROUTE.PRODUCTDETAIL()).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findById(String s, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCTDETAIL(s)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void findPurchasedQuantityByProductId(String productId, ValueEventListener event) {
        mRef.child(ROUTE.PRODUCTDETAIL_PURCHASEDQUANTITY(productId)).addListenerForSingleValueEvent(event);
    }

    @Override
    public void setPurchasedQuantityByProductId(String productId, int purchasedQuantity, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_PURCHASEDQUANTITY(productId)).setValue(purchasedQuantity)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void updateImageId(String productId, String imageId, String newImageId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_IMAGELIST(productId, imageId)).setValue(newImageId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void pushImageId(String productId, String imageId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_IMAGELIST(productId)).push().setValue(imageId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setFileId(String productId, String fileId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_FILE(productId)).setValue(fileId)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setCapacity(String productId, double capacity, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_CAPACITY(productId)).setValue(capacity)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void removeImageId(String productId, String oldImageKey, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_IMAGELIST(productId, oldImageKey)).removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void setViewsByUserId(String productId, String userId, int value, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.PRODUCTDETAIL_VIEWS(productId, userId)).setValue(value)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
