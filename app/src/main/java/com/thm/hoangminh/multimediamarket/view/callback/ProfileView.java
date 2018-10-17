package com.thm.hoangminh.multimediamarket.view.ProfileViews;

import com.thm.hoangminh.multimediamarket.models.User;

public interface ProfileView  extends BundleBaseView {
     void ShowCurrentUserInformation(User user);

     void showGameNumber(int size);

     void showImageNumber(int size);

     void showVideoNumber(int size);

     void showMusicNumber(int size);

     void EnableChangeCurrentUserEmail();

     void EnableChangeCurrentUserPassword();

     void showMessage(int messageId);

     void dismissDialog();

     void showProgresbarDialog();

    void hideProgresbarDialog();

    void editable();

    void showEditableBalance(boolean b);

    void showUserRole(String role);
}
