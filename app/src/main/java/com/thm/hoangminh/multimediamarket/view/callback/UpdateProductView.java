package com.thm.hoangminh.multimediamarket.view.UpdateProductViews;

import android.net.Uri;

import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;

import java.util.Map;

public interface UpdateProductView {
    void UpdateProductUI(Product product);

    void UpdateProductDetailUI(ProductDetail pDetail, String cateId);

    void UpdateSectionProductUI(Map<String, String> sections);

    void UpdateSectionList(Map<String, String> sections);

    void UpdateProgressDialog(int message);

    void showMessage(int e);

    void showProductCateText(String productCate);

    void showProgressDialog();

    void loadProductImageByUri(Uri uri);

    void setTagImageView(int i, int hasOrNotHasImageTag);
}
