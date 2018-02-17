package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;
import com.webkul.mobikul.mobikulstandalonepos.model.ProductCategoryModel;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.content.ContentValues.TAG;

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
        Log.d(TAG, "saveToInternalStorage: " + path);
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

    public static void shake(Context context, View view) {
        Vibrator vibrateObject = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 200 milliseconds
        vibrateObject.vibrate(300);
        view.startAnimation(AnimationUtils.loadAnimation(context, R.anim.shake_error));
    }

    public static final String TIME_SERVER = "time-a.nist.gov";

    public static long getCurrentNetworkTime() {
        NTPUDPClient timeClient = new NTPUDPClient();
        InetAddress inetAddress = null;
        long returnTime = 0;
        try {
            int SDK_INT = android.os.Build.VERSION.SDK_INT;
            if (SDK_INT > 8) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                        .permitAll().build();
                StrictMode.setThreadPolicy(policy);
                inetAddress = InetAddress.getByName(TIME_SERVER);
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        TimeInfo timeInfo = null;
        try {
            timeInfo = timeClient.getTime(inetAddress);

            //long returnTime = timeInfo.getReturnTime();   //local device time
            returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();   //server time

            Date time = new Date(returnTime);
            Log.d(TAG, "Time_from " + TIME_SERVER + ": " + time);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }
}