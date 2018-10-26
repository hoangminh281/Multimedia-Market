package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;

public interface UserPresenter {
    void initDataUsers(Context context);

    void activeOrDeactiveUser(String userId, boolean b);

    void updateUserRole(String userId, int roleId);

    void removeListerner();
}
