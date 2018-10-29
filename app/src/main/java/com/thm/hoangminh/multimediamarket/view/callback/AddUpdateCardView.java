package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Card;

public interface AddUpdateCardView {
    void setTitle(int titleId);

    void showCard(Card card);

    void showMessage(int messageId);

    void backToScreen();

    void onBackScreenWithResult();

    void initCardUI();
}
