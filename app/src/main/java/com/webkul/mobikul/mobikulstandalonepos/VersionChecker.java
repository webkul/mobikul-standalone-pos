package com.webkul.mobikul.mobikulstandalonepos;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;

/**
 * Created by aastha.gupta on 3/2/17.
 */

public class VersionChecker extends AsyncTask<String, String, String> {

    private String newVersion = "1.0";

    @Override
    protected String doInBackground(String... params) {
        try {
            Element element = Jsoup.connect("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select("div[itemprop=softwareVersion]")
                    .first();
            if (element != null)
                newVersion = element.ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }
}
