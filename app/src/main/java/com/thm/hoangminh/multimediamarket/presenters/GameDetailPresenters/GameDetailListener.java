package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public interface GameDetailListener {
    public void onLoadGameDetailByIdSuccess(GameDetail gameDetail);

    public void onLoadOwnerSuccess(User user);

    public void onLoadGameImageLinkSuccess(String link);

    public void onUserRatingNotExist();

    public void onLoadUserImageLinkSuccess(String user_image_link);

    public void onAddNewRatingSuccess();

    public void onLoadRatingSuccess(ArrayList<RatingContent> ratingList);
}
