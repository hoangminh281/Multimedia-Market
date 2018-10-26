package com.thm.hoangminh.multimediamarket.view.callback;

import java.util.Map;

public interface ModifyProductView {
    void hideEdtYoutube();

    void showMessage(int messageId);

    void updateProgressMessage(String message);

    void showSectionList(Map<String,String> sections);

    void setCateId(String cateId);
}
