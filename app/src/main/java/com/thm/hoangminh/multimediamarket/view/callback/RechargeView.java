package com.thm.hoangminh.multimediamarket.view.RechargeViews;

public interface RechargeView extends BundleBaseView {
    void showTotal(double balance);

    void showMessage(int messageId);
}
