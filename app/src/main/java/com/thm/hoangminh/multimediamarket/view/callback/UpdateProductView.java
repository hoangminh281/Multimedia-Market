package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.Map;

public interface UpdateProductView {
    void UpdateProductUI(Product product);

    void UpdateProductDetailUI(ProductDetail pDetail);

    void UpdateSectionProductUI(Map<String, String> sections);

    void UpdateSectionList(Map<String, String> sections);

    void UpdateProgressDialog(int message);

    void showMessage(int e);

}
