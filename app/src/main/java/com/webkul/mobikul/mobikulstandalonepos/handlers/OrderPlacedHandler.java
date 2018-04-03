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
import android.text.Html;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.MainActivity;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.OrderEntity;
import com.webkul.mobikul.mobikulstandalonepos.helper.Helper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.mail.Mail;
import com.webkul.mobikul.mobikulstandalonepos.mail.SendMail;
import com.webkul.mobikul.mobikulstandalonepos.model.CartModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by aman.gupta on 25/1/18. @Webkul Software Private limited
 */

public class OrderPlacedHandler {
    private Context context;
    String[] invoiceTitlelistArray, invoicepricelistArray, invoiceQNTlistArray, invoiceNOlistArray;
    TextView path_pdf;
    private String invoiceFolder = Environment.getExternalStorageDirectory().getPath() + "/Mobikul-Pos-Invoices";
    private File file;

    public OrderPlacedHandler(Context context) {
        this.context = context;
    }

    public void printInvoice(OrderEntity orderData) {
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

    public void mailInvoice(OrderEntity orderData) {
        ActivityCompat.requestPermissions((BaseActivity) context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 666);
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            new SendMail(context, orderData).execute();
        }
    }

    public void goToHome() {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(i);
    }
}
