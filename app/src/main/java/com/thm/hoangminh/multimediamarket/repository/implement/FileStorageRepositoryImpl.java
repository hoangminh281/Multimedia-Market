package com.thm.hoangminh.multimediamarket.repository.implement;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.repository.FileStorageRepository;

import java.util.concurrent.Callable;

public class FileStorageRepositoryImpl implements FileStorageRepository {
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();
    private BroadcastReceiver onComplete;

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
                add(item.getName(), item, successListener, failureListener);
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
    public void findUriById(String fileId, OnSuccessListener<Uri> successListener, OnFailureListener failureListener) {
        mStorageRef.child(ROUTE.FILE(fileId)).getDownloadUrl()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void updateOrCreate(String fileId, File updatedFile, OnSuccessListener<UploadTask.TaskSnapshot> successListener, OnFailureListener failureListener) {
        if (fileId != null) {
            update(fileId, updatedFile, successListener, failureListener);
        } else {
            add(updatedFile.getName(), updatedFile, successListener, failureListener);
        }
    }

    @Override
    public void downloadFile(final String fileId, final Context context, final Callable<?> callbackFunction, OnFailureListener failureListener) {
        findUriById(fileId, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileId);
                DownloadManager downloadManager = (DownloadManager) context.getSystemService(context.DOWNLOAD_SERVICE);
                final Long refid = downloadManager.enqueue(request);
                onComplete = new BroadcastReceiver() {
                    public void onReceive(Context ctxt, Intent intent) {
                        if (refid != 0) {
                            try {
                                callbackFunction.call();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                context.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        }, failureListener);
    }

    @Override
    public void unRegisterReceiver(Context context) {
        if (onComplete != null) {
            context.unregisterReceiver(onComplete);
        }
    }
}
