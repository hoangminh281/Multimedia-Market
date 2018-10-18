package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.RatingContent;
import com.thm.hoangminh.multimediamarket.model.User;

import java.util.ArrayList;

public interface ProductDetailView {

    public void showProductDetail(ProductDetail productDetail);

    public void showOwner(User user);

    public void checkBookmark(boolean b);

    public void addLinkIntoSlider(String link);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();

    public void showRatingLayout();

    public void showRatingSuccessLayout();

    public void showImageUser(String user_image_link);

    public void showMessage(String message);

    public void showMessageFromResource(int resource);

    public void showRatingFragment(ArrayList<RatingContent> ratingList);

    public void showDialogProgressbar();

    public void hideDialogProgressbar();

    public void closeDialogProgressbar();

    public void AllowCheckout(double balance);

    public void NotAllowCheckout(double balance);

    public void EnableInstall();

    void onLoadCurrentUserSuccess(int role, String user_id);

    void showMessage(int messageId);

    void EnableButtonBuy();

    void showProduct(Product value);

}
