package com.thm.hoangminh.multimediamarket.view.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.model.User;

public class RegisterActivity extends AppCompatActivity {
    private EditText edtEmail, edtPassword, edtPasswordConfirm, edtUsername;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        setControls();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_arrowleft);
    }

    void setControls() {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConfirm = findViewById(R.id.edtPasswordConfirm);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Signup(View view) {
        progressDialog.setMessage("Loading...");
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        final String username = edtUsername.getText().toString();
        final String email = edtEmail.getText().toString();
        final String password = edtPassword.getText().toString();
        String nhaplaimatkhau = edtPasswordConfirm.getText().toString();

        if (username.trim().length() == 0) {
            Toast.makeText(this, R.string.err_username, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (email.trim().length() == 0) {
            Toast.makeText(this, R.string.err_email, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (password.trim().length() == 0) {
            Toast.makeText(this, R.string.err_password, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (!nhaplaimatkhau.equals(password)) {
            Toast.makeText(this, R.string.err_passnotsame, Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        final User user = new User(firebaseUser.getUid(), username, "user.png", email, "", "", 2, 0, 2, 1);
                        mRef.child("users/" + user.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    mRef.child("users/" + user.getId()).setValue(user);// Nếu không tồn tại sẽ tạo user mới
                                }
                                Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                                startActivity(login);
                                finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, R.string.err_signup, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
