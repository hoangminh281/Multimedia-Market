package com.thm.hoangminh.multimediamarket.presenters.GamePresenters;

import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.models.Section;

import java.util.HashMap;
import java.util.List;

public interface GameListener {

    public void onLoadSectionSuccess(List<String> listGameId);

    public void onLoadGameBySectionSuccess(Game game);
}
