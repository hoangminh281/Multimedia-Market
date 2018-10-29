package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;

public class CardRepositoryImpl implements CardRepository {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(Card card, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.CARD(card.getCategory(), card.getValue(), card.getCardId())).setValue(card)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void update(final Card card, final OnSuccessListener successListener, final OnFailureListener failureListener) {
        remove(card, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                add(card, successListener, failureListener);
            }
        }, failureListener);
    }

    @Override
    public void remove(Card card, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.CARD(card.getCategory(), card.getValue(), card.getCardId())).removeValue()
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);
    }

    @Override
    public void findAll(ValueEventListener eventListener) {
        mRef.child(ROUTE.CARD()).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void findById(String s, ValueEventListener eventListener) {

    }

    @Override
    public void findAll(int category, int value, ValueEventListener eventListener) {
        mRef.child(ROUTE.CARD(category, value)).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void setStatus(Card card, int status, OnSuccessListener successListener, OnFailureListener failureListener) {
        mRef.child(ROUTE.CARD_STATUS(card.getCategory(), card.getValue(), card.getCardId())).setValue(status)
                .addOnSuccessListener(successListener)
                .addOnFailureListener(failureListener);

    }

    @Override
    public void findById(Card card, ValueEventListener eventListener) {
        mRef.child(ROUTE.CARD(card.getCategory(), card.getValue(), card.getCardId())).addListenerForSingleValueEvent(eventListener);
    }

    @Override
    public void findAndWatchStatus(Card card, ValueEventListener eventListener) {
        mRef.child(ROUTE.CARD_STATUS(card.getCategory(), card.getValue(), card.getCardId()))
                .addValueEventListener(eventListener);
    }

    @Override
    public DatabaseReference createDataRef(int cardCategory, int cardValue) {
        return mRef.child(ROUTE.CARD(cardCategory, cardValue)).push();
    }

    @Override
    public void addByDataRef(DatabaseReference dataRef, Card card, OnSuccessListener successListener, OnFailureListener failureListener) {
        dataRef.setValue(card).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void findAndWatchById(String s, ValueEventListener eventListener) {

    }

    @Override
    public void findAndWatch(ValueEventListener eventListener) {
        mRef.child(ROUTE.CARD()).addValueEventListener(eventListener);
    }
}
