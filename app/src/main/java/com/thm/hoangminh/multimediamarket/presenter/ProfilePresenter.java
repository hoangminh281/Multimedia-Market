package com.thm.hoangminh.multimediamarket.presenter.service;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.view.callback.BundleBaseView;

public interface ProfilePresenter{
    void extractBundle(Bundle bundle);

    void updateImageCurrentUser(Bitmap bitmap);

    Double getUserBalance();

    void updateUserBalance(double balance);

    String getUserName();

    void updateUsername(String userName);

    String getUserEmail();

    void updatePassword(String password);

    void updateUserEmail(String email);

    String getUserBirthday();

    void updateBirthday(int day, int month, int year);

    void updateGender(int genderId);

    void redirectToProductActivity(int cateIndex);
}
