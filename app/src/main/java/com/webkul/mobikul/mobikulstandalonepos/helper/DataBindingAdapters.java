package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.databinding.BindingAdapter;
import android.databinding.InverseBindingAdapter;
import android.databinding.InverseBindingListener;
import android.databinding.InverseMethod;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
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
        /*LOAD WITHOUT PLACE HOLDER*/
        if (imageUrl != null && !imageUrl.equalsIgnoreCase("")) {
            try {
                Glide.with(view.getContext())
                        .load(imageUrl)
//                    .error(R.drawable.ic_error)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .dontAnimate()
                        .into(view);
            } catch (Exception e) {
                view.setImageResource(R.drawable.ic_error);
            }
        } else {
            view.setImageResource(R.drawable.ic_product_placeholder);
            Log.d("Placeholder Image", imageUrl + "---" + view);
        }
    }

    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(AppCompatSpinner pAppCompatSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
            pAppCompatSpinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(AppCompatSpinner pAppCompatSpinner) {
        return (String) pAppCompatSpinner.getSelectedItem();
    }

//
//    @InverseMethod("toString")
//    public static String toStringFormat(String string, String data) {
//        return String.format(string, data);
//    }
//
//    public static String toString(String data) {
//        return data;
//    }

//    public static PhoneNumberType toPhoneNumberType(int ordinal) {
//        return PhoneNumberType.values()[ordinal];
//    }

    @BindingAdapter({"emptyTextSubHeading"})
    public static void setEmptyTextSubHeading(TextView textview, BundleConstants.EmptyLayoutType pageType) {
        switch (pageType) {
            case CUSTOMER:
                textview.setText(textview.getContext().getString(R.string.no_customer_secondary));
                break;
            case PRODUCT:
                textview.setText(textview.getContext().getString(R.string.no_products_available_yet));
                break;
            case ORDER:
                textview.setText(textview.getContext().getString(R.string.shop_first_to_see_your_orders_here));
                break;
            case CART:
                textview.setText(textview.getContext().getString(R.string.add_products_to_your_cart_first));
                break;
            case HOLD:
                textview.setText(textview.getContext().getString(R.string.subtitle_hold_cart));
                break;
            case CATEGORY:
                textview.setText(textview.getContext().getString(R.string.you_do_not_have_any_category_yet));
                break;
            case PAYMENT:
                textview.setText(textview.getContext().getString(R.string.empty_subtitle_payment_methods));
                break;
            case CASHDRAWER:
                textview.setText(textview.getContext().getString(R.string.empty_subtitle_cash_drawer));
                break;
            case OPTIONS:
                textview.setText(textview.getContext().getString(R.string.you_do_not_have_any_option_yet));
                break;
            case TAX:
                textview.setText(textview.getContext().getString(R.string.you_do_not_have_any_taxes_yet));
                break;
            case LOW_STOCK:
                textview.setText(textview.getContext().getString(R.string.no_low_stock_subtitle));
                break;
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
            case ORDER:
                textview.setText(textview.getContext().getString(R.string.no_order));
                break;
            case CART:
                textview.setText(textview.getContext().getString(R.string.your_bag_is_empty));
                break;
            case HOLD:
                textview.setText(textview.getContext().getString(R.string.empty_hold_cart));
                break;
            case CATEGORY:
                textview.setText(textview.getContext().getString(R.string.no_category));
                break;
            case PAYMENT:
                textview.setText(textview.getContext().getString(R.string.empty_payment_methods));
                break;
            case CASHDRAWER:
                textview.setText(textview.getContext().getString(R.string.empty_cash_drawer));
                break;
            case OPTIONS:
                textview.setText(textview.getContext().getString(R.string.no_options));
                break;
            case TAX:
                textview.setText(textview.getContext().getString(R.string.no_tax));
                break;
            case LOW_STOCK:
                textview.setText(textview.getContext().getString(R.string.no_low_stock));
                break;
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
            case ORDER:
                imageview.setImageResource(R.drawable.ic_empty_orders);
                break;
            case CART:
                imageview.setImageResource(R.drawable.ic_empty_cart);
                break;
            case HOLD:
                imageview.setImageResource(R.drawable.ic_empty_hold_cart);
                break;
            case CATEGORY:
                imageview.setImageResource(R.drawable.ic_empty_category);
                break;
            case PAYMENT:
                imageview.setImageResource(R.drawable.ic_empty_payment);
                break;
            case CASHDRAWER:
                imageview.setImageResource(R.drawable.ic_empty_cash_drawer);
                break;
            case OPTIONS:
                imageview.setImageResource(R.drawable.ic_empty_options);
                break;
            case TAX:
                imageview.setImageResource(R.drawable.ic_empty_tax);
                break;
            case LOW_STOCK:
                imageview.setImageResource(R.drawable.ic_empty_low_stock);
                break;
//            case NOTIFICATION:
//                imageview.setImageResource(R.drawable.ic_vector_empty_notification);
//                break;
//            default:
//                break;
        }
    }
}
