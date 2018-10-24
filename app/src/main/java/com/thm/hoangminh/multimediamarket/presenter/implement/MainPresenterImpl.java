package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.content.Context;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.fomular.MoneyFormular;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.MainPresenter;
import com.thm.hoangminh.multimediamarket.presenter.MainPresenters.MainInteractor;
import com.thm.hoangminh.multimediamarket.repository.CategoryRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CategoryRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RoleRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;
import com.thm.hoangminh.multimediamarket.view.callback.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainPresenterImpl implements MainPresenter {
    private final Context context;
    private MainView listener;
    private UserRepository userRepository;
    private UserStorageRepository userStorageRepository;
    private RoleRepository roleRepository;
    private CategoryRepository categoryRepository;
    private ProductRepository productRepository;

    public MainPresenterImpl(MainView listener, Context context) {
        this.context = context;
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        roleRepository = new RoleRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        categoryRepository = new CategoryRepositoryImpl();
        userStorageRepository = new UserStorageRepositoryImpl();
    }

    @Override
    public void loadUserProfile() {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userRepository.findAndWatchById(firebaseUser.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User currentUser = dataSnapshot.getValue(User.class);
                    listener.updateUserUI(currentUser);
                    User.setInstance(currentUser);
                    if (Validate.validateCurrentUserStatus(context, currentUser.getStatus())) {
                        //setup menu with Role and rule
                        switch (currentUser.getRole()) {
                            case User.ADMIN:
                                listener.setVisibleItemMenu(R.id.menu_user_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_card_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_product_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_upload, true);
                                break;
                            case User.MOD:
                                listener.setVisibleItemMenu(R.id.menu_user_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_card_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_product_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_upload, true);
                                break;
                            case User.USER:
                                listener.setVisibleItemMenu(R.id.menu_user_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_card_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_product_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_upload, false);
                                break;
                        }
                        //Load user avatar with image id
                        userStorageRepository.findUriById(currentUser.getImage(), new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                listener.loadUserAvatar(uri);
                            }
                        }, null);
                        //Load user role with role id
                        roleRepository.findById(currentUser.getRole(), new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    listener.showUserRole(dataSnapshot.getValue(String.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //Load user gender
                        listener.showUserGenderImage(Validate.validateGender(currentUser.getSex()));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void loadCategory() {
        categoryRepository.findAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    ArrayList<Category> categories = new ArrayList<>();
                    for (DataSnapshot item : iterable) {
                        categories.add(item.getValue(Category.class));
                    }
                    Category.setInstance(categories);
                    listener.setNavigationItemSelectedListener();
                    listener.initViewPager();
                    listener.setupTabIcons();
                    listener.setEvents();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void loadProductSuggestions() {
        productRepository.findAndWatch(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> suggestions = new HashMap<>();
                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                for (DataSnapshot item : iterable) {
                    if (User.getInstance().getRole() == User.ADMIN
                            || item.child(Constants.ProductStatus).getValue(int.class) == Constants.ProductEnable) {
                        suggestions.put(item.getKey(), item.child(Constants.ProductTitle).getValue(String.class));
                    }
                }
                listener.setProductSuggestions(suggestions);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
