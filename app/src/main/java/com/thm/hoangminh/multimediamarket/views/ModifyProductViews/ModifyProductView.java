package com.thm.hoangminh.multimediamarket.views.ModifyProductViews;

import java.util.Map;

public interface ModifyProductView {
    public void ShowCategory(Map<String, String> categoryList);

    public void hideEdtYoutube();

    public void showMessage(String message);

    void updateProgress(String s);
}
