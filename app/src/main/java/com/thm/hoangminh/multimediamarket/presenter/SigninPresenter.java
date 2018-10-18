package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Intent;

public interface SigninPresenter {
    void addSigninListener();

    void removeSigninListener();

    void signin(String username, String password);

    void signinGG();

    void activityResultCallback(int requestCode, int resultCode, Intent data);
}
