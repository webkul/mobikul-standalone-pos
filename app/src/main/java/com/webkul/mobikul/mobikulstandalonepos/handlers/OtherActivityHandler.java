package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.BaseActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.CurrencyActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.LowStockActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.OtherActivity;
import com.webkul.mobikul.mobikulstandalonepos.activity.ProductActivity;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Currency;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.fragment.AddProductFragment;
import com.webkul.mobikul.mobikulstandalonepos.fragment.CurrencyPreferenceFragment;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
import static com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager.unzip;
import static com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager.zip;

/**
 * Created by aman.gupta on 6/3/18. @Webkul Software Private limited
 */

public class OtherActivityHandler {

    private Context context;
    private String backupDBPath = Environment.getExternalStorageDirectory().getPath() + "/Mobikul-Pos-db-backup";
    File ImageDirectory;

    public OtherActivityHandler(Context context) {
        this.context = context;
        ContextWrapper cw = new ContextWrapper(context);
        ImageDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    }

    public void lowStockProducts() {
        Intent i = new Intent(context, LowStockActivity.class);
        context.startActivity(i);
    }

    public void selectCurrency() {
        Intent i = new Intent(context, CurrencyActivity.class);
        context.startActivity(i);
    }

    public void currencyConfig() {
        Intent i = new Intent(context, CurrencyActivity.class);
        i.putExtra("currency_config", "");
        context.startActivity(i);
    }

    public void exportDB() {
        File data = Environment.getDataDirectory();
        FileChannel src = null;
        FileChannel dst = null;
        String currentDBPath = ApplicationConstants.DB_FILEPATH;

        File currentDB = new File(data, currentDBPath);
        final File backupDBFolder = new File(backupDBPath);
        backupDBFolder.mkdirs();
        final File backupDB = new File(backupDBFolder, "/db_pos.db");
        try {
            ActivityCompat.requestPermissions((OtherActivity) context, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
            }, 666);

            src = new FileInputStream(currentDB).getChannel();
            dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            String imageZipPath = exportImages();
            String[] s = new String[2];
            s[0] = backupDB.getAbsolutePath();
            s[1] = imageZipPath;
            zip(s, backupDBPath + "/pos_demo.zip");
            backupDB.delete();
            File imageZip = new File(imageZipPath);
            imageZip.delete();
            SweetAlertDialog sweetAlert = new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE);
            sweetAlert.setTitleText(context.getString(R.string.db_exported))
                    .setContentText("Path- " + backupDBPath + "/pos_demo.zip") /*+ " Do you want to see?"*/
                    .setConfirmText(context.getResources().getString(R.string.dialog_ok))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismissWithAnimation();
                        }
                    })
                    .show();
            src.close();
            dst.close();
            ToastHelper.showToast(context, context.getString(R.string.db_exported), Toast.LENGTH_LONG);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String exportImages() {
        String[] s = new String[1];
        s[0] = backupDBPath;

        if (ZipManager.zipFileAtPath(ImageDirectory.getAbsolutePath(), backupDBPath + "/db_pos_images.zip")) {
            Log.d(TAG, "exportImages: + Success");
        } else
            Log.d(TAG, "exportImages: + Fail");
        return backupDBPath + "/db_pos_images.zip";
    }

    public void importDB() {
        // TODO Auto-generated method stub
        ActivityCompat.requestPermissions((OtherActivity) context, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 666);

        try {
            //for samsung device...
            Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            ((OtherActivity) context).startActivityForResult(intent, 7);
        } catch (ActivityNotFoundException e1) {
            Intent intent = null;
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            ((OtherActivity) context).startActivityForResult(intent, 7);
        }
    }

    public void onActivityResultCustom(String backupDBPath) {
        try {
            ImportDB importDB = new ImportDB(context);
            importDB.execute(backupDBPath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ImportDB extends AsyncTask<String, Void,
            Boolean> {

        private Context context;

        public ImportDB(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SweetAlertBox.getInstance().showProgressDialog(context, "DB Importing...", "");
        }

        @Override
        protected Boolean doInBackground(String... backupDBPath) {
            try {
                File sd = Environment.getExternalStorageDirectory();
                File data = Environment.getDataDirectory();
                if (sd.canWrite()) {
                    final File backupDBFolder = new File(sd.getPath());
                    String currentDBPath = ApplicationConstants.DB_FILEPATH;
                    unzip(backupDBPath[0], backupDBFolder.getPath());

                    File backupDB = new File(data, currentDBPath);
                    File currentDB = new File(backupDBFolder.getPath(), "/db_pos.db");

                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    currentDB.delete();
                    imageImport(backupDBFolder.getAbsolutePath(), "/" + "db_pos_images.zip");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess) {
            super.onPostExecute(isSuccess);
            if (isSuccess) {
                SweetAlertBox.getInstance().dissmissSweetAlert();
                SweetAlertBox.getInstance().showSuccessPopUp(context, context.getString(R.string.db_imported), context.getString(R.string.subtitle_dp_imported));
                ToastHelper.showToast(context, context.getString(R.string.db_imported), Toast.LENGTH_LONG);
            } else
                SweetAlertBox.getInstance().showSuccessPopUp(context, context.getString(R.string.db_imported), context.getString(R.string.subtitle_dp_imported));
        }

        private void imageImport(String currentPath, String fileName) {
            File backupDB = new File(ImageDirectory.getAbsolutePath());
            if (!backupDB.exists())
                backupDB.mkdirs();
            try {
                ZipManager.unzipFolder(currentPath + fileName, backupDB.getAbsolutePath().replace("/app_imageDir", ""));
                File imageZip = new File(currentPath + fileName);
                imageZip.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
