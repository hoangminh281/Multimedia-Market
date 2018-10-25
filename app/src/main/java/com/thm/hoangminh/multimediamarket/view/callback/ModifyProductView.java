package com.thm.hoangminh.multimediamarket.view.callback;

import java.util.Map;

public interface ModifyProductView {
    void hideEdtYoutube();

    void showMessage(int messageId);

    void updateProgress(String s);

    void showSectionList(Map<String,String> sections);
}
