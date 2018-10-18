package com.thm.hoangminh.multimediamarket.view.callback;

import android.os.Bundle;

public interface RechargeView {

    public void showTotal(double balance);

    public void showMessage(String message);

    public void showMessageFromResource(int resource);

    public void startRechargeHistoryActivity(Bundle bundle);

}
