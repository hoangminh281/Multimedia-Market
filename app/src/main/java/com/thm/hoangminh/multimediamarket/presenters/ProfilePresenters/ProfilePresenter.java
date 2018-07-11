package com.thm.hoangminh.multimediamarket.presenters.ProfilePresenters;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.ProfileViews.ProfileView;

public class ProfilePresenter implements ProfileListener {
    private ProfileView listener;
    private ProfileInteractor interactor;

    private final String PASSWORD_KEY = "password";

    public ProfilePresenter(ProfileView listener) {
        this.listener = listener;
        this.interactor = new ProfileInteractor(this);
    }

    public void LoadCurrentUserInformation() {
        interactor.LoadCurrentUserInformation();
    }

    public void LoadCurrentUserMultimedia() {
        interactor.LoadCurrentUserMultimedia();
    }

    public void CheckCurrentUserProvider() {
        interactor.CheckCurrentUserProvider();
    }

    @Override
    public void onLoadCurrentUserInformationSuccess(User user) {
        listener.ShowCurrentUserInformation(user);
    }

    @Override
    public void onLoadCurrentUserProductSuccess(int size) {
        listener.showGameNumber(size);
    }

    @Override
    public void onLoadCurrentUserImageSuccess(int size) {
        listener.showImageNumber(size);
    }

    @Override
    public void onLoadCurrentUserVideoSuccess(int size) {
        listener.showVideoNumber(size);
    }

    @Override
    public void onLoadCurrentUserMusicSuccess(int size) {
        listener.showMusicNumber(size);
    }

    @Override
    public void onCheckCurrentUserProviderSuccess(String st) {
        if (st.equals(PASSWORD_KEY)) {
            listener.EnableChangeCurrentUserEmail();
            listener.EnableChangeCurrentUserPassword();
        }
    }

    public void EditUsername(String username) {
        listener.showProgresbarDialog();
        interactor.EditUsername(username);
    }

    public void EditEmail(String email) {
        listener.showProgresbarDialog();
        interactor.EditEmail(email);
    }

    public void EditPassword(String pass) {
        listener.showProgresbarDialog();
        interactor.EditPassword(pass);
    }

    public void EditBirthday(int year, int month, int day) {
        listener.showProgresbarDialog();
        interactor.EditBirthday(year, month, day);
    }

    public void EditGender(int i) {
        listener.showProgresbarDialog();
        interactor.EditGender(i);
    }

    @Override
    public void onEditSuccess() {
        listener.showMessageFromResource(R.string.info_editSuccess);
        listener.dismissDialog();
    }

    @Override
    public void onExistError() {
        listener.showMessageFromResource(R.string.infor_failure);
        listener.hideProgresbarDialog();
    }
}
