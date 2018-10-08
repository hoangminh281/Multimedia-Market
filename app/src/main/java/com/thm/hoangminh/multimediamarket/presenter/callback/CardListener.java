package com.thm.hoangminh.multimediamarket.presenter.CardPresenters;

import com.thm.hoangminh.multimediamarket.models.Card;

import java.util.ArrayList;

public interface CardListener {
    void onLoadCardListSuccess(ArrayList<Card> cards);

    void onFindCurrentUserRoleSuccess(Integer value);
}
