package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.ArrayList;

public interface AddProductPresenter {
    void extractBundle(Context context, Bundle bundle);

    void addProduct(Product product, ProductDetail productDetail, ArrayList<Integer> selectedSections, ArrayList<Bitmap> selectedBitmaps, File pickedFile);

    void removeEventListener();
}
