package com.thm.hoangminh.multimediamarket.repository.base;

import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public interface StorageRepository<T, ID> {
    void add(ID id, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void update(ID id, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void remove(ID id, OnSuccessListener successListener, OnFailureListener failureListener);

    void findUriById(ID id, OnSuccessListener<Uri> successListener, OnFailureListener failureListener);
}
