package com.thm.hoangminh.multimediamarket.view.callback.base;

import android.os.Bundle;

public interface BundleBaseView extends BaseView {
    void startActivity(Class<?> clazz, Bundle bundle);
}
