package com.thm.hoangminh.multimediamarket.presenters.GameDetailPresenters;

import com.thm.hoangminh.multimediamarket.models.GameDetail;
import com.thm.hoangminh.multimediamarket.models.User;

public interface GameDetailListener {
    public void onLoadGameDetailByIdSuccess(GameDetail gameDetail);

    public void onLoadOwnerSuccess(User user);

    public void onLoadImageLinkSuccess(String link);
}
