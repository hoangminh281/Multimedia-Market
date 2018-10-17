package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;

import com.thm.hoangminh.multimediamarket.models.Product;

public interface ProductDetailPresenter {
    void extractBundle(Bundle bundle);

    void enableOrDisableProduct(String productId, boolean b);

    void checkoutProduct(String cateId, String productId, String ownerId);

    void downLoadProduct(Context context, String fileName);

    void setupCheckoutDialog(double price);

    void addNewRating(String productId, float rating, String s);

    void bookmarkOrUnBookmarkProduct(String cateId, String productId, boolean b);

    void loadImageProduct(Context context, ImageView imgLogo, Product product);
}
