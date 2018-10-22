package com.thm.hoangminh.multimediamarket.presenter.callback;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.ArrayList;
import java.util.Map;

public interface UpdateProductPresenter {
    void extractBundle(Bundle bundle);

    void updateProduct(ArrayList<Integer> selectedProductSections, Product product, ProductDetail productDetail,
                       Bitmap productImage, Map<Integer, Bitmap> productDetailBitmaps, File updatedFile);

    void loadProductDetailImages(Context context, ImageView[] imgArr);
}
