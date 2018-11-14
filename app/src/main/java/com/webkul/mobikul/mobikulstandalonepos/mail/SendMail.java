package com.webkul.mobikul.mobikulstandalonepos.mail;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;

import java.io.File;

public class SendMail extends AsyncTask<Void, Integer, Boolean> {

    private ProgressDialog progressDialog;
    File invoiceFile;
    private Context context;
    private OrderEntity orderData;
    private boolean isAutomatically = false;

    public SendMail(Context context, OrderEntity orderEntity) {
        this.context = context;
        this.orderData = orderEntity;
    }

    public SendMail(Context context, OrderEntity orderEntity, boolean isAutomatically) {
        this.context = context;
        this.orderData = orderEntity;
        this.isAutomatically = isAutomatically;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (!isAutomatically)
            progressDialog = ProgressDialog.show(context, "Please wait", "Sending mail", true, false);
        if (Helper.getInstanse().generateInvoice(context, orderData) != null) {
            invoiceFile = Helper.getInstanse().generateInvoice(context, orderData);
        }
    }

    protected Boolean doInBackground(Void... voids) {
        Mail m = new Mail(ApplicationConstants.USERNAME_FOR_SMTP, ApplicationConstants.PASSWORD_FOR_SMTP);

        String[] toArr = {orderData.getCartData().getCustomer().getEmail()};
        m.setTo(toArr);
        m.setFrom(ApplicationConstants.USERNAME_FOR_SMTP);
        m.setSubject("Mobikul POS - Order " + orderData.getOrderId());
        m.setBody("Thank you for choosing " + context.getString(R.string.app_name) + ". Your order has been completed.\n" +
                "\n" +
                "To view your order, open your " + invoiceFile.getName() + ", which is attached here.");
        if (invoiceFile != null)
            try {
                m.addAttachment(invoiceFile.getAbsolutePath(), invoiceFile.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        try {
            m.send();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean isSent) {
        super.onPostExecute(isSent);
        if (!isAutomatically) {
            try {
                if (isSent) {
                    showReportMessage(true);
                } else {
                    showReportMessage(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MailApp", "Could not send email", e);
            }
            progressDialog.dismiss();
        }
    }

    public void showReportMessage(boolean isSuccess) {
        if (isSuccess) {
            ToastHelper.showToast(context, "Invoice has been sent successfully.", Toast.LENGTH_LONG);
        } else {
            ToastHelper.showToast(context, "Email was not sent.", Toast.LENGTH_LONG);
        }
    }
}
