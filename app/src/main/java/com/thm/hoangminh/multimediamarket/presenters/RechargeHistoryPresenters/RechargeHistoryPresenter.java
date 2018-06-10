package com.thm.hoangminh.multimediamarket.presenters.RechargeHistoryPresenters;

import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;
import com.thm.hoangminh.multimediamarket.views.RechargeHistoryViews.RechargeHistoryView;

public class RechargeHistoryPresenter implements RechargeHistoryListener{

    private RechargeHistoryView listener;
    private RechargeHistoryInteractor interactor;

    public RechargeHistoryPresenter(RechargeHistoryView listener) {
        this.listener = listener;
        interactor = new RechargeHistoryInteractor(this);
    }

    public void LoadRechargeHistory(String trans_code) {
        interactor.LoadRechargeHistory(trans_code);
    }

    @Override
    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory) {
        listener.onLoadRechargeHistorySuccess(rechargeHistory);
        interactor.LoadCardDetail(rechargeHistory.getCard_id(), rechargeHistory.getCardCategory(), rechargeHistory.getCardValue());
    }

    @Override
    public void onLoadCardDetailSuccess(Card card) {
        listener.onLoadCardDetailSuccess(card);
    }
}
