package com.thm.hoangminh.multimediamarket.presenters.MainPresenters;

import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainView;

public class MainPresenter implements MainListener {
    private MainView listener;
    private MainInteractor interactor;

    public MainPresenter(MainView listener) {
        this.listener = listener;
        interactor = new MainInteractor(this);
    }

    public void LoadUserProfile() {
        interactor.LoadUserProfile();
    }

    @Override
    public void onLoadUserProfileSuccess(User user) {
        listener.updateUI(user);
        interactor.LoadUserImageLink(user.getImage());
    }

    @Override
    public void onLoadUserImageLinkSuccess(String image_link) {
        listener.updateUI(image_link);
    }
}
