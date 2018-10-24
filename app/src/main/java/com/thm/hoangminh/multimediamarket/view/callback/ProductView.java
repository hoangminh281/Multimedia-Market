package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.Product;

public interface ProductView {
    void addProductIntoAdapter(Product product);

    void refreshAdapter();

    void setTitle(String title);

    void setTitle(int titleId);
}
