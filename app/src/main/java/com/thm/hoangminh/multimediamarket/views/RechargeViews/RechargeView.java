package com.thm.hoangminh.multimediamarket.views.RechargeViews;

import android.app.Activity;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.views.RechargeHistoryViews.RechargeHistoryActivity;

import java.util.ArrayList;

public interface RechargeView {

    public void showTotal(double balance);

    public void showMessage(String message);

    public void showMessageFromResource(int resource);

    public void startRechargeHistoryActivity(Bundle bundle);

}
