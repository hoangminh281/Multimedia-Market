package com.thm.hoangminh.multimediamarket.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.thm.hoangminh.multimediamarket.R;

public class SplashscreenActivity extends AppCompatActivity {
    final static String SHARED_PREFERENCES_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        AnhXa();
        if (getFirstApp()) {
            Intent intent = new Intent(SplashscreenActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            setFirstApp();
        }
    }

    private void AnhXa() {

    }

    private void setFirstApp() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("IS_FIRTS_LAUNCHER", true);
        if (!editor.commit()) {
            Toast.makeText(this, "Cannot save first time app", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getFirstApp() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean("IS_FIRTS_LAUNCHER", false);
    }
}
