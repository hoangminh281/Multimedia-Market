package com.thm.hoangminh.multimediamarket.view.callback;

import com.thm.hoangminh.multimediamarket.model.User;

import java.util.ArrayList;

public interface UserView {
    void showUsers(ArrayList<User> users);;

    void dissmissDialog();

    void showRoles(ArrayList<String> value);
}
