package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.repository.base.WatchingRepository;

public interface ProductDetailRepository extends WatchingRepository <ProductDetail, String> {
    void findPurchasedQuantityByProductId(String productId, ValueEventListener event);

    void setPurchasedQuantityByProductId(String productId, int purchasedQuantity, OnSuccessListener successListener, OnFailureListener failureListener);

    void updateImageId(String productId, String imageId, String newImageId, OnSuccessListener successListener, OnFailureListener failureListener);

    void pushImageId(String productId, String imageId, OnSuccessListener successListener, OnFailureListener failureListener);

    void setFileId(String productId, String fileId, OnSuccessListener successListener, OnFailureListener failureListener);

    void setCapacity(String productId, double capacity, OnSuccessListener successListener, OnFailureListener failureListener);

    void removeImageId(String productId, String oldImageKey, OnSuccessListener successListener, OnFailureListener failureListener);

    void setViewsByUserId(String productId, String userId, int value, OnSuccessListener successListener, OnFailureListener failureListener);
}
