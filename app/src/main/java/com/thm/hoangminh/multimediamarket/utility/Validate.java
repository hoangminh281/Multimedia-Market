package com.thm.hoangminh.multimediamarket.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.model.User;
import com.thm.hoangminh.multimediamarket.view.activity.BookmarkActivity;
import com.thm.hoangminh.multimediamarket.view.activity.MainActivity;
import com.thm.hoangminh.multimediamarket.view.activity.SigninActivity;

public class Validate {
    public static boolean validateCurrentUserStatus(final Context context, int status) {
        if (status != Constants.UserEnable) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(context, R.string.info_logout, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SigninActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            return false;
        }
        return true;
    }

    public static boolean validateCurrentUserRole(final Context context, int roleId) {
        if (roleId != User.ADMIN) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, R.string.info_fail_role, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                    ((Activity)context).finish();
                }
            });
            return false;
        }
        return true;
    }

    public static int validateGender(int gender) {
        if (gender == 0) {
            return R.mipmap.ic_male;
        } else if (gender == 1) {
            return R.mipmap.ic_female;
        } else {
            return R.mipmap.ic_male_female;
        }
    }

    public static boolean validateEditTextsToString(Context context, EditText... editTexts) {
        boolean validate = true;
        for (EditText editText : editTexts) {
            if (editText.getVisibility() == View.VISIBLE && editText.getText().toString().trim().length() == 0) {
                editText.setError(context.getResources().getString(R.string.err_empty));
                validate = false;
            }
        }

        return validate;
    }

    public static boolean validateEditTextsToNumber(Context context, EditText... editTexts) {
        boolean validate = true;
        for (EditText editText : editTexts) {
            if (editText.getVisibility() == View.VISIBLE
                    && (editText.getText().toString().trim().length() == 0
                    || Double.parseDouble(editText.getText().toString().trim()) == 0)) {
                editText.setError(context.getResources().getString(R.string.err_empty));
                validate = false;
            }
        }

        return validate;
    }

    public static boolean validateTextViewsToString(Context context, TextView... textViews) {
        boolean validate = true;
        for (TextView textView : textViews) {
            if (textView.getVisibility() == View.VISIBLE && textView.getText().toString().trim().length() == 0) {
                textView.setError(context.getResources().getString(R.string.err_empty));
                validate = false;
            }
        }

        return validate;
    }

    public static void validateImageProduct(ImageView img, int status) {
        if (status == Constants.ProductDisable) img.setColorFilter(R.color.white_transparent);
        else img.clearColorFilter();
    }

    public static boolean validatePassword(Context context, EditText... edtPasswords) {
        boolean validate = true;
        for (EditText editText : edtPasswords) {
            if (editText.getVisibility() != View.VISIBLE) {
                continue;
            }
            String password = editText.getText().toString().trim();
            if (password.length() == 0) {
                editText.setError(context.getResources().getString(R.string.err_empty));
                validate = false;
            } else if (password.length() < 6) {
                editText.setError(context.getResources().getString(R.string.err_notlength));
                validate = false;
            }
        }

        return validate;
    }

    public static boolean validateSamePassword(Context context, EditText edtPassword, EditText edtRepassword) {
        boolean validate = true;
        String password = edtPassword.getText().toString().trim();
        String repassword = edtRepassword.getText().toString().trim();
        if (!password.equals(repassword)) {
            edtRepassword.setError(context.getResources().getString(R.string.err_passnotsame));
            validate = false;
        }

        return validate;
    }

    public static boolean validateAge(Context context, int day, int month, int year) {
        try {
            com.thm.hoangminh.multimediamarket.references.Tools.getAge(day, month, year);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, R.string.err_ageExceed, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
