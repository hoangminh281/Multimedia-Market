package com.thm.hoangminh.multimediamarket.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.constant.Constants;
import com.thm.hoangminh.multimediamarket.models.User;
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
}
