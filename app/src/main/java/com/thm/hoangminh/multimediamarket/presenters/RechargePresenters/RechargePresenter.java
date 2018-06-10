package com.thm.hoangminh.multimediamarket.presenters.RechargePresenters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.Card;
import com.thm.hoangminh.multimediamarket.views.RechargeHistoryViews.RechargeHistoryActivity;
import com.thm.hoangminh.multimediamarket.views.RechargeViews.RechargeView;

import java.util.ArrayList;

public class RechargePresenter implements RechargeListener {
    private RechargeView listener;
    private RechargeInteractor interactor;
    private Context context;

    public RechargePresenter(Context context, RechargeView listener) {
        this.context = context;
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
        Intent intent = new Intent(context, RechargeHistoryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("trans_code", trans_id);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    @Override
    public void onRechargeCardFailure() {
        listener.showMessage(context.getResources().getString(R.string.infor_failure));
    }

    @Override
    public void onRechargeCardWrong() {
        listener.showMessage(context.getResources().getString(R.string.info_wrong));
    }

    public void RechargeCard(Card card, int category, int value) {
        interactor.RechargeCard(card, category, value);
    }
}
