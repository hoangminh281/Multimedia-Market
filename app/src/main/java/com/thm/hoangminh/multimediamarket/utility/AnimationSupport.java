package com.thm.hoangminh.multimediamarket.utility;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.thm.hoangminh.multimediamarket.R;

public class AnimationSupport {
    public static void slide_down(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_down);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void fade_in(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.fade_in);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void fade_out(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.fade_out);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void zoom_in(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void zoom_out(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.zoom_out);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void shake(Context context, View v) {
        Animation a = AnimationUtils.loadAnimation(context, R.anim.shake);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
}
