package com.thm.hoangminh.multimediamarket.presenter;

import android.content.Context;
import android.os.Bundle;

public interface BookmarkPresenter {
    void extractBundle(Context context, Bundle bundle);

    void removeListener();
}
