package com.thm.hoangminh.multimediamarket.repository.implement;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class ProductStorageRepositoryImpl implements ProductStorageRepository {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void add(String imageId, Bitmap item, OnSuccessListener successListener, OnFailureListener failureListener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        item.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child(ROUTE.USERSTORAGE_IMAGE(imageId)).putBytes(data);
        uploadTask.addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void update(final String imageId, final Bitmap item, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(imageId, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(imageId, item, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(String imageId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.USERSTORAGE_IMAGE(imageId)).delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findDownloadUriById(String productImageId, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.PRODUCTSTORAGE_IMAGE(productImageId)).getDownloadUrl()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
