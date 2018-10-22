package com.thm.hoangminh.multimediamarket.view.callback;

import android.net.Uri;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.Map;

public interface UpdateProductView {
    void hideVideoEditText();

    void updateProductUI(Product product);

    void loadProductImageByUri(Uri uri);

    void updateProductDetailUI(ProductDetail productDetail, String cateId);

    void updateProgressDialog(int message);

    void showMessage(int e);

    void showProductCateText(String productCate);

    void setEventSectionCategories(Map<String,String> sectionCategories);

    void setupProgressDialog(int pgdMax, String title, String pgdMessage);

    void showProgressDialog();

    void setTagImageView(int i, int statusTag);
}
