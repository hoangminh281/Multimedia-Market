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
    public void add(Card item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(Card item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(Card item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener eventListener) {

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
}
