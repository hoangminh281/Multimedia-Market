package com.thm.hoangminh.multimediamarket.presenter;

import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.model.ProductRating;

public interface ProductDetailPresenter {
    void extractBundle(Bundle bundle);

    void activeOrDeactiveProduct(boolean b);

    void enableOrDisableBookmark(boolean b);

    void loadUserWallet();

    void checkoutProduct();

    void downLoadProduct();

    void addRating(ProductRating productRating);
}
