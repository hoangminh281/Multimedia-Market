package com.thm.hoangminh.multimediamarket.presenter.service;

import android.content.Intent;

public interface SigninPresenter {
    void addSigninListener();

    void removeSigninListener();

    void signin();

    void signinGG();

    void activityResultCallback(int requestCode, int resultCode, Intent data);
}
