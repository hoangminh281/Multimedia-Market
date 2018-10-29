package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.AddUpdateCardPresenter;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.repository.CardRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CardRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.AddUpdateCardView;

public class AddUpdateCardPresenterImpl implements AddUpdateCardPresenter {
    private Card baseCard;
    private AddUpdateCardView listener;
    private CardRepository cardRepository;

    public AddUpdateCardPresenterImpl(AddUpdateCardView listener) {
        this.listener = listener;
        cardRepository = new CardRepositoryImpl();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        if (bundle != null) {
            listener.setTitle(R.string.menu_editCard);
            baseCard = bundle.getParcelable(Constants.CardObjectKey);
            listener.showCard(baseCard);
        } else {
            listener.setTitle(R.string.menu_addCard);
            listener.initCardUI();
        }
    }

    @Override
    public void addOrUpdateCard(Card card) {
        if (baseCard == null) {
            addCard(card);
        } else {
            updateCard(card);
        }
    }

    private void addCard(Card card) {
        DatabaseReference dataRef = cardRepository.createDataRef(card.getCategory(), card.getValue());
        card.setCardId(dataRef.getKey());
        card.setNumber(Tools.md5(card.getNumber()));
        cardRepository.addByDataRef(dataRef, card, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                listener.showMessage(R.string.info_successfully_create_card);
                listener.onBackScreenWithResult();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure_create_card);
            }
        });
    }

    private void updateCard(final Card card) {
        card.setCardId(baseCard.getCardId());
        if (card.getNumber() == null || card.getNumber().equals("")) {
            card.setNumber(baseCard.getNumber());
        } else {
            card.setNumber(Tools.md5(card.getNumber()));
        }
        cardRepository.remove(baseCard, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                cardRepository.add(card, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        listener.showMessage(R.string.info_success_edit_card);
                        listener.onBackScreenWithResult();
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_failure_edit_card);
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure_edit_card);
            }
        });
    }
}
