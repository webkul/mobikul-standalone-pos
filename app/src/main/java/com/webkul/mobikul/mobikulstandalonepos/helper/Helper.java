package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by aman.gupta on 13/1/18. @Webkul Software Private limited
 */

public class Helper {

    public static Uri saveToInternalStorage(Context context, Bitmap bitmapImage, String id) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, id + ".jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);

            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                assert fos != null;
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Uri path = Uri.fromFile(mypath);
        return path;
    }

    public static String fromCartModelToString(CartModel cartData) {
        if (cartData == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        String json = gson.toJson(cartData, type);
        return json;
    }

    public static CartModel fromStringToCartModel(String cartDataString) {
        if (cartDataString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<CartModel>() {
        }.getType();
        CartModel cartData = gson.fromJson(cartDataString, type);
        return cartData;
    }

}