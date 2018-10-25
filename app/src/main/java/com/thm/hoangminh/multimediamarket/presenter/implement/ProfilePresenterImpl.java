package com.thm.hoangminh.multimediamarket.presenter.implement;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.Category;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.presenter.ProfilePresenter;
import com.thm.hoangminh.multimediamarket.repository.PurchasedProductRepository;
import com.thm.hoangminh.multimediamarket.repository.RoleRepository;
import com.thm.hoangminh.multimediamarket.repository.UserRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.PurchasedProductRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.RoleRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.utility.Tools;
import com.thm.hoangminh.multimediamarket.view.activity.ProductActivity;
import com.thm.hoangminh.multimediamarket.view.callback.ProfileView;

public class ProfilePresenterImpl implements ProfilePresenter {
    private String userId;
    private User currentUser;
    private ProfileView listener;
    private FirebaseUser dbCurrentUser;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private final String PASSWORD_KEY = "password";
    private UserStorageRepository userStorageRepository;
    private PurchasedProductRepository purchasedProductRepository;


    public ProfilePresenterImpl(ProfileView listener) {
        this.listener = listener;
        userRepository = new UserRepositoryImpl();
        roleRepository = new RoleRepositoryImpl();
        userStorageRepository = new UserStorageRepositoryImpl();
        purchasedProductRepository = new PurchasedProductRepositoryImpl();
        dbCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public void extractBundle(Bundle bundle) {
        if (bundle != null) {
            userId = bundle.getString(Constants.UserIdKey);
            listener.showEditableBalance(true);
        }
        if (userId == null) userId = dbCurrentUser.getUid();
        loadCurrentUserData();
    }

    private void loadCurrentUserData() {
        loadCurrentUserInformation();
        loadCurrentUserMultimedia();
        checkCurrentUserProvider();
    }

    private void loadCurrentUserInformation() {
        userRepository.findAndWatchById(userId,
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            currentUser = dataSnapshot.getValue(User.class);
                            listener.showCurrentUserInformation(currentUser);
                            listener.registerImageUser();
                            loadUserRole();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void loadUserRole() {
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
    }

    private void loadCurrentUserMultimedia() {
        for (int i = 1; i < 5; i++) {
            final int finalI = i;
            purchasedProductRepository.findAll(userId, Category.getInstance().get(i).getCateId(),
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                Iterable<DataSnapshot> iterable = dataSnapshot.getChildren();
                                int size = 0;
                                for (DataSnapshot item : iterable) {
                                    size++;
                                }
                                switch (finalI) {
                                    case 1:
                                        listener.showGameNumber(size);
                                        break;
                                    case 2:
                                        listener.showImageNumber(size);
                                        break;
                                    case 3:
                                        listener.showVideoNumber(size);
                                        break;
                                    case 4:
                                        listener.showMusicNumber(size);
                                        break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }

    private void checkCurrentUserProvider() {
        if (userId.equals(dbCurrentUser.getUid())) {
            String provider = dbCurrentUser.getProviders().get(0);
            if (provider.equals(PASSWORD_KEY)) {
                listener.enableChangeCurrentUserEmail();
                listener.enableChangeCurrentUserPassword();
            }
        }
    }

    @Override
    public void updateImageCurrentUser(final Bitmap bitmap) {
        if (bitmap != null) {
            String imagePath = currentUser.getImage();
            final String newImagePath = Tools.createImageNameRandom();
            if (imagePath.equals(Constants.UserImageDefault)) {
                userRepository.setImagePath(currentUser.getId(), newImagePath, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        userStorageRepository.add(newImagePath, bitmap, new OnSuccessListener() {
                                    @Override
                                    public void onSuccess(Object o) {
                                        listener.showMessage(R.string.info_editSuccess);
                                    }
                                },
                                new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.showMessage(R.string.info_failure);
                                    }
                                });
                    }
                }, null);
            } else {
                userStorageRepository.update(currentUser.getImage(), bitmap,
                        new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                listener.showMessage(R.string.info_editSuccess);
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.showMessage(R.string.info_failure);
                            }
                        });
            }
        }
    }

    @Override
    public void removeAvatarUser() {
        userStorageRepository.remove(currentUser.getImage(), new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                userRepository.setImagePath(currentUser.getId(), Constants.UserImageDefault, new OnSuccessListener() {
                    @Override
                    public void onSuccess(Object o) {
                        listener.showMessage(R.string.info_editSuccess);
                    }
                }, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_failure);
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure);
            }
        });
    }

    @Override
    public void updateUserBalance(double balance) {
        userRepository.setBalance(currentUser.getId(), balance, new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                listener.showMessage(R.string.info_editSuccess);
                listener.dismissDialog();
                listener.editable();
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure);
                listener.hideProgresbarDialog();
            }
        });
    }

    @Override
    public void updateUsername(String username) {
        listener.showProgresbarDialog();
        userRepository.setUsername(currentUser.getId(), username,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.showMessage(R.string.info_editSuccess);
                        listener.dismissDialog();
                        listener.editable();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_failure);
                        listener.hideProgresbarDialog();
                    }
                });
    }

    @Override
    public void updatePassword(String password) {
        listener.showProgresbarDialog();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.updatePassword(password).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                listener.showMessage(R.string.info_editSuccess);
                listener.dismissDialog();
                listener.editable();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure);
                listener.hideProgresbarDialog();
            }
        });
    }

    @Override
    public void updateUserEmail(final String email) {
        listener.showProgresbarDialog();
        final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                userRepository.setEmail(currentUser.getUid(), email,
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                listener.showMessage(R.string.info_editSuccess);
                                listener.dismissDialog();
                                listener.editable();
                            }
                        },
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                listener.showMessage(R.string.info_failure);
                                listener.hideProgresbarDialog();
                            }
                        });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                listener.showMessage(R.string.info_failure);
                listener.hideProgresbarDialog();
            }
        });
    }

    @Override
    public void updateBirthday(int day, int month, int year) {
        listener.showProgresbarDialog();
        userRepository.setBirthday(currentUser.getId(), day + "/" + month + "/" + year,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.showMessage(R.string.info_editSuccess);
                        listener.dismissDialog();
                        listener.editable();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_failure);
                        listener.hideProgresbarDialog();
                    }
                });
    }

    @Override
    public void updateGender(int genderId) {
        userRepository.setGender(currentUser.getId(), genderId,
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listener.showMessage(R.string.info_editSuccess);
                        listener.dismissDialog();
                        listener.editable();
                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.showMessage(R.string.info_failure);
                        listener.hideProgresbarDialog();
                    }
                });
    }

    @Override
    public void redirectToProductActivity(int cateIndex) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.BundleOptionKey, Constants.UserOption);
        bundle.putString(Constants.UserIdKey, currentUser.getId());
        bundle.putString(Constants.CateIdKey, Category.getInstance().get(cateIndex).getCateId());
        bundle.putString(Constants.CateTitleKey, Category.getInstance().get(cateIndex).getName());
        listener.startActivity(ProductActivity.class, bundle);
    }
}
