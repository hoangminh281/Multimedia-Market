package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.view.callback.base.BundleBaseView;

public interface RechargeView extends BundleBaseView {
    void showTotal(double balance);

    void showMessage(int messageId);
}
