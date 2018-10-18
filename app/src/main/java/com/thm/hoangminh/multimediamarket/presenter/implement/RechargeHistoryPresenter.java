package com.thm.hoangminh.multimediamarket.presenter.implement;

import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.model.RechargeHistory;
import com.thm.hoangminh.multimediamarket.presenter.RechargeHistoryPresenters.RechargeHistoryInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.RechargeHistoryListener;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeHistoryView;

public class RechargeHistoryPresenter implements RechargeHistoryListener {

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
