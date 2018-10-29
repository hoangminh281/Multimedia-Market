package com.thm.hoangminh.multimediamarket.repository;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.repository.base.Repository;

public interface CardRepository extends Repository<Card, String> {
    void findAll(int category, int value, ValueEventListener eventListener);

    void setStatus(Card card, int status, OnSuccessListener successListener, OnFailureListener failureListener);

    void findById(Card card, ValueEventListener eventListener);

    void findAndWatchStatus(Card card, ValueEventListener eventListener);

    DatabaseReference createDataRef(int cardCategory, int cardValue);

    void addByDataRef(DatabaseReference dataRef, Card card, OnSuccessListener successListener, OnFailureListener failureListener);
}
