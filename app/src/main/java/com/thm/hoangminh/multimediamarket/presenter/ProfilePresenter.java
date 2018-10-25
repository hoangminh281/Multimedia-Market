package com.thm.hoangminh.multimediamarket.presenter;

import android.graphics.Bitmap;
import android.os.Bundle;

public interface ProfilePresenter {
    void extractBundle(Bundle bundle);

    void updateImageCurrentUser(Bitmap bitmap);
    
    void updateUserBalance(double balance);

    void updateUsername(String userName);

    void updateUserEmail(String email);

    void updatePassword(String password);

    void updateBirthday(int dayOfMonth, int i, int year);

    void updateGender(int i);

    void redirectToProductActivity(int i);

    void removeAvatarUser();
}
