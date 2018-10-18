package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.graphics.Bitmap;

import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.presenter.ModifyProductPresenters.ModifyProductInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.ModifyProductListener;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;
import com.thm.hoangminh.multimediamarket.view.callback.ModifyProductView;

import java.util.ArrayList;
import java.util.Map;

public class ModifyProductPresenter implements ModifyProductListener {
    private ModifyProductView listener;
    private ModifyProductInteractor interactor;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> photoNames;
    private String photoId;
    private String fileName;
    private String cate_id;
    private Map<String, String> sections;

    public ModifyProductPresenter(ModifyProductView listener) {
        this.listener = listener;
        interactor = new ModifyProductInteractor(this);
    }

    public void LoadCategoryProduct(String key_category) {
        cate_id = key_category;
        interactor.LoadCategoryProduct(cate_id);
        if (key_category.equals(MainActivity.categories.get(1).getCate_id()) || key_category.equals(MainActivity.categories.get(3).getCate_id())) {
            listener.hideEdtYoutube();
        }
    }

    @Override
    public void LoadCategoryProductSuccess(Map<String, String> categoryList) {
        listener.ShowCategory(categoryList);
    }

    public void CreateNewProduct(String title, String key_cate, ArrayList<Bitmap> bitmaps, double price, String intro, String desc, int age_limit, String video, File file, Map<String, String> sections) {
        photoId = Tools.createImageNameRandom();
        interactor.CreateNewProduct(title, key_cate, photoId, price, intro, desc, age_limit, video, file);
        this.bitmaps = bitmaps;
        this.sections = sections;
    }

    @Override
    public void onCreateNewProductSuccess(String id, String intro, String description, File file, int ageLimit, String owner_id, String video) {
        listener.updateProgress("Created successfully product");
        fileName = new String(file.getName());
        String downloadLink = Tools.createFileNameRandom(file.getName());
        file.setName(downloadLink);
        photoNames = new ArrayList<>();
        photoNames.add(photoId);
        for (int i = 1; i < bitmaps.size(); i++) {
            String photoId = Tools.createImageNameRandom();
            while (photoNames.contains(photoId))
                photoId = Tools.createImageNameRandom();
            photoNames.add(photoId);
        }
        interactor.CreateProductDetailById(new ProductDetail(id, intro, description, (int) file.getSize().getValue(), 0, ageLimit, owner_id, video, downloadLink), photoNames);
        interactor.UploadFile(file);
        interactor.UpdateSection(cate_id, sections, id);
    }

    @Override
    public void onCreateNewProductFailure() {
        listener.showMessage("Created failure product");
    }

    @Override
    public void onUploadPhotoSucceed(int photoIndex) {
        listener.updateProgress("Created successfully product detail");
        interactor.UploadPhoto(photoIndex, photoNames.get(photoIndex), bitmaps.get(photoIndex));
    }

    @Override
    public void onUploadPhotoSuccess(int photoIndex) {
        listener.updateProgress("Uploaded successfully image " + (photoIndex + 1));
    }

    @Override
    public void onUploadPhotoFailure(int photoIndex) {
        listener.showMessage("Upload failure image " + (photoIndex + 1));
    }

    @Override
    public void onUploadFileSuccess() {
        listener.updateProgress("Uploaded successfully file " + fileName);
    }

    @Override
    public void onUpdateSectionSuccess(String section) {
        listener.updateProgress("Updated successfully section " + section);
    }
}
