package com.thm.hoangminh.multimediamarket.repository;

import android.content.Context;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;

import java.util.concurrent.Callable;

public interface FileStorageRepository extends StorageRepository<File, String> {
    void updateOrCreate(String fileId, File updatedFile, OnSuccessListener<UploadTask.TaskSnapshot> successListener, OnFailureListener failureListener);

    void downloadFile(final String fileId, final Context context, final Callable<?> callbackFunction, OnFailureListener failureListener);

    void unRegisterReceiver(Context context);
}
