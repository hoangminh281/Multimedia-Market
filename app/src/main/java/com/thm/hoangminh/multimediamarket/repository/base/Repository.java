package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public interface Repository<T, ID, E> {
    void add(T item, E listener);

    void update(T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void remove(T item, E listener);

    void findAll(E event);

    void findById(ID id, E event);
}
