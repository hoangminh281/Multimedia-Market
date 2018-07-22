package com.thm.hoangminh.multimediamarket.presenters.BookmarkPresenters;

import com.thm.hoangminh.multimediamarket.views.BookmarkViews.BookmarkView;

public class BookmarkPresenter implements BookmarkListener {
    private BookmarkInteractor interactor;
    private BookmarkView listener;

    public BookmarkPresenter(BookmarkView listener) {
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
