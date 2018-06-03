package com.thm.hoangminh.multimediamarket.views.MainViews;


import com.thm.hoangminh.multimediamarket.models.User;

public interface MainView {

    public void updateUI(User user);

    public void updateUI(String image_link);
}
