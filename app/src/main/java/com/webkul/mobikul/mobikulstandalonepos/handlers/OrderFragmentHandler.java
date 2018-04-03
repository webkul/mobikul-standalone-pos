package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.html.simpleparser.TableWrapper;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPRow;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CartActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ViewOrderDetails;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.mail.Mail;
import com.webkul.mobikul.mobikulstandalonepos.mail.SendMail;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity.TAG;
import static com.webkul.mobikul.mobikulstandalonepos.constants.BundleConstants.BUNDLE_ORDER_DATA;

/**
 * Created by aman.gupta on 30/1/18. @Webkul Software Private limited
 */

public class OrderFragmentHandler {

    private Context context;

    public OrderFragmentHandler(Context context) {
        this.context = context;
    }

    public void onClickOrderItem(OrderEntity orderData) {
        Intent i = new Intent(context, ViewOrderDetails.class);
        i.putExtra(BUNDLE_ORDER_DATA, orderData);
        context.startActivity(i);
    }

    public void generateInvoice(OrderEntity orderData) {
        ActivityCompat.requestPermissions((BaseActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            if (Helper.getInstanse().generateInvoice(context, orderData) != null) {
                File file = Helper.getInstanse().generateInvoice(context, orderData);
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                Uri path = Uri.fromFile(file);
                Intent pdfOpenIntent = new Intent(Intent.ACTION_VIEW);
                pdfOpenIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                pdfOpenIntent.setDataAndType(path, "application/pdf");
                try {
                    context.startActivity(pdfOpenIntent);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                    ToastHelper.showToast(context,
                            "No Application Available to View PDF",
                            Toast.LENGTH_SHORT);
                }
            } else
                ToastHelper.showToast(context,
                        "ERROR in generating pdf",
                        Toast.LENGTH_SHORT);
        }
    }

    public void sendInvoice(OrderEntity orderData) {
        ActivityCompat.requestPermissions((BaseActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            new SendMail(context, orderData).execute();
        }
    }

    public void returnItems(OrderEntity orderData, String returndOrderId) {
        if (returndOrderId.isEmpty()) {
            AppSharedPref.setCartData(context, Helper.fromCartModelToString(orderData.getCartData()));
            AppSharedPref.setReturnCart(context, true);
            AppSharedPref.setReturnOrderId(context, orderData.getOrderId() + "");
            AppSharedPref.setReturnCart(context, true);
            Intent i = new Intent(context, CartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, ViewOrderDetails.class);
            i.putExtra("order_id", returndOrderId);
            context.startActivity(i);
        }
    }
}