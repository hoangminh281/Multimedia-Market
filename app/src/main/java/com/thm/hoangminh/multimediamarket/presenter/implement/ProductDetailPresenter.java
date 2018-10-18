package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.Product;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.ProductRating;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProductDetailPresenters.ProductDetailInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.ProductDetailListener;
import com.thm.hoangminh.multimediamarket.view.callback.ProductDetailView;

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

    public void LoadProductTransactionHistory(String cate_id, String product_id) {
        interactor.LoadProductTransactionHistory(cate_id, product_id);
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
    public void onLoadRatingSuccess(ArrayList<ProductRating> ratingList) {
        listener.showRatingFragment(ratingList);
    }

    public void addNewRating(String product_id, float rating_point, String rating_content) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        interactor.createNewRating(product_id, new ProductRating((int) rating_point, rating_content, dateFormatter.format(Calendar.getInstance().getTime())));
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

    public void CheckoutProduct(String cate_id, String product_id, String owner_id) {
        listener.showDialogProgressbar();
        interactor.CheckoutProductbyId(cate_id, product_id, owner_id);
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

    public void ActiveProduct(String id, int status) {
        interactor.ActiveProduct(id, status);
    }

    @Override
    public void onFindCurrentUserSuccess(int role, String user_id) {
        listener.onLoadCurrentUserSuccess(role, user_id);
    }

    @Override
    public void onDownloadProductSuccess() {
        listener.showMessage(R.string.info_downloadFileSuccess);
        listener.EnableInstall();
    }

    @Override
    public void onDownloadProductFailure() {
        listener.showMessage(R.string.info_downloadFireFailure);
        listener.EnableInstall();
    }

    @Override
    public void onLoadProductTransactionHistoryFailure() {
        listener.EnableButtonBuy();
    }

    @Override
    public void onLoadProductByIdSuccess(Product value) {
        listener.showProduct(value);
    }

    public void LoadCurrentUser() {
        interactor.FindCurrentUser();
    }

    public void downLoadProduct(Context context , String fileName) {
        interactor.downLoadProduct(context, fileName);
    }

    public void LoadProductById(String product_id) {
        interactor.LoadProductById(product_id);
    }
}
