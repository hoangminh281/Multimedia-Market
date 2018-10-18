package com.thm.hoangminh.multimediamarket.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.User;

public class SigninActivity extends AppCompatActivity implements FirebaseAuth.AuthStateListener, GoogleApiClient.OnConnectionFailedListener {
    private FirebaseAuth mAuth;
    private DatabaseReference mRef;
    private EditText edtUsername, edtPassword;
    private ProgressDialog progressDialog;
    private GoogleApiClient apiClient;
    private final int RC_SIGN_IN = 1111;
    private int CHECK_AUTHENTICATION = 0;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1111;

    final String TAG = "SigninActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
        SetControl();
        //mAuth.signOut();
        CreateClientLoginGG();

        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if ((grantResults.length > 0) && (grantResults[0] != PackageManager.PERMISSION_GRANTED)) {
                    this.finish();
                }
                break;
        }
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SigninActivity.this, R.string.info_failure_signin, Toast.LENGTH_SHORT).show();
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
            Intent login = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(login);
            finish();
            mRef.child("users/" + firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        final User user = new User(firebaseUser.getUid(), firebaseUser.getDisplayName(), "user.png"
                                , firebaseUser.getEmail(), firebaseUser.getPhoneNumber(), "", 2
                                , 0, 2, 1);//Nếu không tồn tại, tạo user mới
                        mRef.child("users/" + user.getId()).setValue(user);
                        Intent in = new Intent(SigninActivity.this, MainActivity.class);
                        startActivity(in);
                        finish();
                    } else {
                        mRef.child("users/" + firebaseUser.getUid() + "/status").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    if (dataSnapshot.getValue(int.class) == 0) {
                                        FirebaseAuth.getInstance().signOut();//kiểm tra trạng thái nếu không hoạt động sẽ không được login
                                        Toast.makeText(SigninActivity.this, R.string.info_logout, Toast.LENGTH_SHORT).show();
                                    }
                                }
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

    }

    public void SigninGG(View view) {
        progressDialog.show();
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
