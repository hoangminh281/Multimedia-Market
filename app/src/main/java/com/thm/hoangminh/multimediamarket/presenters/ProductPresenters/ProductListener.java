package com.thm.hoangminh.multimediamarket.presenters.ProductPresenters;

import com.thm.hoangminh.multimediamarket.models.Product;

import java.util.ArrayList;
import java.util.List;

public interface ProductListener {

    public void onLoadProductIdListSuccess(List<String> product_Id_List);

    public void onLoadProductPagingSuccess(Product product);
}
