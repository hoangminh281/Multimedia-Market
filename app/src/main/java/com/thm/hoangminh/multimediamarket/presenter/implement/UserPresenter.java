package com.thm.hoangminh.multimediamarket.presenter.implement;

import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.UserPresenters.UserInteractor;
import com.thm.hoangminh.multimediamarket.presenter.callback.UserListener;
import com.thm.hoangminh.multimediamarket.view.callback.UserView;

import java.util.ArrayList;

public class UserPresenter implements UserListener {
    private UserView listener;
    private UserInteractor interactor;

    public UserPresenter(UserView listener) {
        this.listener = listener;
        this.interactor = new UserInteractor(this);
    }

    public void findAll() {
        interactor.findAll();
    }

    public void activeUser(String user_id, int i) {
        interactor.active(user_id, i);
    }

    @Override
    public void onFindAllSuccess(ArrayList<User> users) {
        listener.ShowUserList(users);
    }

    @Override
    public void onFindRolesSuccess(ArrayList<String> value) {
        listener.showRoles(value);
    }

    @Override
    public void onUpdateRoleByIdSuccess() {
        listener.dissmissDialog();
    }

    @Override
    public void onFindCurrentUserRoleSuccess(Integer value) {
        listener.bindingUserRole(value);
    }


    public void findRoles() {
        interactor.findRoles();
    }

    public void setRoles(String user_id, int role) {
        interactor.updateRoleById(user_id, role);
    }

    public void findCurrentUserRole() {
        interactor.findCurrentUserRole();
    }

    public void RemoveListener() {
        interactor.RemoveListener();
    }
}
