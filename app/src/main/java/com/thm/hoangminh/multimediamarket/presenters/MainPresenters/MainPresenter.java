package com.thm.hoangminh.multimediamarket.presenters.MainPresenters;

import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainView;

import java.util.ArrayList;
import java.util.Map;

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

    public void LoadCategory() {
        interactor.LoadCategory();
    }

    @Override
    public void onLoadUserProfileSuccess(User user) {
        User.setInstance(user);
        listener.updateUI(user);
    }

    @Override
    public void onLoadCategorySuccess(ArrayList<Category> categories) {
        listener.showCategory(categories);
    }

    @Override
    public void onLoadProductSuggestionsSuccess(Map<String, String> suggestions) {
        listener.showSuggestions(suggestions);
    }

    public void LoadProductSuggestions() {
        interactor.LoadProductSuggestions();
    }
}
