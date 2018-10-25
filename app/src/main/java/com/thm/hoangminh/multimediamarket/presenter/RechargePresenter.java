package com.thm.hoangminh.multimediamarket.presenter;

import com.thm.hoangminh.multimediamarket.model.Card;

public interface RechargePresenter {
    void loadUserWallet();

    void rechargeCard(final Card card);
}
