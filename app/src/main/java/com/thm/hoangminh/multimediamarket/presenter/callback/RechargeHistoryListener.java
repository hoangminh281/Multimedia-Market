package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargeHistory;

public interface RechargeHistoryListener {

    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory);

    public void onLoadCardDetailSuccess(Card card);
}
