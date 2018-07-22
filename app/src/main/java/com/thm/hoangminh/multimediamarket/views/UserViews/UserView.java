package com.thm.hoangminh.multimediamarket.views.UserViews;

import com.thm.hoangminh.multimediamarket.models.User;

import java.util.ArrayList;

public interface UserView {

    void ShowUserList(ArrayList<User> users);;

    void dissmissDialog();

    void showRoles(ArrayList<String> value);

    void bindingUserRole(Integer value);
}
