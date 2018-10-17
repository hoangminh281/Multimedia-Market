package com.thm.hoangminh.multimediamarket.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.models.User;
import com.thm.hoangminh.multimediamarket.view.activity.ProfileActivity;
import com.thm.hoangminh.multimediamarket.view.activity.SigninActivity;

public class UserValidate {
    public static boolean validateCurrentUser(final Context context) {
        User currentUser = User.getInstance();
        if (currentUser != null && currentUser.getStatus() != Constants.UserEnable) {
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
        for (EditText editText : editTexts) {
            if (editText.getVisibility() == View.VISIBLE && editText.getText().toString().trim().length() == 0) {
                editText.setError(context.getResources().getString(R.string.err_empty));

                return false;
            }
        }

        return true;
    }

    public static boolean validateEditTextsToNumber(Context context, EditText... editTexts) {
        for (EditText editText : editTexts) {
            if (editText.getVisibility() == View.VISIBLE
                    && (editText.getText().toString().trim().length() == 0
                    || Double.parseDouble(editText.getText().toString().trim()) == 0)) {
                editText.setError(context.getResources().getString(R.string.err_empty));

                return false;
            }
        }

        return true;
    }

    public static boolean validateTextViewsToString(Context context, TextView... textViews) {
        for (TextView textView : textViews) {
            if (textView.getVisibility() == View.VISIBLE && textView.getText().toString().trim().length() == 0) {
                textView.setError(context.getResources().getString(R.string.err_empty));

                return false;
            }
        }

        return true;
    }

    public static void validateImageProduct(ImageView img, int status) {
        if (status == Constants.ProductDisable) img.setColorFilter(R.color.white_transparent);
        else img.clearColorFilter();
    }

    public static boolean validatePassword(Context context, EditText... edtPasswords) {
        for (EditText editText : edtPasswords) {
            if (editText.getVisibility() != View.VISIBLE) {
                continue;
            }
            String password = editText.getText().toString().trim();
            if (password.length() == 0) {
                editText.setError(context.getResources().getString(R.string.err_empty));

                return false;
            } else if (password.length() < 6) {
                editText.setError(context.getResources().getString(R.string.err_notlength));

                return false;
            }
        }

        return true;
    }

    public static boolean validateSamePassword(Context context, EditText edtPassword, EditText edtRepassword) {
        String password = edtPassword.getText().toString().trim();
        String repassword = edtRepassword.getText().toString().trim();
        if (!password.equals(repassword)) {
            edtRepassword.setError(context.getResources().getString(R.string.err_passnotsame));

            return false;
        }

        return true;
    }

    public static boolean validateAge(Context context, int day, int month, int year) {
        try {
            Tools.getAge(day, month, year);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, R.string.err_ageExceed, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
