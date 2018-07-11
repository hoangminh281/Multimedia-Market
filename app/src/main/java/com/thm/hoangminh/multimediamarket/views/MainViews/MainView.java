package com.thm.hoangminh.multimediamarket.views.MainViews;


import com.thm.hoangminh.multimediamarket.models.Category;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public interface MainView {

    public void updateUI(User user);

    public void showCategory(ArrayList<Category> categories);

}
