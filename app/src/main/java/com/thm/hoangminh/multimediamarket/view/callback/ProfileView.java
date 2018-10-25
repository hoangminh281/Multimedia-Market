package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.view.callback.base.BundleBaseView;

public interface ProfileView extends BundleBaseView {
    void showEditableBalance(boolean b);

    void showCurrentUserInformation(User currentUser);

    void showUserRole(String value);

    void showGameNumber(int size);

    void showImageNumber(int size);

    void showVideoNumber(int size);

    void showMusicNumber(int size);

    void enableChangeCurrentUserEmail();

    void enableChangeCurrentUserPassword();

    void showMessage(int messageId);

    void dismissDialog();

    void editable();

    void showProgresbarDialog();

    void hideProgresbarDialog();

    void registerImageUser();
}
