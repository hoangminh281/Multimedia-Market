package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import android.content.Context;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.GameDetailViews.GameDetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class GameDetailPresenter implements GameDetailListener {

    private GameDetailView listener;
    private GameDetailInteractor interactor;
    private Context context;

    public GameDetailPresenter(GameDetailView listener, Context context) {
        this.listener = listener;
        this.context = context;
        this.interactor = new GameDetailInteractor(this);
    }

    public void LoadGameDetailById(String game_id) {
        interactor.LoadGameDetailById(game_id);
    }

    public void LoadRating(String game_id) {
        interactor.LoadRating(game_id);
        interactor.IsExistUserRating(game_id);
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
    public void onLoadGameImageLinkSuccess(String link) {
        listener.addLinkIntoData(link);
        listener.refreshAdapter();
    }

    @Override
    public void onUserRatingNotExist() {
        interactor.LoadUserImageLink();
        listener.showRatingLayout();
    }

    @Override
    public void onLoadUserImageLinkSuccess(String user_image_link) {
        listener.showImageUser(user_image_link);
    }

    @Override
    public void onAddNewRatingSuccess() {
        listener.slideUpRatingLayout();
        listener.showMessage(context.getResources().getString(R.string.txt_ratingsuccess));
    }

    @Override
    public void onLoadRatingSuccess(ArrayList<RatingContent> ratingList) {
        listener.showRatingPoint(ratingList);
    }

    public void addNewRating(String game_id, float rating_point, String rating_content) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        interactor.createNewRating(game_id, new RatingContent((int) rating_point, rating_content, dateFormatter.format(Calendar.getInstance().getTime())));
    }
}
