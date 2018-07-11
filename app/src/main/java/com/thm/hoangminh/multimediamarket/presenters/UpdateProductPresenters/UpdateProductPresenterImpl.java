package com.thm.hoangminh.multimediamarket.presenters.UpdateProductPresenters;

import android.graphics.Bitmap;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.File;
import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.views.UpdateProductViews.UpdateProductView;

import java.util.ArrayList;
import java.util.Map;

public class UpdateProductPresenterImpl implements UpdateProductPresenter, UpdateProductListener {
    private UpdateProductView listener;
    private UpdateProductInteractor interactor;
    private ProductDetail pDetail;
    private Map<Integer, Bitmap> newProductDetailBitmaps;
    private Bitmap newProductBitmap;
    private ArrayList<String> newSections;
    private Product product;
    private File file;

    public UpdateProductPresenterImpl(UpdateProductView listener) {
        this.listener = listener;
        interactor = new UpdateProductInteractorImpl(this);
    }

    @Override
    public void LoadProductById(String cate_id, String id) {
        interactor.LoadProductById(id);
        interactor.LoadProductDetailById(id);
        interactor.LoadSectionByProductId(cate_id, id);
    }

    @Override
    public void LoadSectionById(String cate_id) {
        interactor.LoadSectionById(cate_id);
    }

    @Override
    public void onLoadProductByIdSuccess(Product product) {
        listener.UpdateProductUI(product);
    }

    @Override
    public void onLoadProductDetailByIdSuccess(ProductDetail pDetail) {
        listener.UpdateProductDetailUI(pDetail);
    }

    @Override
    public void onLoadSectionByProductIdSuccess(Map<String, String> sections) {
        listener.UpdateSectionProductUI(sections);
    }

    @Override
    public void onLoadSectionByIdSuccess(Map<String, String> sections) {
        listener.UpdateSectionList(sections);
    }

    @Override
    public void UpdateProduct(ArrayList<String> oldSections, ArrayList<String> newSections, Product product, ProductDetail pDetail, Bitmap newProductBitmap, Map<Integer, Bitmap> newProductDetailBitmaps, File file) {
        this.newSections = newSections;
        this.product = product;
        this.pDetail = pDetail;
        this.newProductBitmap = newProductBitmap;
        this.newProductDetailBitmaps = newProductDetailBitmaps;
        this.file = file;
        if (newSections != null)
            interactor.DeleteProductSections(product.getProduct_id(), product.getCate_id(), oldSections);
        else
            interactor.UpdateProduct(product);
    }

    @Override
    public void onDeleteProductSectionsSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_delete_section);
        interactor.UpdateProductSections(product.getProduct_id(), product.getCate_id(), newSections);
    }

    @Override
    public void onUpdateProductSectionsSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_update_section);
        interactor.UpdateProduct(product);
    }

    @Override
    public void onUpdateProductSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_update_product);
        interactor.UpdateProductDetail(pDetail);
    }

    @Override
    public void onUpdateProductDetailSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_update_product_detail);
        if (newProductBitmap != null)
            interactor.UpdateProductImage(product.getProduct_id(), product.getPhotoId(), newProductBitmap);
        if (newProductDetailBitmaps.size() != 0)
            interactor.UpdateProductDetailImage(product.getProduct_id(), pDetail.getImageList(), newProductDetailBitmaps);
        if (file != null)
            interactor.UpdateFile(product.getProduct_id(), pDetail.getDownloadLink(), file);
    }

    @Override
    public void onUpdateProductImageSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_update_image_product);
    }

    @Override
    public void onUpdateProductDetailImageSuccess(int finalI) {
        listener.UpdateProgressDialog(R.string.dialog_successfully_update_image_product_detail);
    }

    @Override
    public void onUploadFileSuccess() {
        listener.UpdateProgressDialog(R.string.dialog_successfully_upload_file);
    }

    @Override
    public void onUpdateProductImageFailure(String e) {
        listener.showMessage(R.string.dialog_failure_update_image_product);
    }

    @Override
    public void onUpdateProductDetailImageFailure(int finalI, String e) {
        listener.showMessage(R.string.dialog_failure_update_image_product_detail);
    }

    @Override
    public void onUpdateProductFailure(String e) {
        listener.showMessage(R.string.dialog_failure_update_product);
    }

    @Override
    public void onDeleteProductSectionsFailure(String e) {
        listener.showMessage(R.string.dialog_failure_delete_section_product);
    }

    @Override
    public void onUpdateProductSectionsFailure(String e) {
        listener.showMessage(R.string.dialog_failure_update_section_product);
    }

    @Override
    public void onUpdateFileFailure(String e) {
        listener.showMessage(R.string.dialog_failure_delete_file);
    }

}
