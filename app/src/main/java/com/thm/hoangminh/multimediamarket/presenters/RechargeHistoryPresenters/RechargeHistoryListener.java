package com.thm.hoangminh.multimediamarket.presenters.RechargeHistoryPresenters;

import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;

public interface RechargeHistoryListener {

    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory);

    public void onLoadCardDetailSuccess(Card card);
}
