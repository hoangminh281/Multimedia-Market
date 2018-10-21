package com.thm.hoangminh.multimediamarket.view.callback;

import android.net.Uri;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.view.callback.base.BundleBaseView;

import java.util.ArrayList;

public interface ProductDetailView extends BundleBaseView {
    void showProductDetail(ProductDetail productDetail);

    void showOwner(User user);

    void addUriToSliderLayout(Uri uri);

    void refreshSliderAdapter();

    void showSuccessRatingLayout();

    void showMessage(int resource);

    void showRatingFragment(ArrayList<ProductRating> ratingList);

    void showProgressbarDialog();

    void hideProgressbarDialog();

    void closeCheckoutDialog();

    void enableOrDisableProductCheckout(double balance, boolean b);

    void showProduct(Product value);

    void activeOrDeactiveBookmark(boolean b);

    void showOrHideRatingLayout(boolean b);

    void setVisibleItemMenu(int itemId, boolean b);

    void enableOrDisableDownloadButton(boolean b);
}
