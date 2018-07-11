package com.thm.hoangminh.multimediamarket.presenters.ProductPresenters;

import com.thm.hoangminh.multimediamarket.models.Product;
import com.thm.hoangminh.multimediamarket.views.ProductViews.ProductView;

import java.util.ArrayList;
import java.util.List;

public class ProductPresenter implements ProductListener {
    private ProductView listener;
    private ProductInteractor interactor;
    private int limit_count;
    private List<String> product_Id_List;
    private boolean request_deny;

    public ProductPresenter(ProductView listener) {
        this.listener = listener;
        this.interactor = new ProductInteractor(this);
        this.limit_count = 15;
    }

    public void LoadProductBySectionPaging(String cate_id, String section_id) {
        interactor.LoadProductBySection(cate_id, section_id);
    }

    public void LoadProductByUserPaging(String user_id) {
        interactor.LoadProductByUser(user_id);
    }

    public void LoadProductByBookmarkCateIdPaging(String bookmark_cate_id) {
        interactor.LoadProductByBookmarkCateIdPaging(bookmark_cate_id);
    }

    public void LoadProductNexttoScroll() {
        if (!request_deny) {
            request_deny = true;
            if (product_Id_List != null)
                interactor.LoadProductPaging(product_Id_List, limit_count);
        }
    }

    @Override
    public void onLoadProductIdListSuccess(List<String> product_Id_List) {
        this.product_Id_List = product_Id_List;
        interactor.LoadProductPaging(product_Id_List, limit_count);
    }

    @Override
    public void onLoadProductPagingSuccess(Product product) {
        listener.addProducttoAdapter(product);
        listener.refreshAdapter();
        request_deny = false;
    }
}
