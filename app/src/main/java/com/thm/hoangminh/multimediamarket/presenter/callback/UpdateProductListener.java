package com.thm.hoangminh.multimediamarket.presenter.callback;

import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;

import java.util.Map;

public interface UpdateProductListener {

    void onLoadProductByIdSuccess(Product product);

    void onLoadProductDetailByIdSuccess(ProductDetail pDetail);

    void onLoadSectionByProductIdSuccess(Map<String, String> sections);

    void onLoadSectionByIdSuccess(Map<String, String> sections);

    void onUpdateProductImageSuccess();

    void onUpdateProductImageFailure(String e);

    void onUpdateProductDetailImageSuccess(int finalI);

    void onUpdateProductDetailImageFailure(int finalI, String e);

    void onUpdateProductSuccess();

    void onUpdateProductFailure(String e);

    void onUpdateProductDetailSuccess();

    void onDeleteProductSectionsSuccess();

    void onDeleteProductSectionsFailure(String e);

    void onUpdateProductSectionsSuccess();

    void onUpdateProductSectionsFailure(String e);

    void onUpdateFileFailure(String e);

    void onUploadFileSuccess();

}
