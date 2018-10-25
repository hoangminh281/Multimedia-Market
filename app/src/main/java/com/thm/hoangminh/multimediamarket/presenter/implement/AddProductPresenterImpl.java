package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.File;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.Section;
import com.thm.hoangminh.multimediamarket.presenter.AddProductPresenter;
import com.thm.hoangminh.multimediamarket.presenter.ModifyProductPresenters.ModifyProductInteractor;
import com.thm.hoangminh.multimediamarket.references.Tools;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.callback.ModifyProductView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddProductPresenterImpl implements AddProductPresenter {
    private ModifyProductView listener;
    private ModifyProductInteractor interactor;
    private String cateId;
    private String photoId;
    private String fileName;
    private ArrayList<Bitmap> bitmaps;
    private ArrayList<String> photoNames;
    private Map<String, String> currentProductSections;
    private SectionRepository sectionRepository;

    public AddProductPresenterImpl(ModifyProductView listener) {
        this.listener = listener;
        sectionRepository = new SectionRepositoryImpl();
        interactor = new ModifyProductInteractor(this);
    }

    @Override
    public void extractBundle(Bundle bundle) {
        cateId = bundle.getString(Constants.CateIdKey);
        bindingCurrentUserRole();
        loadCategoryProduct(cateId);
    }

    private void bindingCurrentUserRole() {
        //if role == user then out
    }

    private void loadCategoryProduct(String cateId) {
        if (cateId.equals(Category.getInstance().get(2).getCateId()) || cateId.equals(Category.getInstance().get(4).getCateId())) {
            listener.hideEdtYoutube();
        }
        sectionRepository.findById(cateId, new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Map<String, String> sections = new HashMap<>();
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        Section section = item.getValue(Section.class);
                        sections.put(section.getSectionId(), section.getTitle());
                    }
                    listener.showSectionList(sections);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void addProduct(String title, String key_cate, ArrayList<Bitmap> bitmaps, double price, String intro, String desc, int age_limit, String video, File file, Map<String, String> sections) {
        photoId = Tools.createImageNameRandom();
        interactor.CreateNewProduct(title, key_cate, photoId, price, intro, desc, age_limit, video, file);
        this.bitmaps = bitmaps;
        this.currentProductSections = sections;
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
        interactor.UpdateSection(cateId, currentProductSections, id);
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
