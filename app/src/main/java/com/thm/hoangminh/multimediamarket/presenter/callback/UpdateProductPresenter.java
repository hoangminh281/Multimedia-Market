package com.thm.hoangminh.multimediamarket.presenter.callback;

import android.graphics.Bitmap;

import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.ArrayList;
import java.util.Map;

public interface UpdateProductPresenter {
    void LoadProductById(String cate_id, String id);

    void LoadSectionById(String cate_id);

    void UpdateProduct(ArrayList<String> oldSections, ArrayList<String> newSections, Product product, ProductDetail pDetail, Bitmap newProductBitmap, Map<Integer, Bitmap> newProductDetailBitmaps, File file);

}
