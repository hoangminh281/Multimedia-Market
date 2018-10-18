package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargeHistory;

public interface RechargeHistoryView {
    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory);

    public void onLoadCardDetailSuccess(Card card);
}
