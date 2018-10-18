package com.thm.hoangminh.multimediamarket.view.callback;

import android.content.Intent;

import com.thm.hoangminh.multimediamarket.view.callback.base.BaseView;

public interface SigninView extends BaseView {
    void hideLoadingProgressDialog();

    void showMessage(int messageId);

    void showLoadingProgressDialog();

    void startActivityForResult(Intent intent, int requestCode);
}
