package com.thm.hoangminh.multimediamarket.view.callback;


import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;

import java.util.ArrayList;
import java.util.Map;

public interface MainView {

    public void updateUI(User user);

    public void showCategory(ArrayList<Category> categories);

    void showSuggestions(Map<String, String> suggestions);
}
