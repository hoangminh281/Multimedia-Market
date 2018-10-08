package com.thm.hoangminh.multimediamarket.presenter.ProfilePresenters;

import com.thm.hoangminh.multimediamarket.models.User;

public interface ProfileListener {

    public void onLoadCurrentUserInformationSuccess(User user);

    public void onLoadCurrentUserProductSuccess(int size);

    public void onLoadCurrentUserImageSuccess(int size);

    public void onLoadCurrentUserVideoSuccess(int size);

    public void onLoadCurrentUserMusicSuccess(int size);

    public void onCheckCurrentUserProviderSuccess(String st);

    public void onEditSuccess();

    public void onExistError();

}
