package com.webkul.mobikul.mobikulstandalonepos.connections;

import android.os.AsyncTask;
import android.util.Log;

import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * Created by aastha.gupta on 3/2/17.
 */

public class VersionChecker extends AsyncTask<String, String, String> {

    private String newVersion = "1.0";

    @Override
    protected String doInBackground(String... params) {
        try {
            Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID).get();
            Elements element = doc.getElementsByAttributeValue("itemprop", "softwareVersion");
//                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
//                    .referrer("http://www.google.com")
            if (element != null && !element.text().isEmpty())
                newVersion = element.text();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "doInBackground: " + newVersion);
        return newVersion;
    }
}
