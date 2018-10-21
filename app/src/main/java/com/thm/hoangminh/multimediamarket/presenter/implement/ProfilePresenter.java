package com.thm.hoangminh.multimediamarket.presenter.implement;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProfilePresenters.ProfileInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.ProfileListener;
import com.thm.hoangminh.multimediamarket.view.callback.ProfileView;

public class ProfilePresenter implements ProfileListener {
    private ProfileView listener;
    private ProfileInteractor interactor;

    private final String PASSWORD_KEY = "password";

    public ProfilePresenter(ProfileView listener, String user_id) {
        this.listener = listener;
        this.interactor = new ProfileInteractor(this, user_id);
    }

    public void LoadCurrentUserInformation() {
        interactor.LoadCurrentUserInformation();
        interactor.LoadCurrentUserMultimedia();
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

    public void setBalance(double balance) {
        interactor.updateBalanceByUserId(balance);
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

        interactor.EditGender(i);
    }

    @Override
    public void onEditSuccess() {
        listener.showMessageFromResource(R.string.info_editSuccess);
        listener.dismissDialog();
        listener.editable();
    }

    @Override
    public void onExistError() {
        listener.showMessageFromResource(R.string.info_failure);
        listener.hideProgresbarDialog();
    }

}
