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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.views.MainViews.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword, edtPasswordConfirm, edtUsername;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);

        setControls();
    }

    void setControls() {
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        edtUsername = findViewById(R.id.edtUsername);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtPasswordConfirm = findViewById(R.id.edtPasswordConfirm);
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
            Toast.makeText(this, "Username required", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (email.trim().length() == 0) {
            Toast.makeText(this, "Email required", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (password.trim().length() == 0) {
            Toast.makeText(this, "Pass required", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else if (!nhaplaimatkhau.equals(password)) {
            Toast.makeText(this, "Password not same", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();
                        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                        new User(firebaseUser.getUid(), username, "user.png", email, "", "", 2, 2, 0).createUserOnFirebase();

                        Intent login = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(login);
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Log.d("RegisterActivity", "Signup failure. " + e.getMessage());
                }
            });
        }
    }
}
