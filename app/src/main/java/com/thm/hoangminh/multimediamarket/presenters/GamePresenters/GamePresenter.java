package com.thm.hoangminh.multimediamarket.presenters.GamePresenters;

import com.thm.hoangminh.multimediamarket.models.Game;
import com.thm.hoangminh.multimediamarket.views.GameViews.GameView;

import java.util.List;

public class GamePresenter implements GameListener {
    private GameView listener;
    private GameInteractor interactor;
    private int limit_count;
    private List<String> game_Id_List;
    private boolean request_deny;

    public GamePresenter(GameView listener) {
        this.listener = listener;
        this.interactor = new GameInteractor(this);
        this.limit_count = 15;
    }

    public void LoadGameBySectionPaging(String section_id) {
        interactor.LoadGameBySection(section_id);
    }

    public void LoadGameNexttoScroll() {
        if (!request_deny) {
            request_deny = true;
            if (game_Id_List != null)
                interactor.LoadGameBySectionPaging(game_Id_List, limit_count);
        }
    }

    @Override
    public void onLoadSectionSuccess(List<String> listGameId) {
        this.game_Id_List = listGameId;
        interactor.LoadGameBySectionPaging(game_Id_List, limit_count);
    }

    @Override
    public void onLoadGameBySectionSuccess(Game game) {
        listener.addGametoAdapter(game);
        listener.refreshAdapter();
        request_deny = false;
    }
}
