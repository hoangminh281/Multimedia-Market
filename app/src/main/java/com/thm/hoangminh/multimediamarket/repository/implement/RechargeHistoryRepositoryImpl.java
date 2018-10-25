package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.api.ROUTE;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;
import com.thm.hoangminh.multimediamarket.repository.RechargeHistoryRepository;

public class RechargeHistoryRepositoryImpl implements RechargeHistoryRepository<RechargedHistory, String> {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(RechargedHistory item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void update(RechargedHistory item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(RechargedHistory item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void findAll(ValueEventListener event) {

    }

    @Override
    public void findById(String s, ValueEventListener event) {

    }

    @Override
    public DatabaseReference createDataRef(String userId) {
        return mRef.child(ROUTE.RECHARGEHISTORY(userId)).push();
    }

    @Override
    public void addByDataRef(DatabaseReference dataRef, RechargedHistory rechargedHistory, OnSuccessListener successListener, OnFailureListener failureListener) {
        dataRef.setValue(rechargedHistory).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }

    @Override
    public void findById(String userId, String transactionId, ValueEventListener eventListener) {
        mRef.child(ROUTE.RECHARGEHISTORY(userId, transactionId)).addListenerForSingleValueEvent(eventListener);
    }
}
