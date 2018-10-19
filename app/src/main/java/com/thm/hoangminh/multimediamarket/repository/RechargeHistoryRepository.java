package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface RechargeHistoryRepository<T, ID> extends Repository<T, ID> {
    DatabaseReference createDataRef(ID userId);

    void pushByDataRef(DatabaseReference dataRef, T clazz, OnSuccessListener successListener, OnFailureListener failureListener);
}