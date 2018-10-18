package com.thm.hoangminh.multimediamarket.presenter.RechargeHistoryPresenters;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargeHistory;
import com.thm.hoangminh.multimediamarket.presenter.callback.RechargeHistoryListener;

public class RechargeHistoryInteractor {
    private RechargeHistoryListener listener;
    private DatabaseReference mRef;
    private FirebaseUser firebaseUser;

    public RechargeHistoryInteractor(RechargeHistoryListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void LoadRechargeHistory(String trans_code) {
        mRef.child("recharge_histories/" + firebaseUser.getUid() + "/" + trans_code).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadRechargeHistorySuccess(dataSnapshot.getValue(RechargeHistory.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void LoadCardDetail(String card_id, int category, int value) {
        mRef.child("cards/" + category + "/" + value + "/" + card_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadCardDetailSuccess(dataSnapshot.getValue(Card.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
