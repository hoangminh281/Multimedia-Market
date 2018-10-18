package com.thm.hoangminh.multimediamarket.repository.base;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;

public interface Repository<T, ID, E> {
    void add(T item, E listener);

    void update(T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void remove(T item, E listener);

    void findAll(E event);

    void findById(ID id, E event);
}
