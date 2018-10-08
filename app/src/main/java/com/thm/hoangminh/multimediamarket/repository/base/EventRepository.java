package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public interface EventRepository<T, ID, E> extends Repository<T, ID, E> {
    void addWithEventListener(ID userId, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void removeWithEventListener(ID userId, T item, OnSuccessListener successListener, OnFailureListener failureListener);
}
