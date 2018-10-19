package com.thm.hoangminh.multimediamarket.view.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.presenter.SigninPresenter;
import com.thm.hoangminh.multimediamarket.presenter.implement.SigninPresenterImpl;
import com.thm.hoangminh.multimediamarket.utility.Validate;
import com.thm.hoangminh.multimediamarket.view.callback.SigninView;

public class SigninActivity extends AppCompatActivity implements SigninView {
    private ProgressDialog progressDialog;
    private EditText edtUsername, edtPassword;

    private SigninPresenter presenter;
    private final int REQUEST_WRITE_EXTERNAL_STORAGE = 1111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_layout);
        CheckWriteStoragePermisstion();
        setControls();
        initPresenter();
    }

    private void initPresenter() {
        presenter = new SigninPresenterImpl(this, this);
    }

    private void CheckWriteStoragePermisstion() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
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

    @Override
    protected void onStart() {
        super.onStart();
        presenter.addSigninListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.removeSigninListener();
    }

    public void signin(View view) {
        boolean validate = Validate.validateEditTextsToString(this, edtUsername)
                & Validate.validatePassword(this, edtPassword);
        if (!validate) return;
        this.showLoadingProgressDialog();
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        presenter.signin(username, password);
    }

    @Override
    public void hideLoadingProgressDialog() {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingProgressDialog() {
        progressDialog.setMessage("Signing in, please waiting a moment");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    public void SigninGG(View view) {
        presenter.signinGG();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.activityResultCallback(requestCode, resultCode, data);
    }

    public void SigninFB(View view) {
    }

    public void redirectToSignup(View view) {
        startActivity(RegisterActivity.class);
    }

    @Override
    public void startActivity(Class<?> clazz) {
        startActivity(new Intent(this, clazz));
    }

    private void setControls() {
        edtUsername = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.dismiss();
    }
}
