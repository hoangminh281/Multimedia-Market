package com.thm.hoangminh.multimediamarket.presenters.RechargePresenters;

import java.util.ArrayList;

public interface RechargeListener {

    public void onLoadUserWalletSuccess(double balance);

    public void onRechargeCardSuccess(String trans_id);

    public void onRechargeCardFailure();

    public void onRechargeCardWrong();

}
