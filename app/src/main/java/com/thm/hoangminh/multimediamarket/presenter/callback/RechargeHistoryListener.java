package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargedHistory;

public interface RechargeHistoryListener {

    public void onLoadRechargeHistorySuccess(RechargedHistory rechargedHistory);

    public void onLoadCardDetailSuccess(Card card);
}
