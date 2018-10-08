package com.thm.hoangminh.multimediamarket.viewImpl.CardViews;

import com.thm.hoangminh.multimediamarket.models.Card;

import java.util.ArrayList;

public interface CardView {
    void showCardList(ArrayList<Card> cards);

    void bindingUserRole(Integer role_id);
}
