package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.databinding.CustomToastBinding;

/**
 * Created by vedesh.kumar on 18/12/17. @Webkul Software Private limited
 */

public class ToastHelper {

    public static void showToast(@NonNull Context context, @NonNull String msg, int duration) {
        showToast(context, msg, duration, 0);
    }

    public static void showToast(@NonNull Context context, @NonNull String msg, int duration, int icon) {
        if (!msg.isEmpty()) {
            CustomToastBinding customToastBinding = CustomToastBinding.inflate(LayoutInflater.from(context));
            customToastBinding.setMessage(Html.fromHtml(msg).toString());
            Toast toast = new Toast(context);
            toast.setGravity(Gravity.BOTTOM, 0, 150);
            toast.setView(customToastBinding.getRoot());
            toast.setDuration(duration);
            toast.show();
        }
    }
}