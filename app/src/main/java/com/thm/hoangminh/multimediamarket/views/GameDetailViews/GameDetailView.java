package com.thm.hoangminh.multimediamarket.views.GameDetailViews;

import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;

public interface GameDetailView {

    public void showGameDetail(GameDetail gameDetail);

    public void showOwner(User user);

    public void addLinkIntoData(String link);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
