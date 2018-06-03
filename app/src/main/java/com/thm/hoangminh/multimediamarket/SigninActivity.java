package com.thm.hoangminh.multimediamarket;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;

public class SigninActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private EditText edtUsername, edtPassword;
    private ProgressDialog progressDialog;
    private GoogleApiClient apiClient;
    private final int RC_SIGN_IN = 1111;
    private int CHECK_AUTHENTICATION = 0;

    final String TAG = "SigninActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        SetControl();
        //mAuth.signOut();
        CreateClientLoginGG();
    }


    public void CreateClientLoginGG() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(this);
    }

    public void Signin(View view) {
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (username.length() == 0) {
            edtUsername.setError("Empty email");
            progressDialog.dismiss();
        } else if (password.length() == 0) {
            edtPassword.setError("Empty email");
            progressDialog.dismiss();
        } else {
            mAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Log.d(TAG, "Đăng nhập thành công.");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Đăng nhập thất bại. " + e.getMessage());
                }
            });
        }
    }

    //method: know status of user login or log out
    @Override
    public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
        progressDialog.dismiss();
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), "user.png", firebaseUser.getEmail(), firebaseUser.getPhoneNumber(), "", "", 0).createUserOnFirebase();

            Intent in = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(in);
            finish();
        }
    }

    public void SigninGG(View view) {
        CHECK_AUTHENTICATION = 2222;
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == RESULT_OK) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                FirebaseLoginAuthentication(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    public void SigninFB(View view) {
    }

    public void FirebaseLoginAuthentication(String token_id) {
        if (CHECK_AUTHENTICATION == 2222) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(token_id, null);
            mAuth.signInWithCredential(authCredential);
        }
    }

    public void gotoSignup(View view) {
        Intent intent = new Intent(SigninActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void SetControl() {
        edtUsername = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
    }

}
