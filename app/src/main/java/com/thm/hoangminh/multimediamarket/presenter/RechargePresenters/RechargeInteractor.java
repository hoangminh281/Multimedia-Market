package com.thm.hoangminh.multimediamarket.presenter.RechargePresenters;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;
import com.thm.hoangminh.multimediamarket.presenter.callback.RechargeListener;
import com.thm.hoangminh.multimediamarket.utility.Validate;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RechargeInteractor {
    private RechargeListener listener;
    private DatabaseReference mRef;
    private FirebaseUser firebaseUser;

    public RechargeInteractor(RechargeListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void LoadUserWallet() {
        mRef.child("users/" + firebaseUser.getUid() + "/balance").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onLoadUserWalletSuccess(dataSnapshot.getValue(double.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public synchronized void RechargeCard(final Card card, final int category, final int value) {
        mRef.child("cards/" + category + "/" + value).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (final DataSnapshot item : iterable) {
                        Card card1 = item.getValue(Card.class);
                        if (card1.getStatus() == 0) continue;
                        if (Validate.validateSameCard(card1, card)) {
                            mRef.child("cards/" + category + "/" + value + "/" + item.getKey() + "/status").setValue(0)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mRef.child("users/" + firebaseUser.getUid() + "/balance")
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(final DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                mRef.child("users/" + firebaseUser.getUid() + "/balance")
                                                        .setValue(dataSnapshot.getValue(double.class)
                                                                + Constants.CardValueList[value])
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        SimpleDateFormat dateFormatter
                                                                = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
                                                        final DatabaseReference mRefTmp
                                                                = mRef.child("recharge_histories/" + firebaseUser.getUid())
                                                                .push();
                                                        mRefTmp.setValue(new RechargedHistory(mRefTmp.getKey()
                                                                , item.getKey(), category, value,
                                                                dateFormatter.format(Calendar.getInstance().getTime())))
                                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                listener.onRechargeCardSuccess(mRefTmp.getKey());
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                mRef.child("users/" + firebaseUser.getUid() + "/balance")
                                                                        .setValue(dataSnapshot.getValue(double.class));
                                                                mRef.child("cards/" + category + "/" + value + "/"
                                                                        + item.getKey() + "/status").setValue(1);
                                                                listener.onRechargeCardFailure();
                                                            }
                                                        });

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        mRef.child("cards/" + category + "/" + value + "/" + item.getKey() + "/status").setValue(1);
                                                        listener.onRechargeCardFailure();
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            mRef.child("cards/" + category + "/" + value + "/" + item.getKey() + "/status").setValue(1);
                                            listener.onRechargeCardFailure();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    listener.onRechargeCardFailure();
                                }
                            });
                            return;
                        } else {
                            listener.onRechargeCardWrong();
                            return;
                        }
                    }
                }
                listener.onRechargeCardWrong();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onRechargeCardWrong();
            }
        });
    }

}
