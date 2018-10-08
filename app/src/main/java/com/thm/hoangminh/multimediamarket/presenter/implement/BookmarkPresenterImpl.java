package com.thm.hoangminh.multimediamarket.presenter.service.implement;

import com.thm.hoangminh.multimediamarket.presenter.BookmarkPresenters.BookmarkInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.BookmarkListener;
import com.thm.hoangminh.multimediamarket.view.callback.BookmarkView;

public class BookmarkPresenterImpl implements BookmarkListener {
    private BookmarkInteractor interactor;
    private BookmarkView listener;

    public BookmarkPresenterImpl(BookmarkView listener) {
        interactor = new BookmarkInteractor(this);
        this.listener = listener;
    }

    public void findCurrentUserRole() {
        interactor.findCurrentUserRole();
    }

    @Override
    public void onFindCurrentUserRoleSuccess(Integer value) {
        listener.bindingUserRole(value);
    }

    public void RemoveListener() {
        interactor.RemoveListener();
    }
}
