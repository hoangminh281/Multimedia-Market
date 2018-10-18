package com.thm.hoangminh.multimediamarket.repository.base;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public interface Repository<T, ID> {
    void add(T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void update(T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void remove(T item, OnSuccessListener successListener, OnFailureListener failureListener);

    void findAll(ValueEventListener eventListener);

    void findById(ID id, ValueEventListener eventListener);
}
