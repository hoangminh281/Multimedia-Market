package com.thm.hoangminh.multimediamarket.presenters.CardPresenters;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.models.Card;

import java.util.ArrayList;

public class CardInteractor {
    private CardListener listener;
    private DatabaseReference mRef;
    private ValueEventListener mListener;

    public CardInteractor(CardListener listener) {
        this.listener = listener;
        mRef = FirebaseDatabase.getInstance().getReference();
    }

    public void loadCardList() {
        mRef.child("cards").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> cardCategories = dataSnapshot.getChildren();
                ArrayList<Card> cards = new ArrayList<>();
                for (DataSnapshot cardCategory : cardCategories) {
                    Iterable<DataSnapshot> cardValues = cardCategory.getChildren();
                    for (DataSnapshot cardValue : cardValues) {
                        Iterable<DataSnapshot> cardItems = cardValue.getChildren();
                        for (DataSnapshot cardItem : cardItems) {
                            Card card = cardItem.getValue(Card.class);
                            card.setId(cardItem.getKey());
                            card.setCategory(Integer.parseInt(cardCategory.getKey()));
                            card.setValue(Integer.parseInt(cardValue.getKey()));
                            cards.add(card);
                        }
                    }
                }
                listener.onLoadCardListSuccess(cards);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void activeCard(int category, int value, String id, int i) {
        mRef.child("cards/" + category + "/" + value + "/" + id + "/status").setValue(i);
    }

    public void findCurrentUserRole() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mListener = mRef.child("users/" + currentUser.getUid() + "/role").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                    listener.onFindCurrentUserRoleSuccess(dataSnapshot.getValue(int.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void RemoveListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mRef.child("users/" + currentUser.getUid() + "/role").removeEventListener(mListener);
    }
}
