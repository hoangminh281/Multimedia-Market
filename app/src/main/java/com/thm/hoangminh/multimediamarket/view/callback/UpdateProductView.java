package com.thm.hoangminh.multimediamarket.view.UpdateProductViews;

import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;

import java.util.Map;

public interface UpdateProductView {
    void UpdateProductUI(Product product);

    void UpdateProductDetailUI(ProductDetail pDetail);

    void UpdateSectionProductUI(Map<String, String> sections);

    void UpdateSectionList(Map<String, String> sections);

    void UpdateProgressDialog(int message);

    void showMessage(int e);

}
