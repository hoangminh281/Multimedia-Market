package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;

import com.thm.hoangminh.multimediamarket.model.Card;

public interface CardPresenter {
    void loadCardList();

    void bidingCurrentUser(Context context);

    void activeOrDeactiveCard(Card card, int status);

    void removeListener();
}
