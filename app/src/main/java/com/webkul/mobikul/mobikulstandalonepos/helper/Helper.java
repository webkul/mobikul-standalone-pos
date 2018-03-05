package com.webkul.mobikul.mobikulstandalonepos.helper;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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
        long returnTime = 0;
        NTPUDPClient timeClient = new NTPUDPClient();
        timeClient.setDefaultTimeout(1000);
        // Connect to network. Try again on timeout (max 6).
        for (int retries = 7; retries >= 0; retries--) {

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                // Try connecting to network to get time. May timeout.
                InetAddress inetAddress = InetAddress.getByName(TIME_SERVER);
                TimeInfo timeInfo = timeClient.getTime(inetAddress);
                long networkTimeLong = timeInfo.getMessage().getTransmitTimeStamp().getTime();

                // Convert long to Date, and Date to String. Log results.
                Date networkTimeDate = new Date(networkTimeLong);
                Log.i("Time", "Time from " + TIME_SERVER + ": " + networkTimeDate);
                returnTime = networkTimeDate.getTime();
                break;
                // Return resulting time as a String.
            } catch (IOException e) {
                // Max of 6 retries.
                Log.i("RTCTestActivity", "Unable to connect. Retries left: " + (retries - 1));
            }
        }
        return returnTime;
    }

    public static String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yy");
        String date = simpleDateFormat.format(new Date());
        return date;
    }

    public static int getViewWidth(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return view.getWidth();
    }

    public static int getViewHeight(final View view) {
        ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
        return view.getHeight();
    }

}