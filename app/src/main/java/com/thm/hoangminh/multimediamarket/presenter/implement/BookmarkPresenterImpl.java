package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.presenter.BookmarkPresenter;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.BookmarkView;

public class BookmarkPresenterImpl implements BookmarkPresenter {
    private BookmarkView listener;
    private FirebaseUser currentUser;
    private UserRepository userRepository;
    private ValueEventListener eventListener;

    public BookmarkPresenterImpl(BookmarkView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void extractBundle(Context context, Bundle bundle) {
        if (bundle != null) {
            String optionKey = bundle.getString(Constants.BundleOptionKey);
            if (optionKey.equals(Constants.AdminOption)) {
                listener.setTitle(R.string.menu_product_admin);
                bindingCurrentUserRole(context);
                listener.setUpCategoriesTabLayout(optionKey);
            }
        } else {
            listener.setTitle(R.string.menu_bookmark);
            listener.setUpCategoriesTabLayout(Constants.BookmarkOption);
        }
    }

    public void bindingCurrentUserRole(final Context context) {
        eventListener = userRepository.findAndWatchRole(currentUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Validate.validateCurrentUserRole(context, dataSnapshot.getValue(int.class), new int[]{Constants.AdminRole});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void removeListener() {
        if (eventListener != null) {
            userRepository.removeFindAndWatchRoleListener(currentUser.getUid(), eventListener);
        }
    }
}
