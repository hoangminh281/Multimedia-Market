package com.thm.hoangminh.multimediamarket.presenters.ModifyProductPresenters;

import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;

import java.util.Map;

public interface ModifyProductListener {
     void LoadCategoryProductSuccess(Map<String, String> categoryList);

     void onCreateNewProductSuccess(String id, String intro, String description, File file, int ageLimit, String owner_id, String video);

     void onCreateNewProductFailure();

     void onUploadPhotoSucceed(int photoIndex);

     void onUploadPhotoSuccess(int photoIndex);

     void onUploadPhotoFailure(int photoIndex);

     void onUploadFileSuccess();

    void onUpdateSectionSuccess(String section);

}
