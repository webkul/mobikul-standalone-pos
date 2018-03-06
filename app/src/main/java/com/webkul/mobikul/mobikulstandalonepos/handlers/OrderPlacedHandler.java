package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.mail.Mail;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

/**
 * Created by aman.gupta on 25/1/18. @Webkul Software Private limited
 */

public class OrderPlacedHandler {
    private Context context;

    public OrderPlacedHandler(Context context) {
        this.context = context;
    }

    public void printInvoice(OrderEntity orderData) {

    }

    public void mailInvoice(OrderEntity orderData) {
        new SendMail().execute("");

    }

    public void goToHome() {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(context, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Mail m = new Mail(ApplicationConstants.USERNAME_FOR_SMTP, ApplicationConstants.PASSWORD_FOR_SMTP);

            String[] toArr = {"anchit.makkar849@webkul.com", "aman.gupta39@webkul.com"/*, "vedesh.kumar198@webkul.com", "ratnesh@webkul.com"*/};
            m.setTo(toArr);
            m.setFrom(ApplicationConstants.USERNAME_FOR_SMTP);
            m.setSubject("This is a test email sent using SMTP from an Android device.");
            m.setBody("Email body. \n Need To Look how we will add Invoice and other attachements");

            try {
                if (m.send()) {
                    showReportMessage(true);
                } else {
                    showReportMessage(false);
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }


    public void showReportMessage(boolean isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Email was sent successfully.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Email was not sent.", Toast.LENGTH_LONG).show();
        }
    }

}
