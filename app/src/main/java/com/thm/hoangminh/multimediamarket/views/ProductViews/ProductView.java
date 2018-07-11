package com.thm.hoangminh.multimediamarket.views.ProductViews;

import com.thm.hoangminh.multimediamarket.models.Product;

public interface ProductView {
    public void addProducttoAdapter(Product product);

    public void refreshAdapter();

    public void showBottomProgressbar();

    public void hideBottomProgressbar();
}
