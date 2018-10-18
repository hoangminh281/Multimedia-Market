package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.RatingContent;
import com.thm.hoangminh.multimediamarket.model.User;

import java.util.ArrayList;

public interface ProductDetailListener {
    public void onLoadProductDetailByIdSuccess(ProductDetail productDetail);

    public void onMarkedContent(boolean b);

    public void onLoadProductTransactionHistorySuccess();

    public void onLoadOwnerSuccess(User user);

    public void onLoadProductImageLinkSuccess(String link);

    public void onUserRatingNotExist();

    public void onLoadUserImageLinkSuccess(String user_image_link);

    public void onAddNewRatingSuccess();

    public void onLoadRatingSuccess(ArrayList<RatingContent> ratingList);

    public void onLoadUserWalletSuccess(double balance);

    public void onCheckoutProductbyIdSuccess();

    public void onCheckoutProductbyIdFailure();

    public void onSavedProductBookmarkSuccess();

    public void onUnSavedProductBookmarkSuccess();

    void onFindCurrentUserSuccess(int role, String user_id);

    void onDownloadProductSuccess();

    void onDownloadProductFailure();

    void onLoadProductTransactionHistoryFailure();

    void onLoadProductByIdSuccess(Product value);
}
