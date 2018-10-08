package com.thm.hoangminh.multimediamarket.presenter.UserPresenters;

import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public interface UserListener {
    void onFindAllSuccess(ArrayList<User> users);

    void onFindRolesSuccess(ArrayList<String> value);

    void onUpdateRoleByIdSuccess();

    void onFindCurrentUserRoleSuccess(Integer value);
}
