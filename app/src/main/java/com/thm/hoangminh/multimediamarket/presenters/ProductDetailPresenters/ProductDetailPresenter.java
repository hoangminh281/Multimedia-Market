package com.thm.hoangminh.multimediamarket.presenters.ProductDetailPresenters;

import android.content.Context;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.models.ProductDetail;
import com.thm.hoangminh.multimediamarket.models.RatingContent;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.ProductDetailViews.ProductDetailView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ProductDetailPresenter implements ProductDetailListener {

    private ProductDetailView listener;
    private ProductDetailInteractor interactor;
    private double price;

    public ProductDetailPresenter(ProductDetailView listener) {
        this.listener = listener;
        this.interactor = new ProductDetailInteractor(this);
    }

    public void LoadProductDetailById(String product_id) {
        interactor.LoadProductDetailById(product_id);
    }

    public void LoadBookmarkContent(String cate_id, String product_id) {
        interactor.LoadBookmarkContent(cate_id, product_id);
    }

    public void LoadProductTransactionHistory(String product_id) {
        interactor.LoadProductTransactionHistory(product_id);
    }

    @Override
    public void onLoadProductTransactionHistorySuccess() {
        listener.EnableInstall();

    }

    public void LoadRating(String product_id) {
        interactor.LoadRating(product_id);
        interactor.IsExistUserRating(product_id);
    }

    @Override
    public void onLoadProductDetailByIdSuccess(ProductDetail productDetail) {
        listener.showProductDetail(productDetail);
        interactor.LoadOwnerById(productDetail.getOwner_id());
        if (productDetail.getImageList() != null) {
            interactor.getImageLinkDownload(new ArrayList<>(productDetail.getImageList().values()));
        }
    }

    @Override
    public void onMarkedContent(boolean b) {
        listener.checkBookmark(b);
    }

    @Override
    public void onLoadOwnerSuccess(User user) {
        listener.showOwner(user);
    }

    @Override
    public void onLoadProductImageLinkSuccess(String link) {
        listener.addLinkIntoSlider(link);
        listener.refreshAdapter();
    }

    @Override
    public void onUserRatingNotExist() {
        interactor.LoadUserImageLink();
        listener.showRatingLayout();
    }

    @Override
    public void onLoadUserImageLinkSuccess(String user_image_link) {
        listener.showImageUser(user_image_link);
    }

    @Override
    public void onAddNewRatingSuccess() {
        listener.showRatingSuccessLayout();
    }

    @Override
    public void onLoadRatingSuccess(ArrayList<RatingContent> ratingList) {
        listener.showRatingFragment(ratingList);
    }

    public void addNewRating(String product_id, float rating_point, String rating_content) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        interactor.createNewRating(product_id, new RatingContent((int) rating_point, rating_content, dateFormatter.format(Calendar.getInstance().getTime())));
    }

    public void LoadUserWallet(double price) {
        this.price = price;
        listener.showDialogProgressbar();
        interactor.LoadUserWallet();
    }

    @Override
    public void onLoadUserWalletSuccess(double balance) {
        if (balance >= price) {
            listener.AllowCheckout(balance);
        } else if (balance < price) {
            listener.NotAllowCheckout(balance);
        }
        listener.hideDialogProgressbar();
    }

    public void CheckoutProduct(String product_id) {
        listener.showDialogProgressbar();
        interactor.CheckoutProductbyId(product_id);
    }

    @Override
    public void onCheckoutProductbyIdSuccess() {
        listener.showMessageFromResource(R.string.info_buyingSuccess);
        listener.closeDialogProgressbar();
    }

    @Override
    public void onCheckoutProductbyIdFailure() {
        listener.showMessageFromResource(R.string.infor_failure);
        listener.hideDialogProgressbar();
    }

    public void SavedProductBookmark(String cate_id, String product_id) {
        interactor.SavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onSavedProductBookmarkSuccess() {
        listener.showMessageFromResource(R.string.info_saved);
    }

    public void UnSavedProductBookmark(String cate_id, String product_id) {
        interactor.UnSavedProductBookmark(cate_id, product_id);
    }

    @Override
    public void onUnSavedProductBookmarkSuccess() {
        listener.showMessageFromResource(R.string.info_unSaved);
    }

    public void DeleteProduct(String id) {
        interactor.DeleteProduct(id);
    }

    @Override
    public void onDeleteProductSuccess() {
        listener.showMessageFromResource(R.string.dialog_successfully_delete_product);
    }

    @Override
    public void onDeleteProductFailure(Exception e) {
        listener.showMessageFromResource(R.string.dialog_failure_delete_product);
    }

}
