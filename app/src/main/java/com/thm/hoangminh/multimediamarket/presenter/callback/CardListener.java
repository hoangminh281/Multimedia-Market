package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Card;

import java.util.ArrayList;

public interface CardListener {
    void onLoadCardListSuccess(ArrayList<Card> cards);

    void onFindCurrentUserRoleSuccess(Integer value);
}
