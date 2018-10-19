package com.thm.hoangminh.multimediamarket.repository.base;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public interface EventRepository<T, ID> extends Repository<T, ID> {
    void addWithEventListener(ID userId, T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void removeWithEventListener(ID userId, T item, OnSuccessListener successListener, OnFailureListener failureListener);
}
