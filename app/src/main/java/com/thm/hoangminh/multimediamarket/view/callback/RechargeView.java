package com.thm.hoangminh.multimediamarket.view.RechargeViews;

import android.os.Bundle;

public interface RechargeView {

    public void showTotal(double balance);

    public void showMessage(String message);

    public void showMessageFromResource(int resource);

    public void startRechargeHistoryActivity(Bundle bundle);

}
