package com.thm.hoangminh.multimediamarket.views.RechargeHistoryViews;

import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;

public interface RechargeHistoryView {
    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory);

    public void onLoadCardDetailSuccess(Card card);
}
