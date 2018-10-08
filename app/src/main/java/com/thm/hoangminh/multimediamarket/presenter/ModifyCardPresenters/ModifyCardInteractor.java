package com.thm.hoangminh.multimediamarket.presenter.ModifyCardPresenters;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Card;

public class ModifyCardInteractor {
    private ModifyCardListener listener;
    private DatabaseReference mRef;

    public ModifyCardInteractor(ModifyCardListener listener) {
        this.listener = listener;
        this.mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void createNewCard(Card card) {
        mRef.child("cards/" + card.getCategory() + "/" + card.getValue()).push().setValue(card).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.onCreateNewCardSuccess();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.onCreateNewCardFailure();
            }
        });
    }

    public void editCard(final Card newCard, final Card oldCard) {
        mRef.child("cards/" + newCard.getCategory() + "/" + newCard.getValue() + "/" + newCard.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    mRef.child("cards/" + newCard.getCategory() + "/" + newCard.getValue() + "/" + newCard.getId()).setValue(newCard).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            listener.onEditCardSuccess();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onEditCardFailure();
                        }
                    });
                } else {
                    mRef.child("cards/" + oldCard.getCategory() + "/" + oldCard.getValue() + "/" + oldCard.getId() + "/status").setValue(0).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRef.child("cards/" + newCard.getCategory() + "/" + newCard.getValue()).push().setValue(newCard).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    listener.onEditCardSuccess();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mRef.child("cards/" + oldCard.getCategory() + "/" + oldCard.getValue() + "/" + oldCard.getId() + "/status").setValue(1);
                                    listener.onEditCardFailure();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            listener.onEditCardFailure();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onEditCardFailure();
            }
        });
    }
}
