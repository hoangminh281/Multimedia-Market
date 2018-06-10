package com.thm.hoangminh.multimediamarket.views.GameDetailViews;

import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public interface GameDetailView {

    public void showGameDetail(GameDetail gameDetail);

    public void showOwner(User user);

    public void addLinkIntoData(String link);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();

    public void showRatingLayout();

    public void slideUpRatingLayout();

    public void showImageUser(String user_image_link);

    public void showMessage(String message);

    public void showRatingPoint(ArrayList<RatingContent> ratingList);
}
