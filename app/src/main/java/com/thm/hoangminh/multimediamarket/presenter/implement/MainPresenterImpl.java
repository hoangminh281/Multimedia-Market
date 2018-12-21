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
import com.thm.hoangminh.multimediamarket.business.SectionCalculator;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.ProductDetail;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.MainPresenter;
import com.thm.hoangminh.multimediamarket.repository.CategoryRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RatingRepository;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;
import com.thm.hoangminh.multimediamarket.repository.SectionRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.CategoryRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RatingRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RoleRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.SectionRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainPresenterImpl implements MainPresenter {
    private MainView listener;
    private final Context context;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private RatingRepository ratingRepository;
    private ProductRepository productRepository;
    private SectionRepository sectionRepository;
    private CategoryRepository categoryRepository;
    private UserStorageRepository userStorageRepository;
    private ProductDetailRepository productDetailRepository;

    public MainPresenterImpl(MainView listener, Context context) {
        this.context = context;
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        roleRepository = new RoleRepositoryImpl();
        ratingRepository = new RatingRepositoryImpl();
        productRepository = new ProductRepositoryImpl();
        sectionRepository = new SectionRepositoryImpl();
        categoryRepository = new CategoryRepositoryImpl();
        userStorageRepository = new UserStorageRepositoryImpl();
        productDetailRepository = new ProductDetailRepositoryImpl();
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
                    if (Validate.validateCurrentUserStatus(context, currentUser.getStatus(), Constants.UserEnable)) {
                        //setup menu with Role and rule
                        switch (currentUser.getRole()) {
                            case Constants.AdminRole:
                                listener.setVisibleItemMenu(R.id.menu_user_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_card_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_product_admin, true);
                                listener.setVisibleItemMenu(R.id.menu_upload, true);
                                listener.setVisibleItemMenu(R.id.menu_refresh_section, true);
                                break;
                            case Constants.ModRole:
                                listener.setVisibleItemMenu(R.id.menu_user_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_card_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_product_admin, false);
                                listener.setVisibleItemMenu(R.id.menu_upload, true);
                                break;
                            case Constants.UserRole:
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
                        listener.showUserGenderImage(Validate.validateGenderToMipmap(currentUser.getSex()));
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
                    if (User.getInstance().getRole() == Constants.AdminRole
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

    @Override
    public void refreshProductSection() {
        final LinkedHashMap<String, Integer> dominatedArcadeProducts = new LinkedHashMap<>();
        final LinkedHashMap<String, Integer> hintedProducts = new LinkedHashMap<>();

        productDetailRepository.findAll(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                    for (DataSnapshot item : iterable) {
                        final ProductDetail productDetail = item.getValue(ProductDetail.class);
                        dominatedArcadeProducts.put(productDetail.getId(), productDetail.getBuyCount());
                        if (productDetail.getViews() != null) {
                            hintedProducts.put(productDetail.getId(), SectionCalculator.calculateHintedGamePoint(productDetail.getViews().size()));
                        }
                    }
                    saveSectionProductIds(Constants.HintedSectionId, hintedProducts);

                    ratingRepository.findAll(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                                for (DataSnapshot item : iterable) {
                                    String productRatingId = item.getKey();
                                    if (dominatedArcadeProducts.get(productRatingId) != null) {
                                        dominatedArcadeProducts.put(productRatingId, SectionCalculator.calculateDominatedArcadeGamePoint(dominatedArcadeProducts.get(productRatingId), (int) item.getChildrenCount()));
                                    } else {
                                        dominatedArcadeProducts.put(productRatingId, SectionCalculator.calculateDominatedArcadeGamePoint(0, (int) item.getChildrenCount()));
                                    }
                                }
                            }
                            saveSectionProductIds(Constants.DominatedArcadeSectionId, dominatedArcadeProducts);
                            listener.refreshSectionData();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void saveSectionProductIds(String sectionId, Map<String, Integer> dominatedArcadeProducts) {
        for (Map.Entry<String, Integer> entry : dominatedArcadeProducts.entrySet()) {
            productRepository.findCateById(entry.getKey(), new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.getValue(String.class).equals(Category.getInstance().get(1).getCateId())) {
                            sectionRepository.setProductValue(Constants.Home, sectionId, entry.getKey(), entry.getValue(), null, null);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
