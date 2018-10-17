package com.thm.hoangminh.multimediamarket.repository.implement;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.config.api.ROUTE;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;
import com.thm.hoangminh.multimediamarket.repository.RechargeHistoryRepository;

public class RechargeHistoryRepositoryImpl implements RechargeHistoryRepository<RechargeHistory, String, ValueEventListener> {
    private DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();

    @Override
    public void add(RechargeHistory item, ValueEventListener listener) {

    }

    @Override
    public void update(RechargeHistory item, OnSuccessListener successListener, OnFailureListener failureListener) {

    }

    @Override
    public void remove(RechargeHistory item, ValueEventListener listener) {

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
    public void pushByDataRef(DatabaseReference dataRef, RechargeHistory rechargeHistory, OnSuccessListener successListener, OnFailureListener failureListener) {
        dataRef.setValue(rechargeHistory).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
    }
}
