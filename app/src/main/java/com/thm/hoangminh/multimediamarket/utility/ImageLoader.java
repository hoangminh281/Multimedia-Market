package com.thm.hoangminh.multimediamarket.utility;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;
import com.thm.hoangminh.multimediamarket.R;
import com.thm.hoangminh.multimediamarket.repository.ProductDetailStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.ProductStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.UserStorageRepository;
import com.thm.hoangminh.multimediamarket.repository.base.StorageRepository;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductDetailStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.ProductStorageRepositoryImpl;
import com.thm.hoangminh.multimediamarket.repository.implement.UserStorageRepositoryImpl;

public class ImageLoader {
    public static void loadImage(Class<?> clazz, final Context context, final ImageView img, String imageId) {
        StorageRepository storageRepository;
        if (clazz.isAssignableFrom(UserStorageRepository.class)) {
            storageRepository = new UserStorageRepositoryImpl();
        } else if (clazz.isAssignableFrom(ProductStorageRepository.class)) {
            storageRepository = new ProductStorageRepositoryImpl();
        } else if (clazz.isAssignableFrom(ProductDetailStorageRepository.class)) {
            storageRepository = new ProductDetailStorageRepositoryImpl();
        }
        else return;
        if (storageRepository != null) {
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
    }

    public static void loadImageByUri(final Context context, final ImageView img, Uri uri) {
        Picasso.with(context)
                .load(uri)
                .error(R.mipmap.icon_app_2)
                .into(img);
    }

    public static void loadImageByString(final Context context, final ImageView img, String path) {
        Picasso.with(context)
                .load(path)
                .error(R.mipmap.icon_app_2)
                .into(img);
    }
}
