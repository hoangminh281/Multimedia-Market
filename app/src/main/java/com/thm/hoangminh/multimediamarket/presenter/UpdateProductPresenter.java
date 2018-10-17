package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.view.activity.UpdateProductActivity;

import java.util.ArrayList;
import java.util.Map;

public interface UpdateProductPresenter {
    void extractBundle(Bundle bundle);

    void updateProduct(ArrayList<Integer> selectedProductSections, Product product, ProductDetail productDetail,
                       Bitmap productImage, Map<Integer, Bitmap> productDetailBitmaps, File updatedFile);

    void loadProductDetailImages(Context context, ImageView[] imgArr);
}
