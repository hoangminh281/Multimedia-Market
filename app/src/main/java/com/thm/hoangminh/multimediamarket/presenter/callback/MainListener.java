package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface MainListener {

    public void onLoadUserProfileSuccess(User user);

    public void onLoadCategorySuccess(ArrayList<Category> categories);

    void onLoadProductSuggestionsSuccess(Map<String, String> suggestions);
}
