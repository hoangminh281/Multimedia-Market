package com.thm.hoangminh.multimediamarket.view.MainViews;


import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;
import java.util.Map;

public interface MainView {

    public void updateUI(User user);

    public void showCategory(ArrayList<Category> categories);

    void showSuggestions(Map<String, String> suggestions);
}
