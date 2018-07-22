package com.thm.hoangminh.multimediamarket.presenters.MainPresenters;

import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;
import java.util.Map;

public interface MainListener {

    public void onLoadUserProfileSuccess(User user);

    public void onLoadCategorySuccess(ArrayList<Category> categories);

    void onLoadProductSuggestionsSuccess(Map<String, String> suggestions);
}
