package com.thm.hoangminh.multimediamarket.repository.implement;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;

public class FileStorageRepositoryImpl implements FileStorageRepository {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    @Override
    public void add(String fileId, File item, OnSuccessListener successListener, OnFailureListener failureListener) {
        StorageReference cloneStorageRef = mStorageRef.child(ROUTE.FILE(fileId));
        UploadTask uploadTask = cloneStorageRef.putFile(item.getUri());
        uploadTask.addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void update(final String fileId, final File item, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(item.getName(), new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(fileId, item, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(String fileId, OnSuccessListener successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.FILE(fileId)).delete()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(OnSuccessListener successListener, OnFailureListener failureListener) {
    }

    @Override
    public void findById(String s, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findDownloadUriById(String fileId, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.FILE(fileId)).getDownloadUrl()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }
}
