package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Product;

import java.util.List;

public interface ProductListener {

    public void onLoadProductIdListSuccess(List<String> product_Id_List);

    public void onLoadProductPagingSuccess(Product product);
}
