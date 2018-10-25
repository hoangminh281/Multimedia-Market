package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;

public interface RechargeHistoryView {
    void showRechargeHistory(RechargedHistory rechargedHistory);

    void showCard(Card card);
}
