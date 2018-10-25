package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargeHistoryPresenter;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.RechargeHistoryRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RechargeHistoryRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeHistoryView;

public class RechargeHistoryPresenterImpl implements RechargeHistoryPresenter {
    private String transactionId;
    private FirebaseUser currentUser;
    private RechargeHistoryView listener;
    private CardRepository cardRepository;
    private RechargeHistoryRepository rechargeHistoryRepository;

    public RechargeHistoryPresenterImpl(RechargeHistoryView listener) {
        this.listener = listener;
        cardRepository = new CardRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        rechargeHistoryRepository = new RechargeHistoryRepositoryImpl();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        if (bundle != null) {
            transactionId = bundle.getString(Constants.TransactionKey);
            loadRechargedHistory(transactionId);
        }

    }

    public void loadRechargedHistory(String transactionId) {
        rechargeHistoryRepository.findById(currentUser.getUid(), transactionId,
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            RechargedHistory rechargedHistory = dataSnapshot.getValue(RechargedHistory.class);
                            listener.showRechargeHistory(rechargedHistory);
                            loadCard(rechargedHistory.getCardCategory(), rechargedHistory.getCardValue(), rechargedHistory.getCardId());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadCard(int cardCategory, int cardValue, String cardId) {
        Card card = new Card();
        card.setCategory(cardCategory);
        card.setValue(cardValue);
        card.setCardId(cardId);
        cardRepository.findById(card, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    listener.showCard(dataSnapshot.getValue(Card.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
