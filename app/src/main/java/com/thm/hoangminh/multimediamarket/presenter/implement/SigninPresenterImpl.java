package com.thm.hoangminh.multimediamarket.presenter.service.implement;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.config.constant.Constants;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.presenter.service.SigninPresenter;
import com.thm.hoangminh.multimediamarket.repository.Repository;
import com.thm.hoangminh.multimediamarket.repository.UserRepositoryImpl;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;
import com.thm.hoangminh.multimediamarket.view.callback.SigninView;

public class SigninPresenterImpl implements SigninPresenter, FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private SigninView listener;
    private final Context context;
    private GoogleApiClient apiClient;
    private final int RC_SIGN_IN = 1111;
    private Repository<User, String, ValueEventListener> userRepository;

    public SigninPresenterImpl(Context context, SigninView listener) {
        this.context = context;
        this.listener = listener;
        this.mAuth = FirebaseAuth.getInstance();
        userRepository = new UserRepositoryImpl();
        this.createClientLoginGG();
    }

    @Override
    public void addSigninListener() {
        mAuth.addAuthStateListener(this);
    }

    @Override
    public void removeSigninListener() {
        mAuth.removeAuthStateListener(this);
    }

    @Override
    public void signin() {
        if (listener.getUsername().length() == 0) {
            listener.setUsernameError(R.string.err_email);
            listener.hideLoadingProgressDialog();
        } else if (listener.getPassword().length() == 0) {
            listener.setPasswordError(R.string.err_password);
            listener.hideLoadingProgressDialog();
        } else {
            mAuth.signInWithEmailAndPassword(listener.getUsername(), listener.getPassword())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.hideLoadingProgressDialog();
                    listener.showMessage(R.string.info_failure_signin);
                }
            });
        }
    }

    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        listener.showLoadingProgressDialog();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            userRepository.findById(firebaseUser.getUid(), new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user;
                    if (!dataSnapshot.exists()) {//Nếu không tồn tại, tạo user mới
                        user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), firebaseUser.getEmail(),
                                firebaseUser.getPhoneNumber());
                        userRepository.add(user);
                    } else {
                        user = dataSnapshot.getValue(User.class);
                        if (user.getStatus() != Constants.UserEnable) {//kiểm tra trạng thái nếu không hoạt động sẽ không được login
                            FirebaseAuth.getInstance().signOut();
                            listener.showMessage(R.string.info_logout);
                            return;
                        }
                    }
                    listener.hideLoadingProgressDialog();
                    listener.startActivity(MainActivity.class);
                    listener.finish();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void createClientLoginGG() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage((FragmentActivity) context, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }

    @Override
    public void signinGG() {
        listener.showLoadingProgressDialog();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        listener.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void activityResultCallback(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == ((Activity) context).RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FirebaseLoginAuthentication(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    private void FirebaseLoginAuthentication(String token_id) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(token_id, null);
        mAuth.signInWithCredential(authCredential);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
