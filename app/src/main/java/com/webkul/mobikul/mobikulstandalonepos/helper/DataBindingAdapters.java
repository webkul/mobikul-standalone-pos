package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.constants.BundleConstants;

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

    @BindingAdapter({"emptyTextSubHeading"})
    public static void setEmptyTextSubHeading(TextView textview, BundleConstants.EmptyLayoutType pageType) {
        switch (pageType) {
            case CUSTOMER:
                textview.setText(textview.getContext().getString(R.string.no_customer_secondary));
                break;
            case PRODUCT:
                textview.setText(textview.getContext().getString(R.string.no_products_available_yet));
                break;
//            case ORDER:
//                textview.setText(textview.getContext().getString(R.string.shop_first_to_see_your_orders_here));
//                break;
            case CART:
                textview.setText(textview.getContext().getString(R.string.add_products_to_your_cart_first));
                break;
//            case WISHLIST:
//                textview.setText(textview.getContext().getString(R.string.you_do_not_have_any_wishlist_yet));
//                break;
//            case NOTIFICATION:
//                textview.setText(textview.getContext().getString(R.string.no_recent_notifications));
//                break;
//            default:
//                break;

        }

    }

    @BindingAdapter({"emptyTextHeading"})
    public static void setEmptyTextHeading(TextView textview, BundleConstants.EmptyLayoutType pageType) {
        switch (pageType) {
            case CUSTOMER:
                textview.setText(textview.getContext().getString(R.string.no_customer));
                break;
            case PRODUCT:
                textview.setText(textview.getContext().getString(R.string.no_products));
                break;
//            case ORDER:
//                textview.setText(textview.getContext().getString(R.string.no_order_placed));
//                break;
            case CART:
                textview.setText(textview.getContext().getString(R.string.your_bag_is_empty));
                break;
//            case WISHLIST:
//                textview.setText(textview.getContext().getString(R.string.empty_wishlist));
//                break;
//            case NOTIFICATION:
//                textview.setText(textview.getContext().getString(R.string.no_notification));
//                break;
//            default:
//                break;
        }

    }

    @BindingAdapter({"emptyIcon"})
    public static void setEmptyIcon(ImageView imageview, BundleConstants.EmptyLayoutType pageType) {
        switch (pageType) {
            case CUSTOMER:
                imageview.setImageResource(R.drawable.ic_customer_empty);
                break;
            case PRODUCT:
                imageview.setImageResource(R.drawable.ic_empty_product);
                break;
//            case ORDER:
//                imageview.setImageResource(R.drawable.ic_vector_empty_order);
//                break;
            case CART:
                imageview.setImageResource(R.drawable.ic_cart_empty);
                break;
//            case WISHLIST:
//                imageview.setImageResource(R.drawable.ic_vector_empty_wishlist);
//                break;
//            case NOTIFICATION:
//                imageview.setImageResource(R.drawable.ic_vector_empty_notification);
//                break;
//            default:
//                break;
        }
    }
}
