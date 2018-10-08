package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public interface StorageRepository<T, ID> {
    void add(ID id, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void update(ID id, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void remove(ID id, OnSuccessListener successListener, OnFailureListener failureListener);

    void findAll(OnSuccessListener successListener, OnFailureListener failureListener);

    void findById(ID id, OnSuccessListener successListener, OnFailureListener failureListener);

    void findDownloadUriById(ID id, OnSuccessListener successListener, OnFailureListener failureListener);
}
