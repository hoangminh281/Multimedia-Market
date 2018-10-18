package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Card;

import java.util.ArrayList;

public interface CardView {
    void showCardList(ArrayList<Card> cards);

    void bindingUserRole(Integer role_id);
}
