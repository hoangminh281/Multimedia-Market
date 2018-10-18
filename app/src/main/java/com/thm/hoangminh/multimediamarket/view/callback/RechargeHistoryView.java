package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;

public interface RechargeHistoryView {
    public void onLoadRechargeHistorySuccess(RechargedHistory rechargedHistory);

    public void onLoadCardDetailSuccess(Card card);
}
