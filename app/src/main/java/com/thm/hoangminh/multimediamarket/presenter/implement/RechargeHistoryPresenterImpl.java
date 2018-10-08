package com.thm.hoangminh.multimediamarket.presenter.service.implement;

import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.models.RechargeHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargeHistoryPresenters.RechargeHistoryInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.RechargeHistoryListener;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeHistoryView;

public class RechargeHistoryPresenterImpl implements RechargeHistoryListener {

    private RechargeHistoryView listener;
    private RechargeHistoryInteractor interactor;

    public RechargeHistoryPresenterImpl(RechargeHistoryView listener) {
        this.listener = listener;
        interactor = new RechargeHistoryInteractor(this);
    }

    public void LoadRechargeHistory(String trans_code) {
        interactor.LoadRechargeHistory(trans_code);
    }

    @Override
    public void onLoadRechargeHistorySuccess(RechargeHistory rechargeHistory) {
        listener.onLoadRechargeHistorySuccess(rechargeHistory);
        interactor.LoadCardDetail(rechargeHistory.getCardId(), rechargeHistory.getCardCategory(), rechargeHistory.getCardValue());
    }

    @Override
    public void onLoadCardDetailSuccess(Card card) {
        listener.onLoadCardDetailSuccess(card);
    }
}
