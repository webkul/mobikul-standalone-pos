package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webkul.mobikul.mobikulstandalonepos.R;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;

public class DataBindingAdapters {

    @BindingAdapter("imageResource")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter(value = {"imageUrl", "placeholder", "sizeDevider"}, requireAll = false)
    public static void loadImage(ImageView view, String imageUrl, Drawable placeHolder, double sizeDevider) {
        if (imageUrl != null && imageUrl.isEmpty()) {
            imageUrl = null;
        }

        Log.d(TAG, "loadImage: " + imageUrl);
        /*LOAD WITHOUT PLACE HOLDER*/
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
            Glide.with(view.getContext())
                    .load(imageUrl).placeholder(R.drawable.ic_product_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(view);
            Log.d("Image", imageUrl + "");
        } else {
            Glide.with(view.getContext())
                    .load(R.drawable.ic_product_placeholder).placeholder(R.drawable.ic_product_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .into(view);
            Log.d("Placeholder Image", imageUrl + "---" + view);
        }
    }
}
