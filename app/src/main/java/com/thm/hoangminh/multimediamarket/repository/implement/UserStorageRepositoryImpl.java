package com.thm.hoangminh.multimediamarket.repository.implement;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;

import java.io.ByteArrayOutputStream;

public class UserStorageRepositoryImpl implements UserStorageRepository {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void add(String photoId, Bitmap item, OnSuccessListener successListener, OnFailureListener failureListener) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        item.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = mStorageRef.child(ROUTE.USERSTORAGE_IMAGE(photoId)).putBytes(data);
        uploadTask.addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void update(final String photoId, final Bitmap item, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(photoId, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(photoId, item, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(String photoId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.USERSTORAGE_IMAGE(photoId)).delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findDownloadUriById(String userImageId, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.USERSTORAGE_IMAGE(userImageId)).getDownloadUrl()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
