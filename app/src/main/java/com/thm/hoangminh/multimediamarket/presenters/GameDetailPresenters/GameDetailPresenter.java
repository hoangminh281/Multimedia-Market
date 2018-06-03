package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.GameDetailViews.GameDetailView;

import java.util.ArrayList;

public class GameDetailPresenter implements GameDetailListener {

    private GameDetailView listener;
    private GameDetailInteractor interactor;

    public GameDetailPresenter(GameDetailView listener) {
        this.listener = listener;
        this.interactor = new GameDetailInteractor(this);
    }

    public void LoadGameDetailById(String game_id) {
        interactor.LoadGameDetailById(game_id);
    }

    @Override
    public void onLoadGameDetailByIdSuccess(GameDetail gameDetail) {
        listener.showGameDetail(gameDetail);
        interactor.LoadOwnerById(gameDetail.getOwner_id());
        if (gameDetail.getImageList() != null) {
            interactor.getImageLinkDownload(gameDetail.getId(), new ArrayList<>(gameDetail.getImageList().values()));
        }
    }

    @Override
    public void onLoadOwnerSuccess(User user) {
        listener.showOwner(user);
    }

    @Override
    public void onLoadImageLinkSuccess(String link) {
        listener.addLinkIntoData(link);
        listener.refreshAdapter();
    }
}
