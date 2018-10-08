package com.thm.hoangminh.multimediamarket.presenter.RechargePresenters;

public interface RechargeListener {

    public void onLoadUserWalletSuccess(double balance);

    public void onRechargeCardSuccess(String trans_id);

    public void onRechargeCardFailure();

    public void onRechargeCardWrong();

}
