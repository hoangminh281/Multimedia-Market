package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Card;
import com.thm.hoangminh.multimediamarket.presenter.RechargePresenters.RechargeInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.RechargeListener;
import com.thm.hoangminh.multimediamarket.view.callback.RechargeView;

public class RechargePresenter implements RechargeListener {
    private RechargeView listener;
    private RechargeInteractor interactor;

    public RechargePresenter(RechargeView listener) {
        this.listener = listener;
        this.interactor = new RechargeInteractor(this);
    }

    public void LoadUserWallet() {
        this.interactor.LoadUserWallet();
    }

    @Override
    public void onLoadUserWalletSuccess(double balance) {
        listener.showTotal(balance);
    }

    @Override
    public void onRechargeCardSuccess(String trans_id) {
        Bundle bundle = new Bundle();
        bundle.putString("trans_code", trans_id);
        listener.startRechargeHistoryActivity(bundle);
    }

    @Override
    public void onRechargeCardFailure() {
        listener.showMessageFromResource(R.string.infor_failure);
    }

    @Override
    public void onRechargeCardWrong() {
        listener.showMessageFromResource(R.string.info_wrong);
    }

    public void RechargeCard(Card card, int category, int value) {
        interactor.RechargeCard(card, category, value);
    }
}
