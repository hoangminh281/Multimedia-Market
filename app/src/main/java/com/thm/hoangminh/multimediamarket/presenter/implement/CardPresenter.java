package com.thm.hoangminh.multimediamarket.presenter.implement;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.CardPresenters.CardInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.CardListener;
import com.thm.hoangminh.multimediamarket.view.callback.CardView;

import java.util.ArrayList;

public class CardPresenter implements CardListener {
    private CardView listener;
    private CardInteractor interactor;

    public CardPresenter(CardView listener) {
        this.listener = listener;
        interactor = new CardInteractor(this);
    }

    public void loadCardList() {
        interactor.loadCardList();
    }

    @Override
    public void onLoadCardListSuccess(ArrayList<Card> cards) {
        listener.showCardList(cards);
    }

    @Override
    public void onFindCurrentUserRoleSuccess(Integer value) {
        listener.bindingUserRole(value);
    }

    public void activeCard(int category, int value, String id, int i) {
        interactor.activeCard(category, value, id, i);
    }

    public void RemoveListener() {
        interactor.RemoveListener();
    }

    public void findCurrentUserRole() {
        interactor.findCurrentUserRole();
    }
}
