package com.thm.hoangminh.multimediamarket.views.ProfileViews;

import com.thm.hoangminh.multimediamarket.models.User;

public interface ProfileView {

    public void ShowCurrentUserInformation(User user);

    public void showGameNumber(int size);

    public void showImageNumber(int size);

    public void showVideoNumber(int size);

    public void showMusicNumber(int size);

    public void EnableChangeCurrentUserEmail();

    public void EnableChangeCurrentUserPassword();

    public void showMessage(String message);

    public void showMessageFromResource(int resource);

    public void dismissDialog();

    public void showProgresbarDialog();

    public void hideProgresbarDialog();

}
