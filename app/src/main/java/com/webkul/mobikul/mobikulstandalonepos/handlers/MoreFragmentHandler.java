package com.webkul.mobikul.mobikulstandalonepos.handlers;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.webkul.mobikul.mobikulstandalonepos.BuildConfig;
import com.webkul.mobikul.mobikulstandalonepos.R;
import com.webkul.mobikul.mobikulstandalonepos.activity.*;
import com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants;
import com.webkul.mobikul.mobikulstandalonepos.db.AppDatabase;
import com.webkul.mobikul.mobikulstandalonepos.db.entity.Product;
import com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref;
import com.webkul.mobikul.mobikulstandalonepos.helper.FileUtils;
import com.webkul.mobikul.mobikulstandalonepos.helper.SweetAlertBox;
import com.webkul.mobikul.mobikulstandalonepos.helper.ToastHelper;
import com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager;
import com.webkul.mobikul.mobikulstandalonepos.interfaces.DataBaseCallBack;
import com.webkul.mobikul.mobikulstandalonepos.model.MoreData;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.content.ContentValues.TAG;
import static com.webkul.mobikul.mobikulstandalonepos.constants.ApplicationConstants.*;
import static com.webkul.mobikul.mobikulstandalonepos.helper.AppSharedPref.USER_PREF;
import static com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager.unzip;
import static com.webkul.mobikul.mobikulstandalonepos.helper.ZipManager.zip;

/**
 * Created by aman.gupta on 5/1/18.
 */

public class MoreFragmentHandler {

    private Context context;
    private String backupDBPath = Environment.getExternalStorageDirectory().getPath() + "/Mobikul-Pos-db-backup";
    File ImageDirectory;

    public MoreFragmentHandler(Context context) {
        this.context = context;
        ContextWrapper cw = new ContextWrapper(context);
        ImageDirectory = cw.getDir("imageDir", Context.MODE_PRIVATE);
    }

    public void performAction(MoreData moreData) {
        Intent i;
        switch (moreData.getId()) {
            case MORE_MENU_CASH_DRAWER:
                i = new Intent(context, CashDrawerActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_CUSTOMERS:
                i = new Intent(context, CustomerActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_CATEGORIES:
                i = new Intent(context, CategoryActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_PRODUCTS:
                i = new Intent(context, ProductActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_MY_ACCOUNT_INFO:
                i = new Intent(context, MyAccountInfo.class);
                context.startActivity(i);
                break;
            case MORE_MENU_OPTIONS:
                i = new Intent(context, OptionsActivity.class);
                context.startActivity(i);
                break;
            case MORE_MENU_PAYMENT_METHODS:
                i = new Intent(context, PaymentMethodActivity.class);
                context.startActivity(i);
                break;
            default:
                ToastHelper.showToast(context, "THIS OPTION IS UNDER DEVELOPMENT MODE!!", Toast.LENGTH_LONG);
        }
    }

    public void signOut() {
        AppSharedPref.getSharedPreferenceEditor(context, USER_PREF).clear().apply();
        Intent i = new Intent(context, SignUpSignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(i);
        ((MainActivity) context).finish();
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
            ActivityCompat.requestPermissions((MainActivity) context, new String[]{
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
        ActivityCompat.requestPermissions((MainActivity) context, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, 666);

        try {
            //for samsung device...
            Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
            intent.putExtra("CONTENT_TYPE", "*/*");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            ((MainActivity) context).startActivityForResult(intent, 7);
        } catch (ActivityNotFoundException e1) {
            Intent intent = null;
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            ((MainActivity) context).startActivityForResult(intent, 7);
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