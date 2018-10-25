package com.thm.hoangminh.multimediamarket.utility;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class Tools {
    public static String md5(String data) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(data.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);

            if (listItem != null) {
                // This next line is needed before you call measure or else you won't get measured height at all. The listitem needs to be drawn first to know the height.
                listItem.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
                totalHeight += listItem.getMeasuredHeight();

            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static int getAge(int day, int month, int year) throws IllegalArgumentException {
        final Calendar birthDay = Calendar.getInstance();
        birthDay.set(year, month, day);
        final Calendar current = Calendar.getInstance();
        if (current.getTimeInMillis() < birthDay.getTimeInMillis())
            throw new IllegalArgumentException("age < 0");
        int age = current.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);
        if (birthDay.get(Calendar.MONTH) > current.get(Calendar.MONTH) ||
                (birthDay.get(Calendar.MONTH) == current.get(Calendar.MONTH) &&
                        birthDay.get(Calendar.DATE) > current.get(Calendar.DATE)))
            age--;
        return age;
    }

    public static String createImageNameRandom() {
        Calendar calendar = Calendar.getInstance();
        return "image" + calendar.getTimeInMillis() + ".png";
    }

    public static String createFileNameRandom(String name) {
        String suffix = name.split("\\.")[1];
        Calendar calendar = Calendar.getInstance();
        return "file" + calendar.getTimeInMillis() + "." + suffix;
    }

    public static long getSizeFromUri(Activity activity, Uri uri) throws IOException {
        String scheme = uri.getScheme();
        if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
            try {
                InputStream fileInputStream = activity.getApplicationContext().getContentResolver().openInputStream(uri);
                return fileInputStream.available();
            } catch (Exception e) {
                throw e;
            }
        } else if (scheme.equals(ContentResolver.SCHEME_FILE)) {
            String path = uri.getPath();
            try {
                return new File(path).length();
            } catch (Exception e) {
                throw e;
            }
        }
        return 0;
    }
}
