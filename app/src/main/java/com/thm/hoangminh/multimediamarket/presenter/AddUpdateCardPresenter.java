package com.thm.hoangminh.multimediamarket.presenter;

import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.model.Card;

public interface AddUpdateCardPresenter {
    void extractBundle(Bundle bundle);

    void addOrUpdateCard(Card card);
}
