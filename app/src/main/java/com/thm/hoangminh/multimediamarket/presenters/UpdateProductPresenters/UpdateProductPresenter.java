package com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters;

import android.graphics.Bitmap;

import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;

import java.util.ArrayList;
import java.util.Map;

public interface UpdateProductPresenter {
    void LoadProductById(String cate_id, String id);

    void LoadSectionById(String cate_id);

    void UpdateProduct(ArrayList<String> oldSections, ArrayList<String> newSections, Product product, ProductDetail pDetail, Bitmap newProductBitmap, Map<Integer, Bitmap> newProductDetailBitmaps, File file);

}
