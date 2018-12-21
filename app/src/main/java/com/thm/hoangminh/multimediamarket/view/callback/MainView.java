package com.thm.hoangminh.multimediamarket.view.callback;


import android.net.Uri;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.view.callback.base.BaseView;
import com.thm.hoangminh.multimediamarket.view.callback.base.BundleBaseView;

import java.util.ArrayList;
import java.util.Map;

public interface MainView extends BundleBaseView {
    void updateUserUI(User user);

    void setVisibleItemMenu(int itemId, boolean b);

    void loadUserAvatar(Uri uri);

    void showUserRole(String roleName);

    void showUserGenderImage(int imageId);

    void setNavigationItemSelectedListener();

    void initViewPager();

    void setupTabIcons();

    void setEvents();

    void setProductSuggestions(Map<String, String> suggestions);

    void refreshSectionData();
}
