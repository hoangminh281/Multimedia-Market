package com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters;

import android.graphics.Bitmap;

import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;

import java.util.ArrayList;
import java.util.Map;

public interface UpdateProductInteractor {

    void LoadProductById(String id);

    void LoadProductDetailById(String id);

    void LoadSectionByProductId(String cate_id, String id);

    void LoadSectionById(String cate_id);

    void UpdateProductImage(String product_id, String image_old_id, Bitmap bitmap);

    void UpdateProductDetailImage(String product_id, Map<String, String> imageOldIds, Map<Integer, Bitmap> bitmaps);

    void UpdateProduct(Product product);

    void UpdateProductDetail(ProductDetail productDetail);

    void DeleteProductSections(String product_id, String cate_id, ArrayList<String> sectionOldIds);

    void UpdateProductSections(String product_id, String cate_id, ArrayList<String> sectionNewIds);

    void UpdateFile(String product_id,String oldFileId, File file);

}
