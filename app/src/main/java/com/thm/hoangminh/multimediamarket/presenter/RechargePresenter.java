package com.thm.hoangminh.multimediamarket.presenter.service;

import com.thm.hoangminh.multimediamarket.models.Card;

public interface RechargePresenter {
    void loadUserWallet();

    void rechargeCard(Card card, int checkPositionCardCategory, int checkedPositionCardValue);
}
