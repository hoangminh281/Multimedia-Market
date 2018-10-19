package com.thm.hoangminh.multimediamarket.utility;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;

public class ImageLoader {
    public static void loadImage(StorageRepository storageRepository, final Context context, final ImageView img, String imageId) {
        storageRepository.findUriById(imageId, new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context)
                        .load(uri)
                        .error(R.mipmap.icon_app_2)
                        .into(img);
            }
        }, null);
    }

    public static void loadImageByUri(final Context context, final ImageView img, Uri uri) {
        Picasso.with(context)
                .load(uri)
                .error(R.mipmap.icon_app_2)
                .into(img);
    }
}