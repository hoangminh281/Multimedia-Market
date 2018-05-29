package com.thm.hoangminh.multimediamarket.views.GameViews;

import com.thm.hoangminh.multimediamarket.models.Game;

public interface GameView {
    public void addGametoAdapter(Game game);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
