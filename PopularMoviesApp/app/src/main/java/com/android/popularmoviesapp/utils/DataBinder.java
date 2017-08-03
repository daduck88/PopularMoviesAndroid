package com.android.popularmoviesapp.utils;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Daduck on 8/1/17.
 */
public final class DataBinder {

    private DataBinder() {
        //NO-OP
    }

    @BindingAdapter("imageUrl")
    public static void imageUrl(ImageView imageView, String url) {
        Context context = imageView.getContext();
        Picasso.with(context)
                .load(url)
                .into(imageView);
    }
}